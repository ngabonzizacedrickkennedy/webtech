package com.thms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * UserProfile entity - Demonstrates ONE-TO-ONE relationship with User
 *
 * This entity stores extended profile information for users.
 * Each User has exactly ONE UserProfile, and each UserProfile belongs to exactly ONE User.
 */
@Entity
@Table(name = "user_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ONE-TO-ONE relationship with User
     * - unique = true ensures one profile per user
     * - nullable = false ensures every profile must have a user
     */
    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Size(max = 1000)
    @Column(length = 1000)
    private String bio;

    @Column(name = "profile_picture_url", columnDefinition = "TEXT")
    private String profilePictureUrl;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @Size(max = 50)
    private String preferredGenre; // Favorite movie genre

    @Size(max = 50)
    private String preferredLanguage; // Preferred language for movies

    private Boolean emailNotifications = true;

    private Boolean smsNotifications = false;

    @Size(max = 500)
    private String favoriteTheatres; // Comma-separated theatre names or IDs

    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt = LocalDate.now();

    @Column(name = "updated_at")
    private LocalDate updatedAt = LocalDate.now();

    /**
     * Helper method to update timestamp
     */
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDate.now();
    }
}