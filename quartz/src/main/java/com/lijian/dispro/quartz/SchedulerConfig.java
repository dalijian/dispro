package com.lijian.dispro.quartz;

import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;

/**
 * 定时配置（可以配置静态定时任务）
 */
@Configuration
public class SchedulerConfig {


    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();

        jobFactory.setApplicationContext(applicationContext);

        return jobFactory;
    }

//    不使用 自定义的 simpleJobTrigger
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory)
            throws IOException{
        SchedulerFactoryBean factory = new SchedulerFactoryBean();

        factory.setJobFactory(jobFactory);
        factory.setQuartzProperties(quartzProperties());
//        factory.setTriggers(simpleJobTrigger);
        return factory;
    }

    //SchedulerFactoryBean
//    @Bean
//    public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory, Trigger simpleJobTrigger)
//            throws IOException{
//        SchedulerFactoryBean factory = new SchedulerFactoryBean();
//
//        factory.setJobFactory(jobFactory);
//        factory.setQuartzProperties(quartzProperties());
//        factory.setTriggers(simpleJobTrigger);
//
//        return factory;
//    }






//    /**
//     * 静态方式配置定时任务
//     * @param jobDetail
//     * @return
//     */
//    @Bean
//    public CronTriggerFactoryBean simpleJobTrigger(@Qualifier("simpleJobDetail") JobDetail jobDetail) {
//        CronTriggerFactoryBean  factoryBean = new CronTriggerFactoryBean ();
//
//        factoryBean.setJobDetail(jobDetail);
//        factoryBean.setStartDelay(1000L);
//        factoryBean.setName("trigger1");
//        factoryBean.setGroup("group1");
//        //周1至周5，每天上午8点至下午18点，每分钟执行一次
//        factoryBean.setCronExpression("0 0/1 8-18 ? * 2-6");
//
//        return factoryBean;
//    }

//    @Bean
//    public JobDetailFactoryBean simpleJobDetail() {
//        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
//
////        factoryBean.setJobClass(ScheduledJob.class);
////        factoryBean.setGroup("group1");
////        factoryBean.setName("job1");
//
//        return factoryBean;
//    }













    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        Resource resource = new ClassPathResource("quartz_druid.properties");
        propertiesFactoryBean.setLocation(resource);

        propertiesFactoryBean.afterPropertiesSet();
        Properties properties = propertiesFactoryBean.getObject();
        return properties;
    }

}
