/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jun 18, 2020
*
*/
package com.bfm.cii.orchestrator.saga;

import java.util.Set;

import org.axonframework.modelling.saga.AssociationValue;
import org.axonframework.modelling.saga.repository.SagaStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author rohsingh
 *
 */
@Service
public class SagaStoreService {
    
    @Autowired
    SagaStore<Object> orderSagaStore;
    
    public Set<String> getSaga(String orderId) {
        AssociationValue val = new AssociationValue("orderId", orderId);
        return orderSagaStore.findSagas(InstructionSagaManager.class, val);
    }

}
