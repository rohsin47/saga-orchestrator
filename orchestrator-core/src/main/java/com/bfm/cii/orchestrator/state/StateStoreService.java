/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jun 16, 2020
*
*/
package com.bfm.cii.orchestrator.state;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bfm.cii.orchestrator.domain.InstructionState;
import com.google.common.collect.ListMultimap;

/**
 * @author rohsingh
 *
 */
@Service
public class StateStoreService {
    
    StateStore store;
    
    public StateStoreService() {
        this.store = new StateStore();
    }
    
    public void updateStore(String orderId, InstructionState orderState) {
        store.update(orderId, orderState);
    }
    
    public List<InstructionState> loadStore(String orderId) {
        return store.load(orderId);
    }
    
    public ListMultimap<String, InstructionState>  getAll() {
        return store.allOrders();
    }

}
