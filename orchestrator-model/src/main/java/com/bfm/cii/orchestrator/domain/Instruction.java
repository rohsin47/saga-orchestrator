/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jun 11, 2020
*
*/
package com.bfm.cii.orchestrator.domain;

import java.math.BigDecimal;

import com.bfm.lib.comstp.api.bms.instruction.TransactionType;

/**
 * @author rohsingh
 *
 */
public class Instruction {

    private String instructionId;
    private InstructionState instructionState;
    private Integer instructionPortfolioCode;
    private String instructionAsset;
    private BigDecimal instructionAmount;
    private TransactionType instructiontxnType;
    
    public Instruction() {       
    }

    public Instruction(InstructionBuilder builder) {
        super();
        this.instructionId = builder.orderId;
        this.instructionState = builder.orderState;
        this.instructionPortfolioCode = builder.orderPortfolioCode;
        this.instructionAsset = builder.orderAsset;
        this.instructionAmount = builder.orderAmount;
        this.instructiontxnType = builder.ordertxnType;
    }

    public String getInstructionId() {
        return instructionId;
    }

    public InstructionState getInstructionState() {
        return instructionState;
    }

    public Integer getInstructionPortfolioCode() {
        return instructionPortfolioCode;
    }

    public String getInstructionAsset() {
        return instructionAsset;
    }

    public BigDecimal getInstructionAmount() {
        return instructionAmount;
    }

    public TransactionType getInstructiontxnType() {
        return instructiontxnType;
    }

    public void rejected() {
        this.instructionState = InstructionState.INSTRUCTION_REJECTED;
    }

    public void approved() {
        this.instructionState = InstructionState.INSTRUCTION_APPROVED;
    }

    public static class InstructionBuilder {

        private String orderId;
        private InstructionState orderState;
        private Integer orderPortfolioCode;
        private String orderAsset;
        private BigDecimal orderAmount;
        private TransactionType ordertxnType;

        public InstructionBuilder withOrderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public InstructionBuilder withOrderState(InstructionState orderState) {
            this.orderState = orderState;
            return this;
        }

        public InstructionBuilder withPortfolioCode(Integer orderPortfolioCode) {
            this.orderPortfolioCode = orderPortfolioCode;
            return this;
        }

        public InstructionBuilder withAsset(String orderAsset) {
            this.orderAsset = orderAsset;
            return this;
        }

        public InstructionBuilder withAmount(BigDecimal orderAmount) {
            this.orderAmount = orderAmount;
            return this;
        }

        public InstructionBuilder withTxnType(TransactionType ordertxnType) {
            this.ordertxnType = ordertxnType;
            return this;
        }

        public Instruction build() {
            return new Instruction(this);
        }

        public static InstructionBuilder newBuilder() {
            return new InstructionBuilder();
        }

    }
}
