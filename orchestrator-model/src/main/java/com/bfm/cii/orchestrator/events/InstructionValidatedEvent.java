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
public class InstructionValidatedEvent extends InstructionEvent {
    
    private String orderId;
    private Instruction order;  
    private boolean isOrderValid;
    private boolean isTransactionTobeFailed;
    
    public InstructionValidatedEvent() {
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

    public boolean isOrderValid() {
        return isOrderValid;
    }

    public void setOrderValid(boolean isOrderValid) {
        this.isOrderValid = isOrderValid;
    }

    public boolean isTransactionTobeFailed() {
        return isTransactionTobeFailed;
    }

    public void setTransactionTobeFailed(boolean isTransactionTobeFailed) {
        this.isTransactionTobeFailed = isTransactionTobeFailed;
    }


}
