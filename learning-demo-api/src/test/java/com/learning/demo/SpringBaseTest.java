package com.learning.demo;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;



@SpringBootTest(classes = LearningDemoApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public abstract class SpringBaseTest {

}
