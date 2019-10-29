package com.lijian.dispro.quartz.transaction;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class WorkTwo implements Runnable {

    private CountDownLatch countDownLatch;

    private volatile boolean flag;

    private List<TransactionStatus> statusList;

    private JdbcTemplate jdbcTemplate;

    private PlatformTransactionManager manager;

    public WorkTwo(CountDownLatch countDownLatch, boolean flag, List<TransactionStatus> statusList, JdbcTemplate jdbcTemplate, PlatformTransactionManager manager) {
        this.countDownLatch = countDownLatch;
        this.flag = flag;
        this.statusList = statusList;
        this.jdbcTemplate = jdbcTemplate;
        this.manager = manager;
    }

    String sql = " insert into table_a (`name`,age,score) values(?,?,?)";
    @Override
    public void run() {
        DefaultTransactionDefinition def2 = new DefaultTransactionDefinition();
        def2.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status =  manager.getTransaction(def2);
        jdbcTemplate.update(sql, "lijian", "20", "90");
        statusList.add(status);
        countDownLatch.countDown();
    }
}
