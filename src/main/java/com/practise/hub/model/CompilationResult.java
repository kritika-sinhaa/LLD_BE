package com.practise.hub.model;

import lombok.Data;

@Data
public class CompilationResult {
    private boolean success;
    private String error;
    private String compiledFilePath;

    public CompilationResult() {
        this.success = true;
    }

    public CompilationResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }
} 