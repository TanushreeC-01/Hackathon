package com.example.pr_creation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangedFile {
    private String filename;
    private String status;
    private int additions;
    private int deletions;
    private int changes;
    private String patch;

    // Getters and setters
}

