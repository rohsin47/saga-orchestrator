/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jun 16, 2020
*
*/
package com.bfm.cii.validate;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.stereotype.Component;

import com.bfm.cii.orchestrator.domain.Instruction;
import com.bfm.cii.orchestrator.domain.InstructionState;
import com.bfm.cii.orchestrator.events.InstructionEventInfo;
import com.bfm.cii.orchestrator.events.InstructionValidatedEvent;

import static com.bfm.cii.orchestrator.kafka.topic.TopicManager.VALIDATE_ORDER;
import static com.bfm.cii.orchestrator.kafka.topic.TopicManager.ORDER_REPLIES;

/**
 * @author rohsingh
 *
 */
@Component
public class InstructionKafkaTopologyBuilder {
       
    public void startTopology(final KafkaStreams kafkaStreams) {
        // start processing
        kafkaStreams.start();
        // Add shutdown hook to respond to SIGTERM and gracefully close Kafka Streams
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
    }

    public KafkaStreams buildOrderValidationStream(final String applicationId) {  
        // stream configuration
        final Properties streamsConfiguration = new Properties();
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        streamsConfiguration.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        // Set to earliest so we don't miss any data that arrived in the topics before the process started
        streamsConfiguration.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // stream builder
        final StreamsBuilder builder = new StreamsBuilder();
        // consume events
         builder
         .stream(VALIDATE_ORDER.name(), Consumed.with(VALIDATE_ORDER.keySerde(), VALIDATE_ORDER.valueSerde()))
         .filter((key, val) -> validateOrder(val.getOrder()))
         .map((key, val) -> {
             InstructionValidatedEvent event = new InstructionValidatedEvent();
             event.setOrder(val.getOrder());
             event.setOrderId(val.getOrder().getInstructionId());
             event.setOrderValid(!val.isFailValidation());
             event.setTransactionTobeFailed(val.isFailTransaction());
             InstructionEventInfo eventInfo = new InstructionEventInfo(event);
             return KeyValue.pair(event.getOrderId(), eventInfo);
         })
         .peek((k, v) -> {
             try {
                 TimeUnit.SECONDS.sleep(3);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         })
         .to(ORDER_REPLIES.name(), Produced.with(ORDER_REPLIES.keySerde(), ORDER_REPLIES.valueSerde()));
           
        // start streaming events
        return new KafkaStreams(builder.build(), streamsConfiguration);
    }
    
    private boolean validateOrder(Instruction ord) {
        return InstructionState.INSTRUCTION_CREATED == ord.getInstructionState() &&
                ord.getInstructionPortfolioCode() != null && 
                ord.getInstructionAsset() != null;
    }
    

}
