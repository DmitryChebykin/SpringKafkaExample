package com.example.springkafkaexample.service;

import com.example.springkafkaexample.dto.ChuckJokeResponseDto;
import com.example.springkafkaexample.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageGeneratorService {
    public static final String TOPIC = "test-topic";

    private final ChuckNorrisIOImpl chuckNorrisIO;

    private final KafkaProducer kafkaProducer;

    @Scheduled(fixedDelay = 5000, initialDelay = 1000)
    public void receiveAndSend() {
        ChuckJokeResponseDto joke = chuckNorrisIO.getJoke();

        log.info("Get joke: {}", joke);
        kafkaProducer.sendMessage(joke.value, TOPIC);
        log.info("Send joke to topic: {}", joke);
    }
}
