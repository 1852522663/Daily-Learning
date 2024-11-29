package com.learning.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * spring内置的日志记录
 * @author yuehewei <yuehewei@kuaishou.com>
 * Created on 2024-11-29
 */
@Configuration
public class RequestLoggingConfig {
    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);//记录URL上的参数
        filter.setIncludePayload(true);//记录请求参数的值
        filter.setIncludeHeaders(true);//记录请求头
        filter.setIncludeClientInfo(true);//记录客户端信息
        filter.setAfterMessagePrefix("REQUEST DATA-");//请求结束后打印的日志前缀
        return filter;
    }
}
