/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jun 11, 2020
*
*/
package com.bfm.cii.orchestrator.events;

import java.util.UUID;

import com.bfm.cii.orchestrator.domain.Transaction;
import com.bfm.cii.orchestrator.events.InstructionEvent;

/**
 * @author rohsingh
 *
 */
public class TransactionCreatedEvent extends InstructionEvent {
    
    private String orderId;
    private Transaction txn;
    
    public TransactionCreatedEvent() {
        this.eventId = UUID.randomUUID().toString();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Transaction getTxn() {
        return txn;
    }

    public void setTxn(Transaction txn) {
        this.txn = txn;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TransactionCreatedEvent [orderId=");
        builder.append(orderId);
        builder.append(", cii=");
        builder.append(txn.getCiiId());
        builder.append("]");
        return builder.toString();
    }
    
   
}
