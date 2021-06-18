/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jun 21, 2020
*
*/
package com.bfm.cii.orchestrator.saga;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import com.bfm.cii.orchestrator.commands.CreateTransactionCommand;
import com.bfm.cii.orchestrator.commands.UpdateOrderStateCommand;
import com.bfm.cii.orchestrator.commands.ValidateInstructionCommand;
import com.bfm.cii.orchestrator.domain.InstructionState;
import com.bfm.cii.orchestrator.domain.TransactionState;
import com.bfm.cii.orchestrator.events.InstructionCreatedEvent;
import com.bfm.cii.orchestrator.events.InstructionValidatedEvent;
import com.bfm.cii.orchestrator.events.TransactionCreatedEvent;
import com.bfm.cii.orchestrator.events.TransactionCreationFailedEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * @author rohsingh
 *
 */
@Slf4j
@Saga(sagaStore = "orderSagaStore")
public class InstructionSagaManager {
    
    @Autowired
    CommandBus commandBus;

    String orderId;
    
    boolean isOrderValid;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handleInstructionCreated(InstructionCreatedEvent event) {
        log.info("Received Instruction Created event : {} for order : {}", 
                event.getEventId(), 
                event.getOrder().getInstructionId());

        this.orderId = event.getOrder().getInstructionId();

        log.info("Started Saga for : {}", orderId);
        
        SagaLifecycle.describeCurrentScope().scopeDescription();
        
        //the below 1 and 2 steps must be atomic.
        //Add transactional boundary        
        
        // 1. storing state
        updateStateStore(orderId, InstructionState.INSTRUCTION_VALIDATING);

        // 2. publish commands
        log.info("Sending the validate instruction command for : {}", orderId);
        // send to order validation service
        ValidateInstructionCommand validateOrder = new ValidateInstructionCommand();
        validateOrder.setOrder(event.getOrder());    
        validateOrder.setFailValidation(event.isFailValidation());
        validateOrder.setFailTransaction(event.isFailTransaction());
        commandBus.dispatch(GenericCommandMessage.asCommandMessage(validateOrder));
        log.info("Sent successful to Kafka");      
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handleInstructionValidation(InstructionValidatedEvent event) {
        log.info("Received Instruction Validation Success event : {} for order : {}", 
                event.getEventId(),
                event.getOrder().getInstructionId());

        this.orderId = event.getOrder().getInstructionId();
        this.isOrderValid = event.isOrderValid();
        
        SagaLifecycle.describeCurrentScope().scopeDescription();
        
        if(isOrderValid) {
        
            updateStateStore(orderId, InstructionState.TRANSACTION_CREATING);
    
            log.info("Sending the create transaction command for : {}", orderId);
            // create cii
            CreateTransactionCommand createCii = new CreateTransactionCommand();
            createCii.setOrder(event.getOrder());
            createCii.setFailTransaction(event.isTransactionTobeFailed());
            commandBus.dispatch(GenericCommandMessage.asCommandMessage(createCii));
            log.info("Sent successful to Kafka");
            
        } else {            
            updateStateStore(orderId, InstructionState.INSTRUCTION_REJECTED);
            
            SagaLifecycle.end();
            
            log.info("Ended Saga for : {}", orderId);   
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handleTxnCreated(TransactionCreatedEvent event) {
        log.info("Received Txn Created event : {} for order : {}", 
                event.getEventId(),
                event.getOrderId());

        this.orderId = event.getOrderId();
        
        SagaLifecycle.describeCurrentScope().scopeDescription();
        
        SagaLifecycle.associateWith("ciiId", event.getTxn().getCiiId());

        if(event.getTxn() != null && 
                TransactionState.OPEN == event.getTxn().getTxnState()) {
            
            updateStateStore(orderId, InstructionState.INSTRUCTION_APPROVED);
            
            SagaLifecycle.end();
            log.info("Ended Saga for : {}", orderId);
        }
    }
    
    @SagaEventHandler(associationProperty = "orderId")
    public void handleTxnCreationFailure(TransactionCreationFailedEvent event) {
        log.info("Received Txn Created event : {} for order : {}", 
                event.getEventId(),
                event.getOrderId());

        this.orderId = event.getOrderId();
        
        SagaLifecycle.describeCurrentScope().scopeDescription();
           
        updateStateStore(orderId, InstructionState.INSTRUCTION_REJECTED);
            
        SagaLifecycle.end();
        
        log.info("Ended Saga for : {}", orderId);
    }
    
    private void updateStateStore(String orderId, InstructionState orderState) {
        log.info("Storing the order state : {} - {}", orderId, orderState);
        // send to state store
        UpdateOrderStateCommand orderStateCommand = new UpdateOrderStateCommand();
        orderStateCommand.setOrderId(orderId);
        orderStateCommand.setOrderState(orderState);       
        commandBus.dispatch(GenericCommandMessage.asCommandMessage(orderStateCommand));
        log.info("Store successful");
    }

}
