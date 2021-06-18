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
public class ValidateInstructionCommand extends InstructionCommand<String> {
    
    private Instruction order;
    
    // hack for -ve cases
    private boolean failValidation;
    private boolean failTransaction;
    
    public ValidateInstructionCommand() {
        this.commandId = UUID.randomUUID().toString();
    }

    public Instruction getOrder() {
        return order;
    }

    public void setOrder(Instruction order) {
        this.order = order;
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
