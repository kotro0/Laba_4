package com.example.demo.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class SpeakerDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Size(max = 1000, message = "Bio cannot exceed 1000 characters")
    private String bio;

    private String company;
    private String specialization;
}