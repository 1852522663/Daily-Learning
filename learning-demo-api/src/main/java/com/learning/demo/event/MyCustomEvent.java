package com.learning.demo.event;

import org.springframework.context.ApplicationEvent;

/**
 * 定义事件类
 * @author yuehewei <yuehewei@kuaishou.com>
 * Created on 2024-11-28
 */
public class MyCustomEvent extends ApplicationEvent {
    private String message;
    public MyCustomEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
    public String getMsg() {
        return message;
    }

}
