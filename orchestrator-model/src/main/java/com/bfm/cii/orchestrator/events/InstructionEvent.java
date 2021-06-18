/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jun 20, 2020
*
*/
package com.bfm.cii.orchestrator.events;

/**
 * @author rohsingh
 *
 */
public abstract class InstructionEvent {

    public String eventId;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
   
}
