/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jun 20, 2020
*
*/
package com.bfm.cii.orchestrator.events;

import java.util.UUID;

import com.bfm.cii.orchestrator.domain.Instruction;
import com.bfm.cii.orchestrator.events.InstructionEvent;

/**
 * @author rohsingh
 *
 */
public class InstructionCreatedEvent extends InstructionEvent {
    
    private String orderId;
    private Instruction order;
    
    // hack for -ve cases
    private boolean failValidation;
    private boolean failTransaction;

    public InstructionCreatedEvent() {
        this.eventId = UUID.randomUUID().toString();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Instruction getOrder() {
        return order;
    }

    public void setOrder(Instruction order) {
        this.order = order;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("InstructionCreatedEvent [orderId=");
        builder.append(orderId);
        builder.append(", order=");
        builder.append(order);
        builder.append("]");
        return builder.toString();
    }

    public boolean isFailValidation() {
        return failValidation;
    }

    public void setFailValidation(boolean failValidation) {
        this.failValidation = failValidation;
    }

    public boolean isFailTransaction() {
        return failTransaction;
    }

    public void setFailTransaction(boolean failTransaction) {
        this.failTransaction = failTransaction;
    }


}
