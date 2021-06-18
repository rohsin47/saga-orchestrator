/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jun 16, 2020
*
*/
package com.bfm.cii.orchestrator.commands;

import java.util.UUID;

import com.bfm.cii.orchestrator.domain.InstructionState;

/**
 * @author rohsingh
 *
 */
public class UpdateOrderStateCommand extends InstructionCommand<String> {
    
    private String orderId;
    private InstructionState orderState;

    public UpdateOrderStateCommand() {
        this.commandId = UUID.randomUUID().toString();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public InstructionState getOrderState() {
        return orderState;
    }

    public void setOrderState(InstructionState orderState) {
        this.orderState = orderState;
    }

}
