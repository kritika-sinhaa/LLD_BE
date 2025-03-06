package com.practise.hub.controllers;

import com.practise.hub.dto.SubmissionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.practise.hub.services.SubmissionService;
import com.practise.hub.services.CodeExecutionService;
import com.practise.hub.dto.CodeSubmission;
import java.util.Map;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {
    @Autowired private SubmissionService submissionService;
    @Autowired private CodeExecutionService codeExecutionService;

    @PostMapping("/evaluate/{problemId}")
    public ResponseEntity<?> evaluateSubmission(
        @PathVariable Long problemId,
        @RequestBody SubmissionDto submissionDto) {
        
        // Add debug logging
        System.out.println("Received evaluation request for problem: " + problemId);
        System.out.println("Submission code: " + submissionDto.getCode());
        
        // Set the problemId from path variable
        submissionDto.setProblemId(problemId);
        
        // Get evaluation results
        Map<String, Object> evaluationResults = submissionService.evaluateSubmission(submissionDto);
        
        // Log the response
        System.out.println("Sending evaluation response: " + evaluationResults);
        
        // Ensure the response has the required fields
        if (!evaluationResults.containsKey("score")) {
            System.out.println("Warning: Response missing score field");
        }
        if (!evaluationResults.containsKey("showAiSolution")) {
            System.out.println("Warning: Response missing showAiSolution field");
        }
        
        return ResponseEntity.ok(evaluationResults);
    }

    @PostMapping("/execute")
    public ResponseEntity<?> executeCode(@RequestBody CodeSubmission request) {
        return ResponseEntity.ok(codeExecutionService.executeCode(request));
    }

    @PostMapping("/test/{problemId}")
    public ResponseEntity<?> testSolution(@PathVariable Long problemId, @RequestBody CodeSubmission submission) {
        // Basic test case template
        String testCase = """
                public class Solution {
                    public static void main(String[] args) {
                        // Test code will be injected here
                        %s
                    }
                }
                """.formatted(submission.getCode());
                
        submission.setCode(testCase);
        return ResponseEntity.ok(codeExecutionService.executeCode(submission));
    }

    @GetMapping("/test")
    public ResponseEntity<String> testConnection() {
        return ResponseEntity.ok("API is working!");
    }
}
