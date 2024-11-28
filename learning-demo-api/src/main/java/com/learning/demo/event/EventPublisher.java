package com.learning.demo.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author yuehewei <yuehewei@kuaishou.com>
 * Created on 2024-11-28
 */
@Component
public class EventPublisher {
    @Autowired
    private  ApplicationEventPublisher publisher;

    /**
     * 发布事件
     * @param message
     */
    public void publish(String message) {
        MyCustomEvent customEvent = new MyCustomEvent(this, message);
        publisher.publishEvent(customEvent);
    }

}
