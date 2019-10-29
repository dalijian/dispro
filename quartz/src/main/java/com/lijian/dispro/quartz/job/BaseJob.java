package com.lijian.dispro.quartz.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@DisallowConcurrentExecution
@PersistJobDataAfterExecution
@Component
public abstract class  BaseJob  implements Job {

    ObjectMapper mapper = new ObjectMapper();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    @Qualifier(value = "disproJdbcTemplate")
    protected JdbcTemplate disproJdbcTemplate;

    /***
     *  向 数据库 存储 job 执行 结果
     * @param context JobExecutionContext
     * @param resultJson job 执行 结果，  json 字符串
     * @param isExcel    执行结果 是否可 生成 excel  报表 1 是 0 否
     * @param recordDescribe 结果 描述
     */


    protected void saveToDatabase(JobExecutionContext context, String resultJson, String isExcel, String recordDescribe) {
        JobDetail jobDetail = context.getJobDetail();
        String jobName = jobDetail.getKey().getName();
        String jobGroup = jobDetail.getKey().getGroup();
        String  json  = resultJson;
        String createTime = simpleDateFormat.format(new Date());

        String sql = "insert into quartz_job_record(job_name,job_group,create_time,result,is_excel,record_describe)values (?,?,?,?,?,?)";
        disproJdbcTemplate.update(sql, jobName, jobGroup, createTime, json, isExcel, recordDescribe);
    }


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        Map<String,Object> map = getSubData();
        String json = (String) map.get("json");
        String isExcel = (String) map.get("isExcel");
        String describe = (String) map.get("describe");

        saveToDatabase(context, json, isExcel, describe);



    }

    public  abstract Map<String, Object> getSubData();
}
