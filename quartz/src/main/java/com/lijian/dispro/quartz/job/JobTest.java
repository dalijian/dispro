package com.lijian.dispro.quartz.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.stereotype.Component;

@DisallowConcurrentExecution
@PersistJobDataAfterExecution
@Component
public class JobTest extends BaseJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("job'name" + context.getJobDetail().getKey().getName() + ",job'group" +
                context.getJobDetail().getKey().getGroup());
    }

}
