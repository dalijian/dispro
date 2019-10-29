package com.lijian.dispro.quartz.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class TransactionCurrency {


    @Autowired
    @Qualifier(value = "disproJdbcTemplate")
    private JdbcTemplate disproJdbcTemplate;

    volatile boolean flag =true;


    @Autowired
    private TransactionTemplate transactionTemplate;



    @Transactional
    public void save() throws InterruptedException {
        PlatformTransactionManager manager = transactionTemplate.getTransactionManager();
        CountDownLatch countDownLatch = new CountDownLatch(2);
        List<TransactionStatus> list = new ArrayList<>();
        WorkOne workOne = new WorkOne(countDownLatch,flag,list,disproJdbcTemplate,manager);
        WorkTwo workTwo = new WorkTwo(countDownLatch,flag,list,disproJdbcTemplate,manager);

        ExecutorService service = Executors.newCachedThreadPool();

        service.execute(workOne);
        service.execute(workTwo);

        countDownLatch.await();
        for (int i = 0; i < list.size(); i++) {
            manager.commit(list.get(i));

        }
    }
}
