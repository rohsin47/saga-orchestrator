package com.bfm.cii.orchestrator.kafka.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.bfm.cii.orchestrator.kafka.KafkaGateway;
import com.bfm.cii.orchestrator.model.event.InstructionEventInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SagaKafkaConsumer {
    
    @Autowired
    KafkaGateway kafkaGateway;
    
    @KafkaListener(topics = "${app.topic.consumer.order-replies}", 
            containerFactory = "orderRepliesKafkaListenerContainerFactory")
    public void consume(@Payload InstructionEventInfo message,
            @Header(name = KafkaHeaders.RECEIVED_MESSAGE_KEY, required = false) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts) {

        log.info("Consumed message: {}, with key : {}, from topic : {}, partition : {}",
                message.getPayload(),
                key,
                topic,
                partition);
               
        kafkaGateway.publishInstructionEvent(message.getPayload());    
    }


}
