/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jun 25, 2020
*
*/
package com.bfm.cii.orchestrator.domain;

/**
 * @author rohsingh
 *
 */
public class Transaction {
    
    Long ciiId;
    TransactionState txnState;
    
    public Long getCiiId() {
        return ciiId;
    }
    public void setCiiId(Long ciiId) {
        this.ciiId = ciiId;
    }
    public TransactionState getTxnState() {
        return txnState;
    }
    public void setTxnState(TransactionState txnState) {
        this.txnState = txnState;
    }

}
