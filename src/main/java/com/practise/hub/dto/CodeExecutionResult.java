package com.practise.hub.dto;

import lombok.Data;

@Data
public class CodeExecutionResult {
    private String status;
    private String output;
    private String error;
    private long executionTime;
    private long memoryUsed;
} 