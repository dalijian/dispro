package com.lijian.dispro.quartz.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class MyKafkaAppender extends AppenderBase<ILoggingEvent>  {

    public static boolean isAsync =false;
    public static  KafkaProducer producer = buildProducer();
    public static String topic = "logback-test-test";
    public static KafkaProducer buildProducer(){

        boolean isAsync = true;
        Properties properties = new Properties();
//    设置服务端地址
        properties.put("bootstrap.servers", "127.0.0.1:9092");
//        设置客户端id
        properties.put("client.id", "DemoProducer");

        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer producer = new KafkaProducer(properties);

        String topic = "logback";
        return producer;
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
       String message = eventObject.getMessage();

        send(String.valueOf(System.currentTimeMillis()), message);
        System.out.println("************************");

    }

    public void send(String messageKey,String messageValue) {
//        boolean isAsync = args.length == 0 || !args[0].trim().equalsIgnoreCase("sync");


        int messageNo = 1;


            String messageStr = "Message_" + messageNo;
            long startTime = System.currentTimeMillis();
            ++messageNo;
            if (isAsync) {
                // 异步

                producer.send(new ProducerRecord(topic, messageKey, messageValue),
//                        设置回调函数
                        new DemoCallBack(startTime, messageKey, messageValue));
            } else {
                try {
                    //同步  阻塞当前 线程，等待 kafka 服务端 的 ack 响应
//                    producer.send()  得到Future ，调用 get() 阻塞当前线程
                    RecordMetadata recordMetadata = (RecordMetadata) producer.send(new ProducerRecord(topic, messageKey, messageValue)).get();
                    System.out.println("RecordMetadata:" + recordMetadata.toString());
                    System.out.println("Sent message:{" + messageKey + "," + messageValue + "}");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }

        }

}
