package org.example.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.dto.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageListener {

    Logger logger = LoggerFactory.getLogger(KafkaMessageListener.class);

    @KafkaListener(topics = "topicE2E",groupId = "groupE2E")
    public void consumeEvents(ConsumerRecord<String,Object> record){
        Object message = record.value();
        if (message instanceof String){
            logger.info("consumer consume the events {}",message);
        } else if (message instanceof Customer){
            Customer customer = (Customer) message;
            logger.info("consumer consume the events {}",customer.toString());
        } else{
            logger.warn("Unknown message type: ",message);
        }
    }
}
