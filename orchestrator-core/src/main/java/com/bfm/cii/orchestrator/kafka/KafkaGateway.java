/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jun 15, 2020
*
*/
package com.bfm.cii.orchestrator.kafka;

import org.springframework.beans.factory.annotation.Autowired;

import com.bfm.cii.orchestrator.axon.gateway.AxonGateway;
import com.bfm.cii.orchestrator.commands.CreateTransactionCommand;
import com.bfm.cii.orchestrator.commands.ValidateInstructionCommand;
import com.bfm.cii.orchestrator.events.InstructionEvent;
import com.bfm.cii.orchestrator.kafka.producer.SagaKafkaProducer;

/**
 * @author rohsingh
 *
 */
public class KafkaGateway {
    
    @Autowired
    SagaKafkaProducer producer;
    
    @Autowired
    AxonGateway axonGateway;
    
    public void sendValidateOrder(ValidateInstructionCommand command) {
        producer.sendValidateOrderCommand(command);
    }
    
    public void sendCreateCii(CreateTransactionCommand command) {
        producer.sendCreateCiiCommand(command);
    }
    
    public void publishInstructionEvent(InstructionEvent message) {
        axonGateway.publishInstructionEvt(message);
    }
    
}
