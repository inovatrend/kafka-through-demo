package com.inovatrend.experiments.kafkathroughdemo;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class IntermediateProducer implements  Runnable {

    private KafkaProducer<String, String> producer;
    private boolean stopped = false;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public IntermediateProducer() {

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
        while ( !stopped) {
            String randomMessage = "This message is not from input-topic.  " + LocalDateTime.now().format(formatter);
            ProducerRecord<String, String> record = new ProducerRecord<>("side-topic", "key-always-same", randomMessage);
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
        new IntermediateProducer();
    }


}
