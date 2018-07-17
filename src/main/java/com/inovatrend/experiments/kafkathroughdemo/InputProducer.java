package com.inovatrend.experiments.kafkathroughdemo;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class InputProducer implements  Runnable {

    private KafkaProducer<String, String> producer;
    private boolean stopped = false;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public InputProducer() {

        Properties producerConfig = new Properties();
        producerConfig.put(ProducerConfig.ACKS_CONFIG, "1");
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producer = new KafkaProducer<>(producerConfig);

        new Thread(this).start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            stopped = true;
            producer.close();
        }));

    }

    @Override
    public void run() {
        int msgNo = 1;
        while ( !stopped) {
            String randomMessage = "Message no " + (msgNo) + " produced at " + LocalDateTime.now().format(formatter);
            ProducerRecord<String, String> record = new ProducerRecord<>("input-topic", "key-always-same", randomMessage);
            producer.send(record);
            sleepQuietly(100);
        }
    }

    private void sleepQuietly(long howLongInMillis) {
        try {
            Thread.sleep(howLongInMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new InputProducer();
    }


}
