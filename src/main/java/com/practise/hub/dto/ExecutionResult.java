package com.practise.hub.dto;

public class ExecutionResult {
    private String output;
    private String error;
    private boolean success;
    private long executionTime;

    public ExecutionResult(String output, String error, boolean success, int executionTime) {
        this.output = output;
        this.error = error;
        this.success = success;
        this.executionTime = executionTime;
    }

    // Existing constructors and getters/setters...
} 