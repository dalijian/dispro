package com.lijian.dispro.quartz.controller;


import com.lijian.dispro.quartz.SchedulerManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/quartz")
@Api(tags = {"定时任务"})
public class QuartzController  {

    Logger log = LoggerFactory.getLogger(QuartzController.class);
    @Autowired
    public SchedulerManager myScheduler;


    @ApiOperation(value = "暂停任务", notes = "暂停任务")
    @RequestMapping(value = "/pauseJob", method = RequestMethod.GET)
    public String deleteScheduleJob2(
            @ApiParam(value = "任务名称") @RequestParam(value = "jobName") String name,
            @ApiParam(value = "任务所在群组") @RequestParam(value = "jobGroup") String group
    ) {
        try {
            myScheduler.pauseJob(name, group);
            return "暂停定时器成功";
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return "暂停定时器失败";
    }

    @ApiOperation(value = "恢复任务", notes = "恢复任务")
    @RequestMapping(value = "/resumeJob", method = RequestMethod.GET)
    public String resumeScheduleJob2(
            @ApiParam(value = "任务名称") @RequestParam(value = "jobName") String name,
            @ApiParam(value = "任务所在群组") @RequestParam(value = "jobGroup") String group
    ) {
        try {
            myScheduler.resumeJob(name, group);
            return "恢复定时器成功";
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return "恢复定时器失败";
    }

    @ApiOperation(value = "查询任务", notes = "查询任务")
    @RequestMapping(value = "/findAllJob", method = RequestMethod.GET)
    public Map<String, Object> findAllScheduleJobs(
            @ApiParam(value = "任务名称") @RequestParam(value = "workName",required = false) String jobName,
            @ApiParam(value = "pageNo") @RequestParam(value = "pageNo", required = false,defaultValue = "1") String pageNo,
            @ApiParam(value = "pageSize") @RequestParam(value = "pageSize", required = false,defaultValue = "10") String pageSize

    ) {
        try {
            return myScheduler.findAll(jobName,pageNo,pageSize);

        } catch (SchedulerException e) {

            e.printStackTrace();
        }

        return null;
    }

}

