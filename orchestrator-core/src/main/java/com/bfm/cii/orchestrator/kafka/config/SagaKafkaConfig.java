/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jun 11, 2020
*
*/
package com.bfm.cii.orchestrator.kafka.config;

import static com.bfm.cii.orchestrator.kafka.topic.TopicManager.CREATE_CII;
import static com.bfm.cii.orchestrator.kafka.topic.TopicManager.ORDER_REPLIES;
import static com.bfm.cii.orchestrator.kafka.topic.TopicManager.VALIDATE_ORDER;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import com.bfm.cii.orchestrator.commands.CreateTransactionCommand;
import com.bfm.cii.orchestrator.commands.ValidateInstructionCommand;
import com.bfm.cii.orchestrator.events.InstructionEventInfo;
import com.bfm.cii.orchestrator.kafka.KafkaGateway;

/**
 * @author rohsingh
 *
 */
@Configuration
@EnableKafka
public class SagaKafkaConfig {
    
    private static final String bootstrapServersCluster = "localhost:9092";
    
    @Bean
    public Map<String, Object> consumerConfigs(String groupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServersCluster);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, "org.apache.kafka.clients.consumer.RoundRobinAssignor");
        return props;
    }
    
    @Bean(name = "orderRepliesKafkaListenerContainerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, InstructionEventInfo>> orderRepliesKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, InstructionEventInfo> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderRepliesConsumerFactory());
        factory.setConcurrency(1);
        factory.getContainerProperties().setPollTimeout(3000);
        factory.getContainerProperties().setSyncCommits(true);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, InstructionEventInfo> orderRepliesConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs("order-replies-group"), 
                ORDER_REPLIES.keySerde().deserializer(), 
                ORDER_REPLIES.valueSerde().deserializer());
    }
    
    
    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServersCluster);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, "0");
        return props;
    }

    @Bean(name = "validateOrderProducerFactory")
    public ProducerFactory<String, ValidateInstructionCommand> validateOrderProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs(), 
                VALIDATE_ORDER.keySerde().serializer(),
                VALIDATE_ORDER.valueSerde().serializer());
    }
    
    @Bean(name = "createCiiProducerFactory")
    public ProducerFactory<String, CreateTransactionCommand> createCiiProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs(), 
                CREATE_CII.keySerde().serializer(),
                CREATE_CII.valueSerde().serializer());
    }
    
    @Bean(name = "validateOrderKafkaTemplate")
    public KafkaTemplate<String, ValidateInstructionCommand> validateOrderkafkaTemplate() {
        return new KafkaTemplate<>(validateOrderProducerFactory());
    }
    
    @Bean(name = "createCiiKafkaTemplate")
    public KafkaTemplate<String, CreateTransactionCommand> createCiiKafkaTemplate() {
        return new KafkaTemplate<>(createCiiProducerFactory());
    }
    
    @Bean
    public KafkaGateway kafkaGateway() {
        return new KafkaGateway();
    }

}
