package com.practise.hub.dto;

import lombok.Data;

@Data
public class CodeExecutionRequest {
    private String code;
    private String language;
    private String input;
} 