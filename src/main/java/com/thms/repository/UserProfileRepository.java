package com.thms.repository;

import com.thms.model.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for UserProfile entity
 *
 * Demonstrates various JPA query methods as required for midterm:
 * - findBy queries
 * - existsBy queries
 * - Custom @Query methods
 * - Pagination and Sorting support
 */
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    // ========== FIND BY QUERIES ==========

    /**
     * Find profile by user ID (demonstrates relationship query)
     */
    Optional<UserProfile> findByUserId(Long userId);

    /**
     * Find profile by user username (demonstrates nested property query)
     */
    Optional<UserProfile> findByUserUsername(String username);

    /**
     * Find profiles by preferred genre
     */
    List<UserProfile> findByPreferredGenre(String genre);

    /**
     * Find profiles with email notifications enabled
     */
    List<UserProfile> findByEmailNotificationsTrue();

    /**
     * Find profiles with SMS notifications enabled
     */
    List<UserProfile> findBySmsNotificationsTrue();

    /**
     * Find profiles by preferred language
     */
    List<UserProfile> findByPreferredLanguage(String language);

    /**
     * Find profiles created after a certain date
     */
    List<UserProfile> findByCreatedAtAfter(LocalDate date);

    /**
     * Find profiles updated after a certain date
     */
    List<UserProfile> findByUpdatedAtAfter(LocalDate date);

    /**
     * Find profiles by birth year (using date range)
     */
    List<UserProfile> findByDateOfBirthBetween(LocalDate startDate, LocalDate endDate);

    // ========== EXISTS BY QUERIES ==========

    /**
     * Check if profile exists for a user
     */
    boolean existsByUserId(Long userId);

    /**
     * Check if profile exists with specific username
     */
    boolean existsByUserUsername(String username);

    /**
     * Check if any profiles exist for a specific genre preference
     */
    boolean existsByPreferredGenre(String genre);

    /**
     * Check if any profiles exist with email notifications enabled
     */
    boolean existsByEmailNotificationsTrue();

    // ========== CUSTOM @QUERY METHODS ==========

    /**
     * Get count of profiles by preferred genre
     */
    @Query("SELECT COUNT(up) FROM UserProfile up WHERE up.preferredGenre = :genre")
    long countByPreferredGenre(@Param("genre") String genre);

    /**
     * Get users with profiles who have birthdays in a specific month
     */
    @Query("SELECT up FROM UserProfile up WHERE FUNCTION('MONTH', up.dateOfBirth) = :month")
    List<UserProfile> findByBirthMonth(@Param("month") int month);

    /**
     * Get profiles with bio text containing keyword (case-insensitive)
     */
    @Query("SELECT up FROM UserProfile up WHERE LOWER(up.bio) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<UserProfile> findByBioContaining(@Param("keyword") String keyword);

    /**
     * Get profiles for users from a specific province (joining across relationships)
     */
    @Query("SELECT up FROM UserProfile up WHERE up.user.village.cell.sector.district.province.code = :provinceCode")
    List<UserProfile> findByProvinceCode(@Param("provinceCode") String provinceCode);

    /**
     * Get count of profiles by province
     */
    @Query("SELECT COUNT(up) FROM UserProfile up WHERE up.user.village.cell.sector.district.province.id = :provinceId")
    long countByProvinceId(@Param("provinceId") Long provinceId);

    /**
     * Get profiles of users who have made bookings
     */
    @Query("SELECT DISTINCT up FROM UserProfile up WHERE SIZE(up.user.bookings) > 0")
    List<UserProfile> findProfilesWithBookings();

    /**
     * Get profiles ordered by number of bookings (most active users)
     */
    @Query("SELECT up FROM UserProfile up LEFT JOIN up.user u LEFT JOIN u.bookings b " +
            "GROUP BY up.id ORDER BY COUNT(b) DESC")
    List<UserProfile> findMostActiveUsers();

    // ========== PAGINATION AND SORTING ==========

    /**
     * Find all profiles with pagination and sorting support
     */
    Page<UserProfile> findAll(Pageable pageable);

    /**
     * Find profiles by preferred genre with pagination
     */
    Page<UserProfile> findByPreferredGenre(String genre, Pageable pageable);

    /**
     * Find profiles with email notifications enabled with pagination
     */
    Page<UserProfile> findByEmailNotificationsTrue(Pageable pageable);

    /**
     * Find profiles created after date with pagination
     */
    Page<UserProfile> findByCreatedAtAfter(LocalDate date, Pageable pageable);
}