package org.example;

import org.example.dto.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class KafkaConsumerApplicationTest {

    @Container
    static final KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    @DynamicPropertySource
    static void overridePropertiesInternal(DynamicPropertyRegistry registry){
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Test
    public void testConsumeEvents(){
        Customer customer = new Customer(2, "test user", "test@gmail.com", "424");

        kafkaTemplate.send("topicE2E", customer);

        await().pollInterval(Duration.ofSeconds(3))
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    // Assertions to verify message consumption can go here.
                });
    }
}
