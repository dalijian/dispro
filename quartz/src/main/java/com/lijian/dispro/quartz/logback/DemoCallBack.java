package com.lijian.dispro.quartz.logback;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.concurrent.TimeUnit;

public class DemoCallBack implements Callback {
    private final long startTime;
    private final String  key;
    private final String message;

    public DemoCallBack(long startTime, String  messageNo, String messageStr) {
        this.startTime = startTime;
        this.key = messageNo;
        this.message = messageStr;
    }

    @Override
    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
        long elapsedTime = System.currentTimeMillis()-startTime;

        if (recordMetadata != null) {
            System.out.println("message {" + key + "," + message + "} send to partition{" + recordMetadata.partition() + "}," + "offset(" + recordMetadata.offset() + ") in" + elapsedTime + "ms");
            try {
                System.out.println("开始 睡眠5s");
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

        }
    }
}
