package com.bfm.cii.validate;

import javax.annotation.PostConstruct;

import org.apache.kafka.streams.KafkaStreams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author rohsingh
 *
 */
@Component
public class InstructionKafkaTopologyProcessor {

    @Autowired
    InstructionKafkaTopologyBuilder orderKafkaTopologyBuilder;

    @PostConstruct
    public void processToplogy() {
        final KafkaStreams ciiEventstreams = orderKafkaTopologyBuilder
                .buildOrderValidationStream("order-validations");
       
        orderKafkaTopologyBuilder.startTopology(ciiEventstreams);
    }

}
