package com.inovatrend.experiments.kafkathroughdemo;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

public class StreamingApp {

    private KafkaStreams streams;

    public StreamingApp() {

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "streaming-app");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        config.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 1000L);

        StreamsConfig streamConfig = new StreamsConfig(config);

        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> inputStream = builder.stream("input-topic");

        inputStream

                .mapValues(value -> {
                    System.out.println("Message read from input topic: " + value);
                    return value.toUpperCase();
                })

                .through("side-topic")

                .peek( (key, value) -> System.out.println("Message after through: " + value) )

                .to("output-topic");


        streams = new KafkaStreams(builder.build(), streamConfig);
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> streams.close()));
    }




    public static void main(String[] args) {
        new StreamingApp();
    }
}
