/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jun 18, 2020
*
*/
package com.bfm.cii.orchestrator.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bfm.cii.orchestrator.domain.InstructionState;
import com.bfm.cii.orchestrator.saga.SagaStoreService;
import com.bfm.cii.orchestrator.state.StateStoreService;
import com.google.common.collect.ListMultimap;

import lombok.extern.slf4j.Slf4j;

/**
 * @author rohsingh
 *
 */
@RestController
@RequestMapping("/orders")
@Slf4j
public class StateStoreController {
    
    @Autowired
    private StateStoreService service;
    
    @Autowired
    private SagaStoreService sagaStoreService;
    
    @GetMapping(value="/", produces = MediaType.TEXT_PLAIN_VALUE)
    public String index() {
        return "This is orders state store page";
    }
    
    @GetMapping("/all")
    public ResponseEntity<?> getOrders(){
        ListMultimap<String, InstructionState>  ordersMap = service.getAll();
        List<OrderDTO> orders = new ArrayList<>();
        ordersMap.entries().stream().forEach(ord -> {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setOrderId(ord.getKey());
            orderDTO.setOrderState(ord.getValue());
            Set<String> sagaIds = sagaStoreService.getSaga(ord.getKey());
            log.info("Saga for order - {} : {}", ord.getKey(), sagaIds.toString());
            orderDTO.setAssociatedSaga(sagaIds.toString());
            orders.add(orderDTO);
        });
        Map<String, List<OrderDTO>> mapBySaga = orders.stream().collect(Collectors.groupingBy(OrderDTO::getOrderId));
        return ResponseEntity.ok(mapBySaga);
    }
    
    

}
