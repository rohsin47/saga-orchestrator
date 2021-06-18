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
public enum TransactionState {
    
    OPEN,
    PENDING_AUTH,
    AUTHORIZED,
    COMPLETED,
    REJECTED,
    CANCELLED;

}
