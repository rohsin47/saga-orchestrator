/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jun 11, 2020
*
*/
package com.bfm.cii.orchestrator.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.bfm.cii.orchestrator.commands.CreateTransactionCommand;
import com.bfm.cii.orchestrator.commands.ValidateInstructionCommand;

import static com.bfm.cii.orchestrator.kafka.topic.TopicManager.VALIDATE_ORDER;
import static com.bfm.cii.orchestrator.kafka.topic.TopicManager.CREATE_CII;

import lombok.extern.slf4j.Slf4j;

/**
 * @author rohsingh
 * @param <T>
 *
 */
@Slf4j
@Component
public class SagaKafkaProducer {

    @Autowired
    KafkaTemplate<String, ValidateInstructionCommand> validateOrderkafkaTemplate;

    @Autowired
    KafkaTemplate<String, CreateTransactionCommand> createCiiKafkaTemplate;

    public void sendValidateOrderCommand(ValidateInstructionCommand command) {
        log.info("Sending message : {}", command.getOrder().getInstructionId());
        ListenableFuture<SendResult<String, ValidateInstructionCommand>> future = validateOrderkafkaTemplate
                .send(VALIDATE_ORDER.name(), command);
        future.addCallback(new ListenableFutureCallback<SendResult<String, ValidateInstructionCommand>>() {
            @Override
            public void onSuccess(SendResult<String, ValidateInstructionCommand> result) {
                log.info("Posted offset: {}", result.getRecordMetadata().offset());
            }
            @Override
            public void onFailure(Throwable ex) {
                log.info("Posting failure due to : {}", ex);
            }
        });
    }

    public void sendCreateCiiCommand(CreateTransactionCommand command) {
        log.info("Sending message : {}", command.getOrder().getInstructionId());
        ListenableFuture<SendResult<String, CreateTransactionCommand>> future = createCiiKafkaTemplate.send(CREATE_CII.name(),
                command);
        future.addCallback(new ListenableFutureCallback<SendResult<String, CreateTransactionCommand>>() {
            @Override
            public void onSuccess(SendResult<String, CreateTransactionCommand> result) {
                log.info("Posted offset : {}", result.getRecordMetadata().offset());
            }
            @Override
            public void onFailure(Throwable ex) {
                log.info("Posting failure due to : {}", ex);
            }
        });
    }

}
