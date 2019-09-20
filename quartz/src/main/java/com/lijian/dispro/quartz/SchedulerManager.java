package com.lijian.dispro.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/*
 * 此处可以注入数据库操作，查询出所有的任务配置
 */
@Component
public class SchedulerManager {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
    private JobListener jobListener;

    /**
     * 开始定时任务
     *
     * @param jobName
     * @param jobGroup
     * @throws SchedulerException
     */
    public void startJob(String cron, String jobName, String jobGroup, String describe, Class<? extends Job> jobClass) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        if (jobListener == null) {
            jobListener = new QuartzJobListener();
            scheduler.getListenerManager().addJobListener(jobListener);
        }
        JobKey jobKey = new JobKey(jobName, jobGroup);
        if (!scheduler.checkExists(jobKey)) {
            scheduleJob(cron, scheduler, jobName, jobGroup, describe, jobClass);
        }else{
            List<? extends Trigger> trigger = scheduler.getTriggersOfJob(new JobKey(jobName, jobGroup));

//           创建 job 时  设置 job 与 trigger 是  1对1
            String triggerName = trigger.get(0).getKey().getName();
            String triggerGroup = trigger.get(0).getKey().getGroup();
//            设置时区 Asia/Shanghai
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron).inTimeZone(TimeZone.getTimeZone("Asia/Shanghai")).withMisfireHandlingInstructionFireAndProceed();
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup)
                    .withSchedule(scheduleBuilder)
                    .build();
            scheduler.rescheduleJob(new TriggerKey(triggerName, triggerGroup),cronTrigger);
        }

    }

    /**
     * 移除定时任务
     *
     * @param jobName
     * @param jobGroup
     * @throws SchedulerException
     */
    public void deleteJob(String jobName, String jobGroup) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = new JobKey(jobName, jobGroup);
        scheduler.deleteJob(jobKey);
    }

    /**
     * 暂停定时任务
     *
     * @param jobName
     * @param jobGroup
     * @throws SchedulerException
     */
    public void pauseJob(String jobName, String jobGroup) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = new JobKey(jobName, jobGroup);
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复定时任务
     *
     * @param jobName
     * @param jobGroup
     * @throws SchedulerException
     */
    public void resumeJob(String jobName, String jobGroup) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = new JobKey(jobName, jobGroup);
        scheduler.resumeJob(jobKey);
    }

    /**
     * 清空所有当前scheduler对象下的定时任务【目前只有全局一个scheduler对象】
     *
     * @throws SchedulerException
     */

    public void clearAll() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.clear();
    }

    /**
     * 查看所有当前scheduler对象下的定时任务【目前只有全局一个scheduler对象】
     *
     * @param jobName
     * @param pageNo
     * @param pageSize
     * @return
     * @throws SchedulerException
     */

    public Map<String, Object> findAll(String jobName, String pageNo, String pageSize) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        Set<JobKey> list = null;

        list = scheduler.getJobKeys(GroupMatcher.anyGroup());


        List<Map<String, String>> keyList = list.stream().map(x -> createMap(x)).collect(Collectors.toList());
        if (!StringUtils.isEmpty(jobName)) {
            keyList = keyList.stream().filter(x -> x.get("jobName").contains(jobName)).collect(Collectors.toList());

        }
        List<Map<String, String>> result = keyList.stream().
                skip((Long.parseLong(pageNo) - 1) * Integer.parseInt(pageSize)).limit(Long.parseLong(pageSize)).collect(Collectors.toList());

        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("content", result);
        modelMap.put("total", keyList.size());
        return modelMap;
    }

    private Map<String, String> createMap(JobKey x) {

        Map<String, String> keyMap = new ConcurrentHashMap<>();
        keyMap.put("jobName", x.getName());
        keyMap.put("jobGroup", x.getGroup());
        Scheduler scheduler = schedulerFactoryBean.getScheduler();


//        Trigger.TriggerState trigger = scheduler.getTriggerState(triggerKey);
//        JobDetail detail = scheduler.getJobDetail(x);
//        detail.getJobDataMap().getLongValue(detail.getKey().);
        try {
//            默认 创建 job 时 trigger_name ,trigger_group , 与 job_name,job_group 相同
            Trigger.TriggerState state = scheduler.getTriggerState(new TriggerKey(x.getName(), x.getGroup()));
            switch (state) {
                case ERROR:
                    keyMap.put("status", "错误");
                    break;
                case NONE:
                    keyMap.put("status", "不存在");
                    break;
                case NORMAL:
                    keyMap.put("status", "正常");
                    break;
                case PAUSED:
                    keyMap.put("status", "暂停");
                    break;
                case BLOCKED:
                    keyMap.put("status", "阻塞");
                    break;
                case COMPLETE:
                    keyMap.put("status", "完成 ");
                    break;
            }
            String describe = scheduler.getJobDetail(x).getDescription();
            keyMap.put("describe", describe);
            List<? extends Trigger> trigger = scheduler.getTriggersOfJob(x);

//           创建 job 时  设置 job 与 trigger 是  1对1
            keyMap.put("triggerName", trigger.get(0).getKey().getName());
            keyMap.put("triggerGroup", trigger.get(0).getKey().getGroup());
            String createTime = scheduler.getJobDetail(x).getJobDataMap().getString("createTime");
            keyMap.put("createTime", createTime);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        return keyMap;
    }


    /**
     * 动态创建Job
     * 此处的任务可以配置可以放到properties或者是放到数据库中
     * Trigger:name和group 目前和job的name、group一致，之后可以扩展归类
     *
     * @param scheduler
     * @throws SchedulerException
     */
    private void scheduleJob(String cron, Scheduler scheduler, String jobName, String jobGroup, String describe, Class<? extends Job> jobClass) throws SchedulerException {
        /*
         *  此处可以先通过任务名查询数据库，如果数据库中存在该任务，更新任务的配置以及触发器
         *  如果此时数据库中没有查询到该任务，则按照下面的步骤新建一个任务，并配置初始化的参数，并将配置存到数据库中
         */

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroup)
                .withDescription(describe)
                .usingJobData("createTime", simpleDateFormat.format(new Date()))
                .build();

        //错过策略 立即执行
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron).withMisfireHandlingInstructionFireAndProceed();
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup)
                .withSchedule(scheduleBuilder)
                .build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

}
