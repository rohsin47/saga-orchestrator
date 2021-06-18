/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jun 16, 2020
*
*/
package com.bfm.cii.orchestrator.kafka.serde;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import com.bfm.cii.orchestrator.model.command.CreateTransactionCommand;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author rohsingh
 *
 */
public class CreateCiiCommandSerde implements Serde<CreateTransactionCommand> {
    
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public void close() {
    }

    @Override
    public Serializer<CreateTransactionCommand> serializer() {
        return new Serializer<CreateTransactionCommand>() {
            @Override
            public void configure(final Map<String, ?> map, final boolean b) {
            }

            @Override
            public void close() {
            }

            @Override
            public byte[] serialize(String topic, CreateTransactionCommand data) {
                byte[] serializedBytes = null;
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    serializedBytes = objectMapper.writeValueAsString(data).getBytes();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return serializedBytes;
            }
        };
    }

    @Override
    public Deserializer<CreateTransactionCommand> deserializer() {
        return new Deserializer<CreateTransactionCommand>() {

            @Override
            public void configure(Map<String, ?> configs, boolean isKey) {
            }

            @Override
            public CreateTransactionCommand deserialize(String topic, byte[] data) {
                ObjectMapper mapper = new ObjectMapper();
                CreateTransactionCommand event = null;
                try {
                    event = mapper.readValue(data, CreateTransactionCommand.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return event;
            }

            @Override
            public void close() {
            }
        };
    }

}
