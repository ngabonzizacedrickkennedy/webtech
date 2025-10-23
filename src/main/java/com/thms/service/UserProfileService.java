package com.thms.service;

import com.thms.dto.UserProfileDTO;
import com.thms.model.User;
import com.thms.model.UserProfile;
import com.thms.repository.UserProfileRepository;
import com.thms.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service layer for UserProfile entity
 *
 * Implements full CRUD operations as required for midterm
 */
@Service
@Transactional
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    public UserProfileService(UserProfileRepository userProfileRepository,
                              UserRepository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }

    // ========== CREATE ==========

    /**
     * Create a new user profile
     */
    public UserProfileDTO createProfile(UserProfileDTO profileDTO) {
        // Find the user
        User user = userRepository.findById(profileDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + profileDTO.getUserId()));

        // Check if profile already exists for this user
        if (userProfileRepository.existsByUserId(user.getId())) {
            throw new RuntimeException("Profile already exists for user: " + user.getUsername());
        }

        // Create new profile
        UserProfile profile = new UserProfile();
        profile.setUser(user);
        profile.setBio(profileDTO.getBio());
        profile.setProfilePictureUrl(profileDTO.getProfilePictureUrl());
        profile.setDateOfBirth(profileDTO.getDateOfBirth());
        profile.setPreferredGenre(profileDTO.getPreferredGenre());
        profile.setPreferredLanguage(profileDTO.getPreferredLanguage());
        profile.setEmailNotifications(profileDTO.getEmailNotifications() != null ?
                profileDTO.getEmailNotifications() : true);
        profile.setSmsNotifications(profileDTO.getSmsNotifications() != null ?
                profileDTO.getSmsNotifications() : false);
        profile.setFavoriteTheatres(profileDTO.getFavoriteTheatres());
        profile.setCreatedAt(LocalDate.now());
        profile.setUpdatedAt(LocalDate.now());

        UserProfile savedProfile = userProfileRepository.save(profile);
        return convertToDTO(savedProfile);
    }

    // ========== READ ==========

    /**
     * Get all profiles
     */
    @Transactional(readOnly = true)
    public List<UserProfileDTO> getAllProfiles() {
        return userProfileRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get profile by ID
     */
    @Transactional(readOnly = true)
    public Optional<UserProfileDTO> getProfileById(Long id) {
        return userProfileRepository.findById(id)
                .map(this::convertToDTO);
    }

    /**
     * Get profile by user ID
     */
    @Transactional(readOnly = true)
    public Optional<UserProfileDTO> getProfileByUserId(Long userId) {
        return userProfileRepository.findByUserId(userId)
                .map(this::convertToDTO);
    }

    /**
     * Get profile by username
     */
    @Transactional(readOnly = true)
    public Optional<UserProfileDTO> getProfileByUsername(String username) {
        return userProfileRepository.findByUserUsername(username)
                .map(this::convertToDTO);
    }

    /**
     * Get profiles by preferred genre
     */
    @Transactional(readOnly = true)
    public List<UserProfileDTO> getProfilesByGenre(String genre) {
        return userProfileRepository.findByPreferredGenre(genre).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get profiles with email notifications enabled
     */
    @Transactional(readOnly = true)
    public List<UserProfileDTO> getProfilesWithEmailNotifications() {
        return userProfileRepository.findByEmailNotificationsTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get profiles by birth month
     */
    @Transactional(readOnly = true)
    public List<UserProfileDTO> getProfilesByBirthMonth(int month) {
        return userProfileRepository.findByBirthMonth(month).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get profiles by province code
     */
    @Transactional(readOnly = true)
    public List<UserProfileDTO> getProfilesByProvinceCode(String provinceCode) {
        return userProfileRepository.findByProvinceCode(provinceCode).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get most active users (by booking count)
     */
    @Transactional(readOnly = true)
    public List<UserProfileDTO> getMostActiveUsers() {
        return userProfileRepository.findMostActiveUsers().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all profiles with pagination
     */
    @Transactional(readOnly = true)
    public Page<UserProfileDTO> getAllProfiles(Pageable pageable) {
        return userProfileRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    /**
     * Get profiles by genre with pagination
     */
    @Transactional(readOnly = true)
    public Page<UserProfileDTO> getProfilesByGenre(String genre, Pageable pageable) {
        return userProfileRepository.findByPreferredGenre(genre, pageable)
                .map(this::convertToDTO);
    }

    // ========== UPDATE ==========

    /**
     * Update an existing profile
     */
    public UserProfileDTO updateProfile(Long id, UserProfileDTO profileDTO) {
        UserProfile profile = userProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + id));

        // Update fields
        if (profileDTO.getBio() != null) {
            profile.setBio(profileDTO.getBio());
        }
        if (profileDTO.getProfilePictureUrl() != null) {
            profile.setProfilePictureUrl(profileDTO.getProfilePictureUrl());
        }
        if (profileDTO.getDateOfBirth() != null) {
            profile.setDateOfBirth(profileDTO.getDateOfBirth());
        }
        if (profileDTO.getPreferredGenre() != null) {
            profile.setPreferredGenre(profileDTO.getPreferredGenre());
        }
        if (profileDTO.getPreferredLanguage() != null) {
            profile.setPreferredLanguage(profileDTO.getPreferredLanguage());
        }
        if (profileDTO.getEmailNotifications() != null) {
            profile.setEmailNotifications(profileDTO.getEmailNotifications());
        }
        if (profileDTO.getSmsNotifications() != null) {
            profile.setSmsNotifications(profileDTO.getSmsNotifications());
        }
        if (profileDTO.getFavoriteTheatres() != null) {
            profile.setFavoriteTheatres(profileDTO.getFavoriteTheatres());
        }

        profile.setUpdatedAt(LocalDate.now());

        UserProfile updatedProfile = userProfileRepository.save(profile);
        return convertToDTO(updatedProfile);
    }

    /**
     * Update profile by user ID
     */
    public UserProfileDTO updateProfileByUserId(Long userId, UserProfileDTO profileDTO) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found for user id: " + userId));

        return updateProfile(profile.getId(), profileDTO);
    }

    // ========== DELETE ==========

    /**
     * Delete a profile by ID
     */
    public void deleteProfile(Long id) {
        if (!userProfileRepository.existsById(id)) {
            throw new RuntimeException("Profile not found with id: " + id);
        }
        userProfileRepository.deleteById(id);
    }

    /**
     * Delete profile by user ID
     */
    public void deleteProfileByUserId(Long userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found for user id: " + userId));
        userProfileRepository.delete(profile);
    }

    // ========== UTILITY METHODS ==========

    /**
     * Check if profile exists for user
     */
    @Transactional(readOnly = true)
    public boolean profileExistsForUser(Long userId) {
        return userProfileRepository.existsByUserId(userId);
    }

    /**
     * Get count of profiles by genre
     */
    @Transactional(readOnly = true)
    public long countProfilesByGenre(String genre) {
        return userProfileRepository.countByPreferredGenre(genre);
    }

    /**
     * Get count of profiles by province
     */
    @Transactional(readOnly = true)
    public long countProfilesByProvince(Long provinceId) {
        return userProfileRepository.countByProvinceId(provinceId);
    }

    // ========== CONVERSION METHODS ==========

    /**
     * Convert UserProfile entity to DTO
     */
    private UserProfileDTO convertToDTO(UserProfile profile) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(profile.getId());
        dto.setUserId(profile.getUser().getId());
        dto.setUsername(profile.getUser().getUsername());
        dto.setBio(profile.getBio());
        dto.setProfilePictureUrl(profile.getProfilePictureUrl());
        dto.setDateOfBirth(profile.getDateOfBirth());
        dto.setPreferredGenre(profile.getPreferredGenre());
        dto.setPreferredLanguage(profile.getPreferredLanguage());
        dto.setEmailNotifications(profile.getEmailNotifications());
        dto.setSmsNotifications(profile.getSmsNotifications());
        dto.setFavoriteTheatres(profile.getFavoriteTheatres());
        dto.setCreatedAt(profile.getCreatedAt());
        dto.setUpdatedAt(profile.getUpdatedAt());
        return dto;
    }
}