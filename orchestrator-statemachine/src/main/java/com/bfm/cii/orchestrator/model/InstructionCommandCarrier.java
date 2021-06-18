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
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstructionCommandCarrier<P> {

	String key;
	
	P payload;
	
	InstructionCommands command;
}
