package com.learning.demo.aop;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author yuehewei <yuehewei@kuaishou.com>
 * Created on 2024-07-11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
@Documented
public @interface Idempotent {

    /**
     * 幂等操作的唯一标识，使用spring  el表达式  用#来引用方法参数
     * @return Spring-EL  expression
     */
    String key() default "";

    /**
     * 有效期 默认1 有效期大于程序执行时间，否则请求还是可能进来
     * @return
     */
    int expireTime() default 10;

    /**
     * 时间单位 默认 秒
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 提示信息，可自定义
     * @return
     */
    String info() default "";

    /**
     * 是否在业务完成后删除key true：删除 false：不删除
     * @return
     */
    boolean delKey() default false;
}
