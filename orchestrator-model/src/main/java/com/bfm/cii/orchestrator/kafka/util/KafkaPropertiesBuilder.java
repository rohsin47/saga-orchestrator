/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jun 12, 2020
*
*/
package com.bfm.cii.orchestrator.kafka.util;

import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;

/**
 * @author rohsingh
 *
 */
public class KafkaPropertiesBuilder {

    private final Properties properties = new Properties();

    public Object get(String key) {
        return properties.get(key);
    }

    public Properties build() {
        return this.properties;
    }
    public KafkaPropertiesBuilder withProperty(String key, Object value) {
        this.properties.put(key, value);
        return this;
    }

    public KafkaPropertiesBuilder withProperties(Properties properties) {
        properties.forEach(this.properties::put);
        return this;
    }

    public KafkaPropertiesBuilder withProperties(Map<String, Object> properties) {
        properties.forEach(this.properties::put);
        return this;
    }

    public KafkaPropertiesBuilder withBootstrapServers(String bootstrapServers) {
        return this.withProperty(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    }

    public KafkaPropertiesBuilder withDefaultConsumerProps() {
        return this.withProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true)
                .withProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000)
                .withProperty(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed")
                .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    }

    public KafkaPropertiesBuilder withDefaultProducerProps() {
        return this.withProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true)
                .withProperty(ProducerConfig.RETRIES_CONFIG, 3)
                .withProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy")
                .withProperty(ProducerConfig.ACKS_CONFIG, "all");
    }

    public KafkaPropertiesBuilder withDefaultStreamProps() {
        return this.withProperty(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE)
                .withProperty(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka-streams")
                .withProperty(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, LogAndContinueExceptionHandler.class)
                .withProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy")
                .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    }
    
    public KafkaPropertiesBuilder withSasl() {
        return this.withProperty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT")
                .withProperty(SaslConfigs.SASL_MECHANISM, "PLAIN")
                .withProperty(SaslConfigs.SASL_JAAS_CONFIG, "com.bfm.kafka.security.login.OneWayChallengeModule required;");
    }

}
