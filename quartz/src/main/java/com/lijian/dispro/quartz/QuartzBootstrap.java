package com.lijian.dispro.quartz;

import com.lijian.dispro.quartz.job.JobTest;
import com.lijian.dispro.quartz.job.WeatherCrawl;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class QuartzBootstrap {

    @Autowired
    public SchedulerManager myScheduler;

    //    自定义 任务启动
    @PostConstruct
    public void start() {

        try {
            myScheduler.startJob("0 0 0 1/1 * ?", "job_crawl_weather", "job_crawl", "定时抓取天气（一天一次）", WeatherCrawl.class);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
