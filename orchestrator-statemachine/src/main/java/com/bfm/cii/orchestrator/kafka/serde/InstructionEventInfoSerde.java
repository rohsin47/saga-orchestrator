/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jun 20, 2020
*
*/
package com.bfm.cii.orchestrator.kafka.serde;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import com.bfm.cii.orchestrator.model.event.InstructionEventInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author rohsingh
 * @param <T>
 * @param <T>
 * @param <T>
 *
 */
public class InstructionEventInfoSerde implements Serde<InstructionEventInfo> {
    
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public void close() {
    }

    @Override
    public Serializer<InstructionEventInfo> serializer() {
        return new Serializer<InstructionEventInfo>() {
            @Override
            public void configure(final Map<String, ?> map, final boolean b) {
            }

            @Override
            public void close() {
            }

            @Override
            public byte[] serialize(String topic, InstructionEventInfo data) {
                byte[] serializedBytes = null; 
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE); 
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
    public Deserializer<InstructionEventInfo> deserializer() {
        return new Deserializer<InstructionEventInfo>() {

            @Override
            public void configure(Map<String, ?> configs, boolean isKey) {
            }

            @Override
            public InstructionEventInfo deserialize(String topic, byte[] data) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE);  
                mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                InstructionEventInfo event = null;
                try {
                    event = mapper.readValue(data, InstructionEventInfo.class);
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
