package com.example.pr_creation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GitHubPRPayload {
    private String title;
    private String state;
    private String body;

    // Add more fields as needed

    // Getters and setters
}