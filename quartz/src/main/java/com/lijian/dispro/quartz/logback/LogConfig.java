package com.lijian.dispro.quartz.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
@Component
public class LogConfig {

    Logger logger = LoggerFactory.getLogger(LogConfig.class);
//    @PostConstruct
//    public void addLog(){
//        for (int i = 0; i < 10000000; i++) {
//            logger.info("this message from logback No:{}",i);
//        }
//    }
}
