/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jun 16, 2020
*
*/
package com.bfm.cii.orchestrator.kafka.topic;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

import com.bfm.cii.orchestrator.kafka.serde.CreateCiiCommandSerde;
import com.bfm.cii.orchestrator.kafka.serde.InstructionEventInfoSerde;
import com.bfm.cii.orchestrator.kafka.serde.ValidateOrderCommandSerde;
import com.bfm.cii.orchestrator.model.command.CreateTransactionCommand;
import com.bfm.cii.orchestrator.model.command.ValidateInstructionCommand;
import com.bfm.cii.orchestrator.model.event.InstructionEventInfo;

/**
 * @author rohsingh
 *
 */
public class TopicManager {

    public static final Map<String, Topic<?, ?>> ALL = new HashMap<>();

    public static Topic<String, ValidateInstructionCommand> VALIDATE_ORDER;
    public static Topic<String, CreateTransactionCommand> CREATE_CII;
    public static Topic<String, InstructionEventInfo> ORDER_REPLIES;

    public static Serde<ValidateInstructionCommand> validateOrderserde = new ValidateOrderCommandSerde();
    public static Serde<CreateTransactionCommand> createCiiSerde = new CreateCiiCommandSerde();
    public static Serde<InstructionEventInfo> instructionInfoSerde = new InstructionEventInfoSerde();

    static {
        createTopics();
    }

    private static void createTopics() {
        VALIDATE_ORDER = new Topic<>("validate-orders", Serdes.String(), validateOrderserde);
        CREATE_CII = new Topic<>("create-cii", Serdes.String(), createCiiSerde);
        ORDER_REPLIES = new Topic<>("order-replies", Serdes.String(), instructionInfoSerde);
    }
}
