/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jun 15, 2020
*
*/
package com.bfm.cii.orchestrator.axon.gateway;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.GenericEventMessage;
import org.springframework.beans.factory.annotation.Autowired;

import com.bfm.cii.orchestrator.commands.CreateTransactionCommand;
import com.bfm.cii.orchestrator.commands.UpdateOrderStateCommand;
import com.bfm.cii.orchestrator.commands.ValidateInstructionCommand;
import com.bfm.cii.orchestrator.events.InstructionEvent;
import com.bfm.cii.orchestrator.kafka.KafkaGateway;
import com.bfm.cii.orchestrator.state.StateStoreService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author rohsingh
 *
 */
@Slf4j
public class AxonGateway {
    
    @Autowired
    KafkaGateway kafkaGateway;
    
    @Autowired
    EventBus eventBus;
    
    @Autowired
    StateStoreService storeService;
    
    @CommandHandler
    public void handleOrderValidation(ValidateInstructionCommand command) {
        log.info("Received validate instruction command, sending to kafka for : {}", command.getOrder().getInstructionId());
        kafkaGateway.sendValidateOrder(command);
    }
    
    @CommandHandler
    public void handleCiiCreation(CreateTransactionCommand command) {
        log.info("Received create txn command, sending to kafka for : {}", command.getOrder().getInstructionId());
        kafkaGateway.sendCreateCii(command);
    }
    
    @CommandHandler
    public void handleOrderState(UpdateOrderStateCommand command) {
       log.info("Doing store for : {}", command.getOrderId());
       storeService.updateStore(command.getOrderId(), command.getOrderState());
    }
     
    public void publishInstructionEvt(InstructionEvent message) {
        log.info("Publishing event to saga for : {}", message.getEventId());
        eventBus.publish(GenericEventMessage.asEventMessage(message));
    }

}
