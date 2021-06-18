/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jun 15, 2020
*
*/
package com.bfm.cii.orchestrator.axon.config;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.commandhandling.gateway.IntervalRetryScheduler;
import org.axonframework.commandhandling.gateway.RetryScheduler;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.modelling.saga.repository.SagaStore;
import org.axonframework.modelling.saga.repository.inmemory.InMemorySagaStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bfm.cii.orchestrator.axon.gateway.AxonGateway;


/**
 * @author rohsingh
 *
 */
@Configuration
public class AxonSagaConfig {
    
    @Bean
    public CommandBus commandBus() {
        return new SimpleCommandBus.Builder().build();
    }
    
    @Bean
    public RetryScheduler retryScheduler() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        return new IntervalRetryScheduler.Builder()
                .retryExecutor(scheduledExecutorService)
                .maxRetryCount(100)
                .retryInterval(1000)
                .build();
    }
    
    @Bean
    public CommandGateway commandGateway(CommandBus commandBus, RetryScheduler retryScheduler) {       
        return new DefaultCommandGateway.Builder()
                .commandBus(commandBus)
                .retryScheduler(retryScheduler)
                .build();
    }
    
    @Bean
    public EventBus eventBus() {
        return new SimpleEventBus.Builder().build();
    }
    
    @Bean
    public AxonGateway axonGateway() {
        return new AxonGateway();
    }

    @Bean
    public SagaStore<Object> orderSagaStore() {
        return new InMemorySagaStore();
    }
 
}
