/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jun 11, 2020
*
*/
package com.bfm.cii.orchestrator.commands;

import java.util.UUID;

import com.bfm.cii.orchestrator.domain.Instruction;

/**
 * @author rohsingh
 *
 */
public class CreateTransactionCommand extends InstructionCommand<String> {
    
    private Instruction order;
    private boolean failTransaction;
    
    public CreateTransactionCommand() {
       this.commandId = UUID.randomUUID().toString();
    }

    public Instruction getOrder() {
        return order;
    }

    public void setOrder(Instruction order) {
        this.order = order;
    }

    public boolean isFailTransaction() {
        return failTransaction;
    }

    public void setFailTransaction(boolean failTransaction) {
        this.failTransaction = failTransaction;
    }

}
