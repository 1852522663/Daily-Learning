package com.learning.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;



@SpringBootApplication(scanBasePackages = "com.learning.demo")
@MapperScan("com.learning.demo.mapper")
@EnableScheduling
public class LearningDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearningDemoApplication.class,args);
    }
}
