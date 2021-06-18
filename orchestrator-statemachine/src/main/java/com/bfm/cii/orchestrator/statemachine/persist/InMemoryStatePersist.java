/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jul 3, 2020
*
*/
package com.bfm.cii.orchestrator.statemachine.persist;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;

import com.bfm.cii.orchestrator.model.domain.InstructionState;
import com.bfm.cii.orchestrator.model.event.InstructionEvent;

/**
 * @author rohsingh
 *
 */
public class InMemoryStatePersist implements StateMachinePersist<InstructionState, InstructionEvent, UUID> {

    private Map<UUID, StateMachineContext<InstructionState, InstructionEvent>> storage = new HashMap<>();

    @Override
    public StateMachineContext<InstructionState, InstructionEvent> read(UUID contextObj) throws Exception {
        return storage.get(contextObj);
    }

    @Override
    public void write(StateMachineContext<InstructionState, InstructionEvent> context, UUID contextObj)
            throws Exception {
        storage.put(contextObj, context);
    }

}
