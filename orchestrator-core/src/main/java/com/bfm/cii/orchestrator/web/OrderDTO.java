/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jun 18, 2020
*
*/
package com.bfm.cii.orchestrator.web;

import com.bfm.cii.orchestrator.domain.InstructionState;

import lombok.Data;

/**
 * @author rohsingh
 *
 */
@Data
public class OrderDTO {
    
    private String orderId;
    private InstructionState orderState;
    private String associatedSaga;
    
}
