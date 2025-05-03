package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ReviewDto {
    @NotBlank(message = "Review content cannot be empty")
    @Size(min = 10, max = 500, message = "Review must be between 10-500 characters")
    private String content;

    // Getters and setters
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}