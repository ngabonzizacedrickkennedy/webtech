package com.thms.controller.api;

import com.thms.dto.UserProfileDTO;
import com.thms.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for UserProfile Management
 *
 * Demonstrates full CRUD operations via REST API as required for midterm
 *
 * Base URL: /api/profiles
 */
@RestController
@RequestMapping("/api/profiles")
public class UserProfileRestController {

    private final UserProfileService userProfileService;

    public UserProfileRestController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    // ========== CREATE ==========

    /**
     * Create a new user profile
     * POST /api/profiles
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<UserProfileDTO> createProfile(@Valid @RequestBody UserProfileDTO profileDTO) {
        try {
            UserProfileDTO createdProfile = userProfileService.createProfile(profileDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProfile);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Create profile for current authenticated user
     * POST /api/profiles/me
     */
    @PostMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileDTO> createMyProfile(@Valid @RequestBody UserProfileDTO profileDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Get user profile by username and set the user ID
        // This ensures users can only create their own profile
        profileDTO.setUsername(username);

        try {
            UserProfileDTO createdProfile = userProfileService.createProfile(profileDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProfile);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ========== READ ==========

    /**
     * Get all profiles (Admin only)
     * GET /api/profiles
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserProfileDTO>> getAllProfiles() {
        List<UserProfileDTO> profiles = userProfileService.getAllProfiles();
        return ResponseEntity.ok(profiles);
    }

    /**
     * Get all profiles with pagination and sorting
     * GET /api/profiles/paginated?page=0&size=10&sortBy=createdAt&sortDir=desc
     */
    @GetMapping("/paginated")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserProfileDTO>> getAllProfilesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserProfileDTO> profiles = userProfileService.getAllProfiles(pageable);

        return ResponseEntity.ok(profiles);
    }

    /**
     * Get profile by ID
     * GET /api/profiles/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<UserProfileDTO> getProfileById(@PathVariable Long id) {
        return userProfileService.getProfileById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get current user's profile
     * GET /api/profiles/me
     */
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileDTO> getMyProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return userProfileService.getProfileByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get profile by user ID
     * GET /api/profiles/user/{userId}
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<UserProfileDTO> getProfileByUserId(@PathVariable Long userId) {
        return userProfileService.getProfileByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get profiles by preferred genre
     * GET /api/profiles/genre/{genre}
     */
    @GetMapping("/genre/{genre}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<List<UserProfileDTO>> getProfilesByGenre(@PathVariable String genre) {
        List<UserProfileDTO> profiles = userProfileService.getProfilesByGenre(genre);
        return ResponseEntity.ok(profiles);
    }

    /**
     * Get profiles with pagination by genre
     * GET /api/profiles/genre/{genre}/paginated?page=0&size=10
     */
    @GetMapping("/genre/{genre}/paginated")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<Page<UserProfileDTO>> getProfilesByGenrePaginated(
            @PathVariable String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<UserProfileDTO> profiles = userProfileService.getProfilesByGenre(genre, pageable);

        return ResponseEntity.ok(profiles);
    }

    /**
     * Get profiles with email notifications enabled
     * GET /api/profiles/email-notifications
     */
    @GetMapping("/email-notifications")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<List<UserProfileDTO>> getProfilesWithEmailNotifications() {
        List<UserProfileDTO> profiles = userProfileService.getProfilesWithEmailNotifications();
        return ResponseEntity.ok(profiles);
    }

    /**
     * Get profiles by birth month
     * GET /api/profiles/birth-month/{month}
     */
    @GetMapping("/birth-month/{month}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<List<UserProfileDTO>> getProfilesByBirthMonth(@PathVariable int month) {
        if (month < 1 || month > 12) {
            return ResponseEntity.badRequest().build();
        }
        List<UserProfileDTO> profiles = userProfileService.getProfilesByBirthMonth(month);
        return ResponseEntity.ok(profiles);
    }

    /**
     * Get profiles by province code (demonstrates location integration)
     * GET /api/profiles/province/{provinceCode}
     */
    @GetMapping("/province/{provinceCode}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<List<UserProfileDTO>> getProfilesByProvinceCode(@PathVariable String provinceCode) {
        List<UserProfileDTO> profiles = userProfileService.getProfilesByProvinceCode(provinceCode);
        return ResponseEntity.ok(profiles);
    }

    /**
     * Get most active users (by booking count)
     * GET /api/profiles/most-active
     */
    @GetMapping("/most-active")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<List<UserProfileDTO>> getMostActiveUsers() {
        List<UserProfileDTO> profiles = userProfileService.getMostActiveUsers();
        return ResponseEntity.ok(profiles);
    }

    // ========== UPDATE ==========

    /**
     * Update profile by ID
     * PUT /api/profiles/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<UserProfileDTO> updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody UserProfileDTO profileDTO) {
        try {
            UserProfileDTO updatedProfile = userProfileService.updateProfile(id, profileDTO);
            return ResponseEntity.ok(updatedProfile);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update current user's profile
     * PUT /api/profiles/me
     */
    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileDTO> updateMyProfile(@Valid @RequestBody UserProfileDTO profileDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        try {
            // Get user's profile first to get the ID
            UserProfileDTO existingProfile = userProfileService.getProfileByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Profile not found"));

            UserProfileDTO updatedProfile = userProfileService.updateProfile(existingProfile.getId(), profileDTO);
            return ResponseEntity.ok(updatedProfile);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Partially update profile (PATCH)
     * PATCH /api/profiles/{id}
     */
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<UserProfileDTO> partialUpdateProfile(
            @PathVariable Long id,
            @RequestBody UserProfileDTO profileDTO) {
        try {
            UserProfileDTO updatedProfile = userProfileService.updateProfile(id, profileDTO);
            return ResponseEntity.ok(updatedProfile);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ========== DELETE ==========

    /**
     * Delete profile by ID (Admin only)
     * DELETE /api/profiles/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        try {
            userProfileService.deleteProfile(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete current user's profile
     * DELETE /api/profiles/me
     */
    @DeleteMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteMyProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        try {
            UserProfileDTO profile = userProfileService.getProfileByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Profile not found"));

            userProfileService.deleteProfile(profile.getId());
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ========== UTILITY ENDPOINTS ==========

    /**
     * Check if profile exists for a user
     * GET /api/profiles/exists/user/{userId}
     */
    @GetMapping("/exists/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<Boolean> profileExistsForUser(@PathVariable Long userId) {
        boolean exists = userProfileService.profileExistsForUser(userId);
        return ResponseEntity.ok(exists);
    }

    /**
     * Get count of profiles by genre
     * GET /api/profiles/count/genre/{genre}
     */
    @GetMapping("/count/genre/{genre}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<Long> countProfilesByGenre(@PathVariable String genre) {
        long count = userProfileService.countProfilesByGenre(genre);
        return ResponseEntity.ok(count);
    }

    /**
     * Get count of profiles by province
     * GET /api/profiles/count/province/{provinceId}
     */
    @GetMapping("/count/province/{provinceId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<Long> countProfilesByProvince(@PathVariable Long provinceId) {
        long count = userProfileService.countProfilesByProvince(provinceId);
        return ResponseEntity.ok(count);
    }
}