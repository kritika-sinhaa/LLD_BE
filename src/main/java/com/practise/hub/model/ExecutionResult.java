package com.practise.hub.model;

import lombok.Data;

@Data
public class ExecutionResult {
    private String output;
    private String error;
    private long executionTime;
    private long memoryUsed;

    public ExecutionResult() {
        this.executionTime = 0;
        this.memoryUsed = 0;
    }
} 