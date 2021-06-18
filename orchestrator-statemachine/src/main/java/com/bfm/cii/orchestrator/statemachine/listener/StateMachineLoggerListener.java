/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jul 6, 2020
*
*/
package com.bfm.cii.orchestrator.statemachine.listener;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateContext.Stage;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

import com.bfm.cii.orchestrator.model.domain.InstructionState;
import com.bfm.cii.orchestrator.model.event.InstructionEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * @author rohsingh
 *
 */
@Slf4j
public class StateMachineLoggerListener extends StateMachineListenerAdapter<InstructionState, InstructionEvent> {

    private final LinkedList<String> messages = new LinkedList<>();

    public List<String> getMessages() {
        return messages;
    }

    public void resetMessages() {
        messages.clear();
    }

    @Override
    public void stateContext(StateContext<InstructionState, InstructionEvent> stateContext) {
        if (stateContext.getStage() == Stage.STATE_ENTRY) {
            messages.addFirst(stateContext.getStateMachine().getId() + " enter " + stateContext.getTarget().getId());
        } else if (stateContext.getStage() == Stage.STATE_EXIT) {
            messages.addFirst(stateContext.getStateMachine().getId() + " exit " + stateContext.getSource().getId());
        } else if (stateContext.getStage() == Stage.STATEMACHINE_START) {
            messages.addLast(stateContext.getStateMachine().getId() + " started");
        } else if (stateContext.getStage() == Stage.STATEMACHINE_STOP) {
            messages.addFirst(stateContext.getStateMachine().getId() + " stopped");
        }
    }

    @Override
    public void transition(Transition<InstructionState, InstructionEvent> transition) {
        log.warn("move from:{} to:{}", ofNullableState(transition.getSource()),
                ofNullableState(transition.getTarget()));
    }

    @Override
    public void eventNotAccepted(Message<InstructionEvent> event) {
        log.error("event not accepted: {}", event);
    }

    private Object ofNullableState(State<InstructionState, InstructionEvent> s) {
        return Optional.ofNullable(s).map(State::getId).orElse(null);
    }

}
