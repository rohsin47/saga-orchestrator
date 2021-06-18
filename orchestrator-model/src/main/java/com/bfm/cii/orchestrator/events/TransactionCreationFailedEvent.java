/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jun 23, 2020
*
*/
package com.bfm.cii.orchestrator.events;

import java.util.UUID;

/**
 * @author rohsingh
 *
 */
public class TransactionCreationFailedEvent extends InstructionEvent {

    private String orderId;
    
    public TransactionCreationFailedEvent() {
        this.eventId = UUID.randomUUID().toString();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
