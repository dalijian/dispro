package com.lijian.dispro.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class QuartzApplication {
    Logger logger = LoggerFactory.getLogger(QuartzApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(QuartzApplication.class, args);
    }


}
