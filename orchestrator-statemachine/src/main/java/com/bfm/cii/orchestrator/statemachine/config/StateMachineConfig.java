/*
 * Copyright (C) 2020 BlackRock.
 *
 * Created on Jul 3, 2020
 *
 */
package com.bfm.cii.orchestrator.statemachine.config;

import java.util.EnumSet;
import java.util.UUID;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.target.CommonsPool2TargetSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;

import com.bfm.cii.orchestrator.model.InstructionStates;
import com.bfm.cii.orchestrator.model.InstructionEvents;
import com.bfm.cii.orchestrator.statemachine.listener.StateMachineLoggerListener;
import com.bfm.cii.orchestrator.statemachine.persist.InMemoryStatePersist;

/**
 * @author rohsingh
 *
 */
@Configuration
public class StateMachineConfig {

    @Bean
    public StateMachineLoggerListener stateMachineLoggerListener() {
        return new StateMachineLoggerListener();
    }
    
    @Bean
    public InMemoryStatePersist inMemoryStatePersist() {
        return new InMemoryStatePersist();
    }
    
    @Bean
    public StateMachinePersister<InstructionStates, InstructionEvents, UUID> persister(
            StateMachinePersist<InstructionStates, InstructionEvents, UUID> defaultPersist) {
        return new DefaultStateMachinePersister<>(defaultPersist);
    }

    @Configuration
    @EnableStateMachine
    public class Config extends StateMachineConfigurerAdapter<InstructionStates, InstructionEvents>{

        @Bean
        @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
        public ProxyFactoryBean stateMachine() {
            ProxyFactoryBean pfb = new ProxyFactoryBean();
            pfb.setTargetSource(poolTargetSource());
            return pfb;
        }

        @Bean
        public CommonsPool2TargetSource poolTargetSource() {
            CommonsPool2TargetSource pool = new CommonsPool2TargetSource();
            pool.setMaxSize(3);
            pool.setTargetBeanName("stateMachineTarget");
            return pool;
        }

        @Bean(name = "stateMachineTarget")
        @Scope(scopeName="prototype")
        public StateMachine<InstructionStates, InstructionEvents> stateMachineTarget() throws Exception {
            Builder<InstructionStates, InstructionEvents> builder = StateMachineBuilder.builder();

            builder.configureConfiguration()
                .withConfiguration()
                .autoStartup(true)
                .machineId("instruction-manager")
                .listener(stateMachineLoggerListener);

            builder.configureStates()
                .withStates()
                .initial(InstructionStates.INSTRUCTION_RECIEVED)
                .states(EnumSet.allOf(InstructionStates.class));

            builder.configureTransitions()
                .withExternal()
                .source(InstructionStates.INSTRUCTION_RECIEVED)
                .target(InstructionStates.INSTRUCTION_VALIDATING)
                .event(arg0)
                .guard(checkInstructionTransition())
                .action(validateInstruction())
            .and()
                .withExternal()
                .source(InstructionState.INSTRUCTION_VALIDATING)
                .target(InstructionState.TRANSACTION_CREATING)
                .event(InstructionEventType.INSTRUCTION_VALIDATED)
                .guard(checkInstructionTransition())
                .action(createTransaction())
            .and()
                .withExternal()
                .source(InstructionState.TRANSACTION_CREATING)
                .target(InstructionState.INSTRUCTION_APPROVED)
                .event(InstructionEventType.TRANSACTION_CREATED)
                .guard(checkInstructionTransition());

            return builder.build();
        }

        @Bean
        public Guard<InstructionStates, InstructionEvents> checkInstructionTransition() {
            return (StateContext<InstructionStates, InstructionEvents> context) -> {
                return true;
            };
        }

        @Bean
        public Action<InstructionState, InstructionEventType> validateInstruction() {
            return (StateContext<InstructionState, InstructionEventType> context) -> {
                String variable = context.getTarget().getId().toString();
                Integer count = context.getExtendedState().get(variable, Integer.class);
                if (count == null) {
                    context.getExtendedState().getVariables().put(variable, 1);
                } else {
                    context.getExtendedState().getVariables().put(variable, (count + 1));
                }
            };
        }

        @Bean
        public Action<InstructionState, InstructionEventType> createTransaction() {
            return (StateContext<InstructionState, InstructionEventType> context) -> {
                String variable = context.getTarget().getId().toString();
                Integer count = context.getExtendedState().get(variable, Integer.class);
                if (count == null) {
                    context.getExtendedState().getVariables().put(variable, 1);
                } else {
                    context.getExtendedState().getVariables().put(variable, (count + 1));
                }
            };
        }
    }
}
