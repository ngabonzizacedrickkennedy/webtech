package com.thms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactRequestDTO {
    
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;
    
    @NotBlank(message = "Subject is required")
    @Size(max = 200, message = "Subject cannot exceed 200 characters")
    private String subject;
    
    @NotBlank(message = "Message is required")
    @Size(max = 2000, message = "Message cannot exceed 2000 characters")
    private String message;
} 