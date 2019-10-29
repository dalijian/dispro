package com.lijian.dispro.quartz;

import com.lijian.dispro.quartz.transaction.TransactionCurrency;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;
//测试注解上 加上 @Transactional 会导致 数据 不能持久化
//测试 多线程 事务
public class TrasactionCurrencyTest extends QuartzApplicationTests {

    @Autowired
    @Qualifier(value = "disproJdbcTemplate")
    private JdbcTemplate disproJdbcTemplate;


    @Autowired
    private TransactionCurrency transactionCurrency;


    @Test
//    @Transactional
    public void save() {

        String sql = " insert into table_a (`name`,age,score) values(?,?,?)";
        String sql2 = " insert into table_b (`name`,age,score) values(?,?,?)";
        String sql3 = " insert into table_c (`name`,age,score) values(?,?,?)";

        disproJdbcTemplate.update(sql, "lijian", "20", "90");
        disproJdbcTemplate.update(sql2, "lijian", "20", "90");
        disproJdbcTemplate.update(sql3, "lijian", "20", "90");
//        Thread thread = new Thread(() -> {
//            disproJdbcTemplate.update(sql, "lijian", "20", "90");
//
//        });
//
//        Thread thread1 = new Thread(() -> {
//            disproJdbcTemplate.update(sql2, "lijian", "20", "90");
//        });
//
//        Thread thread2 = new Thread(() -> {
//            disproJdbcTemplate.update(sql3, "lijian", "2jian", "90");
//        });
//
//        thread.start();
//        thread1.start();
//        thread2.start();

        try {
            TimeUnit.SECONDS.sleep(5L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void save2(){
        try {
            transactionCurrency.save();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
