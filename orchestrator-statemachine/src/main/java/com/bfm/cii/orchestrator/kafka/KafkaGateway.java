/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jun 15, 2020
*
*/
package com.bfm.cii.orchestrator.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;

import com.bfm.cii.orchestrator.kafka.producer.SagaKafkaProducer;
import com.bfm.cii.orchestrator.model.command.CreateTransactionCommand;
import com.bfm.cii.orchestrator.model.command.ValidateInstructionCommand;
import com.bfm.cii.orchestrator.model.domain.InstructionState;
import com.bfm.cii.orchestrator.model.event.InstructionEvent;

/**
 * @author rohsingh
 *
 */
public class KafkaGateway {
    
    @Autowired
    SagaKafkaProducer producer;
    
    @Autowired
    StateMachine<InstructionState, InstructionEvent> stateMachine;
    
    public void sendValidateOrder(ValidateInstructionCommand command) {
        producer.sendValidateOrderCommand(command);
    }
    
    public void sendCreateCii(CreateTransactionCommand command) {
        producer.sendCreateCiiCommand(command);
    }

    public void publishInstructionEvent(InstructionEvent payload) {
        Message<InstructionEvent> message = MessageBuilder
                .withPayload(payload)
                .setHeader("event.id", payload.getEventId())
                .build();
        stateMachine.sendEvent(message);     
    }
    
}
