package com.example.springkafkaexample;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.kafka.annotation.EnableKafka;

import static org.springframework.boot.Banner.Mode.OFF;

@EnableKafka
@SpringBootApplication
public class SpringKafkaExampleApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(SpringKafkaExampleApplication.class)
                .bannerMode(OFF)
//                .listeners(new ApplicationPreparedEventHandler())
                .run(args);
    }
}
