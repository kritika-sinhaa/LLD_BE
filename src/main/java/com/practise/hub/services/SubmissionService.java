package com.practise.hub.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.practise.hub.dto.SubmissionDto;
import java.util.HashMap;
import java.util.Map;

@Service
public class SubmissionService {

    private EvaluationService evaluationService;

    @Autowired
    public SubmissionService(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    public Map<String, Object> evaluateSubmission(SubmissionDto submissionDto) {
        if (submissionDto == null) {
            throw new IllegalArgumentException("Submission cannot be null");
        }

        Map<String, Object> evaluationResult = new HashMap<>();
        
        try {
            boolean isValid = validateSubmission(submissionDto);
            evaluationResult.put("isValid", isValid);
            
            if (isValid) {
                double score = calculateScore(submissionDto);
                String feedback = generateFeedback(submissionDto);
                
                // Debug logging
                System.out.println("Calculated score: " + score);
                System.out.println("Generated feedback: " + feedback);
                
                evaluationResult.put("status", "SUCCESS");
                evaluationResult.put("score", score);
                evaluationResult.put("feedback", feedback);
                evaluationResult.put("showAiSolution", score < 90.0); // Show AI solution if score is less than 90
                evaluationResult.put("problemId", submissionDto.getProblemId());
                
                // Debug log the final result
                System.out.println("Final evaluation result: " + evaluationResult);
            } else {
                evaluationResult.put("status", "INVALID");
                evaluationResult.put("error", "Invalid submission format");
            }
            
        } catch (Exception e) {
            System.err.println("Error during evaluation: " + e.getMessage());
            e.printStackTrace();
            evaluationResult.put("status", "ERROR");
            evaluationResult.put("error", e.getMessage());
        }
        
        return evaluationResult;
    }

    private boolean validateSubmission(SubmissionDto submissionDto) {
        // Add your validation logic here
        return true;
    }

    private double calculateScore(SubmissionDto submissionDto) {
        return evaluationService.evaluate(
            submissionDto.getCode(),
            submissionDto.getFunctionalRequirements(),
            "" // class design parameter
        );
    }

    private String generateFeedback(SubmissionDto submissionDto) {
        double score = calculateScore(submissionDto);
        StringBuilder feedback = new StringBuilder();
        
        // Add specific feedback based on score
        if (score >= 90) {
            feedback.append("Excellent work! Your solution meets all requirements. ");
        } else if (score >= 70) {
            feedback.append("Good job! There's room for some improvements. ");
        } else {
            feedback.append("Your solution needs more work. ");
        }
        
        // Add specific feedback about code structure
        if (!submissionDto.getCode().contains("//")) {
            feedback.append("Consider adding comments to improve code readability. ");
        }
        
        if (!submissionDto.getCode().contains("private")) {
            feedback.append("Consider using private access modifiers for better encapsulation. ");
        }
        
        return feedback.toString();
    }
} 