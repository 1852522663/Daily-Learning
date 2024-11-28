package com.learning.demo.event;

import com.learning.demo.aop.LoggingAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author yuehewei <yuehewei@kuaishou.com>
 * Created on 2024-11-28
 */
@Component
public class MyEventListener {
    private static final Logger log = LoggerFactory.getLogger(MyEventListener.class);
    @EventListener
    public void handleMyCustomEvent(MyCustomEvent event) {
        // 监听事件
        log.info("Received spring custom event - " + event.getMsg());
    }
}
