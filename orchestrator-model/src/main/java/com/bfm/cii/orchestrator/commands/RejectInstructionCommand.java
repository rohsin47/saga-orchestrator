/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jun 23, 2020
*
*/
package com.bfm.cii.orchestrator.commands;

import java.util.UUID;

import com.bfm.cii.orchestrator.domain.Instruction;

/**
 * @author rohsingh
 *
 */
public class RejectInstructionCommand extends InstructionCommand<String> {

    private Instruction order;
    
    public RejectInstructionCommand() {
        this.commandId = UUID.randomUUID().toString();
    }

    public Instruction getOrder() {
        return order;
    }

    public void setOrder(Instruction order) {
        this.order = order;
    }
}
