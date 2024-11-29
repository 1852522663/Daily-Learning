package com.learning.demo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author yuehewei <yuehewei@kuaishou.com>
 * Created on 2024-11-29
 */
@Aspect
@Component
public class LogOutputAspect {

    private static final Logger log =  LoggerFactory.getLogger(LogOutputAspect.class);


    @Pointcut("@annotation(com.learning.demo.aop.LogOutput)")
    public void LogOutput() {
    }

    @Around("LogOutput()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 使用反射获取方法上的注解
        LogOutput annotation = method.getAnnotation(LogOutput.class);
        if (annotation == null) {
            // 如果通过方法获取注解失败，尝试通过类获取注解
            annotation = method.getDeclaringClass().getAnnotation(LogOutput.class);
        }
        // 获取方法的参数名称和参数值
        Object[] args = joinPoint.getArgs();
        log.info("------> {} 方法被调用; 入参：{}",annotation.value(),args);
        return joinPoint.proceed();
    }



}
