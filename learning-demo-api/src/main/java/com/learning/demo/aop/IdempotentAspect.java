package com.learning.demo.aop;

import groovy.util.logging.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author yuehewei <yuehewei@kuaishou.com>
 * Created on 2024-07-11
 */
@Aspect
@Slf4j
public class IdempotentAspect {


    private static final SpelExpressionParser PARSER = new SpelExpressionParser();

    private static final LocalVariableTableParameterNameDiscoverer DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

    /**
     * 线程私有map
     */
    private static final ThreadLocal<Map<String, Object>> THREAD_CACHE = ThreadLocal.withInitial(HashMap::new);

    private static final String KEY = "key";

    private static final String DEL_KEY = "delKey";

    // 以自定义 @Idempotent 注解为切点
    @Pointcut("@annotation(com.kuaishou.demo.aop.Idempotent)")
    public void idempotent() {
    }

    @Before("idempotent()")
    public void before(JoinPoint joinPoint) throws Throwable {
        //获取到当前请求的属性，进而得到HttpServletRequest对象，以便后续获取请求URL和参数信息。
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        //从JoinPoint中获取方法签名，并确认该方法是否被@Idempotent注解标记。如果是，则继续执行幂等性检查逻辑；如果不是，则直接返回，不进行幂等处理。。
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (!method.isAnnotationPresent(Idempotent.class)) {
            return;
        }
        Idempotent idempotent = method.getAnnotation(Idempotent.class);
        String key;
        // 若没有配置 幂等 标识编号，则使用 url + 参数列表作为区分;如果提供了key规则，则利用keyResolver根据提供的规则和切点信息生成键
        if (!StringUtils.hasLength(idempotent.key())) {
            String url = request.getRequestURL().toString();
            String argString = Arrays.asList(joinPoint.getArgs()).toString();
            key = url + argString;
        } else {
            // 使用jstl 规则区分
            key = resolver(idempotent, joinPoint);
        }
        //从注解中读取并设置幂等操作的过期时间、描述信息、时间单位以及是否删除键的标志。
        long expireTime = idempotent.expireTime();
        String info = idempotent.info();
        TimeUnit timeUnit = idempotent.timeUnit();
        boolean delKey = idempotent.delKey();
        String value = LocalDateTime.now().toString().replace("T", " ");
        Map<String, Object> map = THREAD_CACHE.get();
        map.put(KEY, key);
        map.put(DEL_KEY, delKey);
    }


    /**
     * 从注解的方法的参数中解析出用于幂等性处理的键值（key）
     *
     * @param idempotent
     * @param point
     * @return
     */
    private String resolver(Idempotent idempotent, JoinPoint point) {
        //获取被拦截方法的所有参数
        Object[] arguments = point.getArgs();
        //从字节码的局部变量表中解析出参数名称
        String[] params = DISCOVERER.getParameterNames(getMethod(point));
        //SpEL表达式执行的上下文环境，用于存放变量
        StandardEvaluationContext context = new StandardEvaluationContext();
        //遍历方法参数名和对应的参数值，将它们一一绑定到StandardEvaluationContext中。
        //这样SpEL表达式就可以引用这些参数值
        if (params != null && params.length > 0) {
            for (int len = 0; len < params.length; len++) {
                context.setVariable(params[len], arguments[len]);
            }
        }
        //使用SpelExpressionParser来解析Idempotent注解中的key属性，将其作为SpEL表达式字符串
        Expression expression = PARSER.parseExpression(idempotent.key());
        //转换结果为String类型返回
        return expression.getValue(context, String.class);
    }

    /**
     * 根据切点解析方法信息
     *
     * @param joinPoint 切点信息
     * @return Method    原信息
     */
    private Method getMethod(JoinPoint joinPoint) {
        //将joinPoint.getSignature()转换为MethodSignature
        //Signature是AOP中表示连接点签名的接口，而MethodSignature是它的具体实现，专门用于表示方法的签名。
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取到方法的声明。这将返回代理对象所持有的方法声明。
        Method method = signature.getMethod();

        //判断获取到的方法是否属于一个接口
        //因为在Java中，当通过Spring    AOP或其它代理方式调用接口的方法时，实际被执行的对象是一个代理对象，直接获取到的方法可能来自于接口声明而不是实现类。
        if (method.getDeclaringClass().isInterface()) {
            try {
                //通过反射获取目标对象的实际类（joinPoint.getTarget().getClass()）中同名且参数类型相同的方法
                //这样做是因为代理类可能对方法进行了增强，直接调用实现类的方法可以确保获取到最准确的实现细节
                method = joinPoint.getTarget().getClass().getDeclaredMethod(joinPoint.getSignature().getName(),
                        method.getParameterTypes());
            } catch (SecurityException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return method;
    }


    @After("idempotent()")
    public void after() throws Throwable {
        Map<String, Object> map = THREAD_CACHE.get();
        if (CollectionUtils.isEmpty(map)) {
            return;
        }

        String key = map.get(KEY).toString();
        boolean delKey = (boolean) map.get(DEL_KEY);

        //无论是否移除了键，最后都会清空当前线程局部变量THREAD_CACHE中的数据，避免内存泄漏
        THREAD_CACHE.remove();
    }



}
