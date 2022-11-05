package com.example.springkafkaexample;

import com.example.springkafkaexample.kafka.KafkaConsumer;
import com.example.springkafkaexample.kafka.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.io.File;
import java.time.Duration;
import java.util.UUID;

import static com.example.springkafkaexample.service.MessageGeneratorService.TOPIC;

@Slf4j
@TestPropertySource(properties = "app.scheduling.enable=false")
@Testcontainers
@SpringBootTest(classes = SpringKafkaExampleApplication.class)
class SpringKafkaTests {

    private static final File KAFKA_COMPOSE_FILE = new File("src/test/resources/docker/docker-compose.yml");

    private static final String KAFKA_SERVICE = "kafka";

    private static final int SSL_PORT = 9093;

    private static final int PORT = 9092;

    @Container
    public DockerComposeContainer<?> container =
            new DockerComposeContainer<>(KAFKA_COMPOSE_FILE)
                    .withOptions("--compatibility")
                    .withExposedService("zookeeper", 1, 2181, Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)))
                    .withExposedService(KAFKA_SERVICE, SSL_PORT, Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)))
                    .withExposedService(KAFKA_SERVICE, PORT, Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)))
                    .withLocalCompose(true);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private KafkaConsumer kafkaConsumer;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    private static String generateSampleMessage() {
        return UUID.randomUUID().toString();
    }

    @BeforeEach
    public void setUp() {
        kafkaListenerEndpointRegistry.getListenerContainer("1").start();
    }

    @Test
    void givenSslIsConfigured_whenProducerSendsMessageOverSsl_thenConsumerReceivesOverSsl() {
        String message = generateSampleMessage();
        kafkaProducer.sendMessage(message, TOPIC);

        Awaitility.await().atMost(Duration.ofMinutes(2))
                .untilAsserted(() -> Assertions.assertThat(kafkaConsumer.messages).containsExactly(message));
    }
}
