package com.learning.demo.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author yuehewei <yuehewei@kuaishou.com>
 * Created on 2024-11-28
 */
@Component
public class MyEventListener {

    @EventListener
    public void handleMyCustomEvent(MyCustomEvent event) {
        // 监听事件
        System.out.println("Received spring custom event - " + event.getMsg());
    }
}
