package com.learning.demo.aop;

import com.learning.demo.model.User;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * @author yuehewei <yuehewei@learning.com>
 * Created on 2023-08-28
 */
@Component
@Aspect
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * 在目标方法执行之前执行，允许你在方法调用之前插入一些逻辑。 用户权限或记录日志
     * @param joinPoint
     */
    @Before(" execution (* com.learning.demo.controller.UserController.*(..))")
    public void logMethodParameters(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        logger.info("【logMethodParameters】Method {} called with arguments: {}", methodName, args);
    }

    /**
     * 环绕  获取方法名、入参和返回值    ProceedingJoinPoint是继承了JoinPoint并且只能在环绕中使用
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.learning.demo.controller.UserController.*(..))")
    public Object logMethodReturnValue(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取方法签名信息 例如：方法名，修饰符，返回类型
        String methodName = joinPoint.getSignature().getName();
        //获取方法入参
        Object[] args = joinPoint.getArgs();
        logger.info("【logMethodReturnValue】Method {} called with arguments: {}", methodName, args);
        //获取方法返回值
        Object result = joinPoint.proceed();
        logger.info("【logMethodReturnValue】Method returned with value: {}", result);
        return result;
    }

    /**
     * 在连接点方法执行后执行的方法   方法为add 且入参为user
     * @param joinPoint
     * @param user
     */
    @After("execution(* com.learning.demo.controller.UserController.add(com.learning.demo.model.User)) && args(user)")
    public void logUserCreation(JoinPoint joinPoint, User user) {
        logger.info("【logUserCreation】User created: " + user);
    }
}
