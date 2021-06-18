/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jul 7, 2020
*
*/
package com.bfm.cii.orchestrator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author rohsingh
 * @param <E>
 * @param <P>
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstructionEventCarrier<P> {
	
	String key;
	
	P payload;
	
	InstructionEvents event;
	
}
