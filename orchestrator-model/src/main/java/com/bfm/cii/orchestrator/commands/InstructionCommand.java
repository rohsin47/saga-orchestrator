/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jun 15, 2020
*
*/
package com.bfm.cii.orchestrator.commands;

/**
 * @author rohsingh
 *
 */
public class InstructionCommand<T> {
    
    T commandId;

    public T getCommandId() {
        return commandId;
    }

    public void setCommandId(T commandId) {
        this.commandId = commandId;
    }

}
