/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jul 7, 2020
*
*/
package com.bfm.cii.orchestrator.statemachine;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.bfm.cii.enums.CIIActivity;
import com.bfm.cii.enums.CiiInstructionTypeDecode;
import com.bfm.cii.hibernate.Cii;
import com.bfm.cii.orchestrator.model.InstructionEventCarrier;
import com.bfm.cii.orchestrator.model.InstructionEvents;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author rohsingh
 *
 */
public class KafkaPayloadIT {
	
	public static void main(String[] args) {		
		Cii cii = new Cii();
		cii.setCiiId(12345L);
		cii.setActivity(CIIActivity.REKEY);
		cii.setInstructionType(CiiInstructionTypeDecode.CLIENT_INSTRUCTION);
		
		List<Cii> data = new ArrayList<>();
		data.add(cii);
		
		InstructionEventCarrier<List<Cii>> carrier = 
				InstructionEventCarrier.<List<Cii>>builder()
				.key(UUID.randomUUID().toString())
				.event(InstructionEvents.INSTRUCTION_CREATED)
				.payload(data)
				.build();
		
		 System.out.println(carrier);
		
		//serialization
		byte[] serializedBytes = null; 
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            serializedBytes = objectMapper.writeValueAsString(carrier).getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // deserialization
        ObjectMapper mapper = new ObjectMapper();
        InstructionEventCarrier<Cii> event = null; 
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        TypeReference<InstructionEventCarrier<List<Cii>>> type = new TypeReference<InstructionEventCarrier<List<Cii>>>() {};
        try {
        	 event = mapper.readValue(serializedBytes, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println(event);
	}

}
