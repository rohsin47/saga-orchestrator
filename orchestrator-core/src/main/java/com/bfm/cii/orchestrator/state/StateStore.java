/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jun 16, 2020
*
*/
package com.bfm.cii.orchestrator.state;

import java.util.List;

import com.bfm.cii.orchestrator.domain.InstructionState;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;

/**
 * @author rohsingh
 *
 */
public class StateStore {
    
    private final ListMultimap<String, InstructionState> orderByState = 
            Multimaps.synchronizedListMultimap(LinkedListMultimap.create());

    public void insert(String orderId, InstructionState orderState) {
        orderByState.put(orderId, orderState);
    }
    
    public void update(String orderId, InstructionState orderState) {
        orderByState.put(orderId, orderState);
    }
    
    public List<InstructionState> load(String orderId) {
        return orderByState.get(orderId);
    }

    public void delete(String orderId) {
        if(orderByState.containsKey(orderId)) {
            orderByState.removeAll(orderId);
        }
    }
    
    public void delete(String orderId, InstructionState orderState) {
        if(orderByState.containsKey(orderId)) {
            orderByState.remove(orderId, orderState);
        }
    }

    public int size() {
        return orderByState.size();
    }
    
    public ListMultimap<String, InstructionState> allOrders() {
        return orderByState;
    }

}
