package com.thms.dto;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for UserProfile entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {

    private Long id;

    private Long userId;

    private String username; // From associated User

    @Size(max = 1000, message = "Bio cannot exceed 1000 characters")
    private String bio;

    private String profilePictureUrl;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @Size(max = 50, message = "Preferred genre cannot exceed 50 characters")
    private String preferredGenre;

    @Size(max = 50, message = "Preferred language cannot exceed 50 characters")
    private String preferredLanguage;

    private Boolean emailNotifications;

    private Boolean smsNotifications;

    @Size(max = 500, message = "Favorite theatres cannot exceed 500 characters")
    private String favoriteTheatres;

    private LocalDate createdAt;

    private LocalDate updatedAt;
}