/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jun 20, 2020
*
*/
package com.bfm.cii.orchestrator.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author rohsingh
 *
 */
public class InstructionEventInfo {
    
    private InstructionEvent payload;
    
    @JsonCreator
    public InstructionEventInfo(@JsonProperty("payload") InstructionEvent payload) {
        this.payload = payload;
    }
    
    public InstructionEvent getPayload() {
        return payload;
    }

    public void setPayload(InstructionEvent payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("InstructionEventInfo [payload=");
        builder.append(payload);
        builder.append("]");
        return builder.toString();
    }

     
}
