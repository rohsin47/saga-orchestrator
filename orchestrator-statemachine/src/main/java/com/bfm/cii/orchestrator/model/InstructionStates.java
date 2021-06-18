/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jul 7, 2020
*
*/
package com.bfm.cii.orchestrator.model;

/**
 * @author rohsingh
 *
 */
public enum InstructionStates {
	
	INSTRUCTION_RECIEVED,
	INSTRUCTION_PROCESSING,
	INSTRUCTION_PROCESSED,
	CII_CREATING,
	CII_CREATED,
	CII_WAITING,
	CII_CREATING_SYSTEM_ERROR,
	CII_CREATING_ERROR,
	CII_CREATING_TIMEOUT;
	
}
