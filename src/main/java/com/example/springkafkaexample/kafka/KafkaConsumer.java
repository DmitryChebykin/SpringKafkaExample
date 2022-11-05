package com.example.springkafkaexample.kafka;

import com.example.springkafkaexample.service.MqHandlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    public static final String READ_TOPIC = "test-topic";

    public static final String TOPIC = "word-count-topic";

    public final List<String> messages = new ArrayList<>();

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    private final MqHandlerService mqHandlerService;

    private final KafkaProducer kafkaProducer;

    @KafkaListener(id = "1", topics = READ_TOPIC, autoStartup = "false")
    public void receive(ConsumerRecord<String, String> consumerRecord) {
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer("1");
        log.info("Received payload: '{}'", consumerRecord.toString());
        String value = consumerRecord.value();
        messages.add(value);
        Map<String, Long> wordCountMap = mqHandlerService.getWordCountMap(value);
        log.info("Word count in text: '{}'", wordCountMap);
        kafkaProducer.sendMessage("Word count in text <<<" + value + ">>>> : " + wordCountMap, TOPIC);
        listenerContainer.stop();
    }

    @Scheduled(fixedDelay = 15000, initialDelay = 30000)
    public void schedule() {
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer("1");
        listenerContainer.start();
    }
}
