/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jun 11, 2020
*
*/
package com.bfm.cii.orchestrator.domain;

/**
 * @author rohsingh
 *
 */
public enum InstructionState {
    
    INSTRUCTION_CREATED,
    INSTRUCTION_VALIDATING,
    TRANSACTION_CREATING,
    INSTRUCTION_REJECTED,
    INSTRUCTION_APPROVED;

}
