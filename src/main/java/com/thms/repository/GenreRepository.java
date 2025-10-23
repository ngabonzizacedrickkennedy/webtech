package com.thms.repository;

import com.thms.model.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Repository for Genre entity
 *
 * Demonstrates various JPA query methods for Many-to-Many relationship
 */
@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    // ========== FIND BY QUERIES ==========

    /**
     * Find genre by name (case-sensitive)
     */
    Optional<Genre> findByName(String name);

    /**
     * Find genre by name (case-insensitive)
     */
    Optional<Genre> findByNameIgnoreCase(String name);

    /**
     * Find genres by name containing keyword (case-insensitive)
     */
    List<Genre> findByNameContainingIgnoreCase(String keyword);

    /**
     * Find genres by description containing keyword
     */
    List<Genre> findByDescriptionContainingIgnoreCase(String keyword);

    /**
     * Find genres by multiple names
     */
    List<Genre> findByNameIn(List<String> names);

    /**
     * Find genres by name starting with prefix
     */
    List<Genre> findByNameStartingWithIgnoreCase(String prefix);

    // ========== EXISTS BY QUERIES ==========

    /**
     * Check if genre exists by name
     */
    boolean existsByName(String name);

    /**
     * Check if genre exists by name (case-insensitive)
     */
    boolean existsByNameIgnoreCase(String name);

    // ========== CUSTOM @QUERY METHODS ==========

    /**
     * Get genres used by at least one movie
     */
    @Query("SELECT DISTINCT g FROM Genre g WHERE SIZE(g.movies) > 0")
    List<Genre> findGenresWithMovies();

    /**
     * Get genres with no associated movies
     */
    @Query("SELECT g FROM Genre g WHERE SIZE(g.movies) = 0")
    List<Genre> findGenresWithoutMovies();

    /**
     * Get genres ordered by number of movies (most popular first)
     */
    @Query("SELECT g FROM Genre g LEFT JOIN g.movies m GROUP BY g.id ORDER BY COUNT(m) DESC")
    List<Genre> findGenresOrderedByPopularity();

    /**
     * Get top N genres by movie count
     */
    @Query("SELECT g FROM Genre g LEFT JOIN g.movies m GROUP BY g.id ORDER BY COUNT(m) DESC")
    List<Genre> findTopGenres(Pageable pageable);

    /**
     * Count movies for a specific genre
     */
    @Query("SELECT COUNT(m) FROM Movie m JOIN m.genres g WHERE g.id = :genreId")
    long countMoviesByGenreId(@Param("genreId") Long genreId);

    /**
     * Count movies for a specific genre by name
     */
    @Query("SELECT COUNT(m) FROM Movie m JOIN m.genres g WHERE g.name = :genreName")
    long countMoviesByGenreName(@Param("genreName") String genreName);

    /**
     * Find genres that a specific movie has
     */
    @Query("SELECT g FROM Genre g JOIN g.movies m WHERE m.id = :movieId")
    Set<Genre> findGenresByMovieId(@Param("movieId") Long movieId);

    /**
     * Find genres by movie title
     */
    @Query("SELECT g FROM Genre g JOIN g.movies m WHERE m.title LIKE %:movieTitle%")
    Set<Genre> findGenresByMovieTitle(@Param("movieTitle") String movieTitle);

    /**
     * Find genres that have movies released after a specific year
     */
    @Query("SELECT DISTINCT g FROM Genre g JOIN g.movies m WHERE YEAR(m.releaseDate) >= :year")
    List<Genre> findGenresWithMoviesAfterYear(@Param("year") int year);

    /**
     * Find genres with average movie duration
     */
    @Query("SELECT g.name, AVG(m.durationMinutes) FROM Genre g JOIN g.movies m GROUP BY g.id, g.name")
    List<Object[]> findGenresWithAvgDuration();

    /**
     * Search genres by name or description
     */
    @Query("SELECT g FROM Genre g WHERE LOWER(g.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(g.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Genre> searchGenres(@Param("searchTerm") String searchTerm);

    // ========== SORTING ==========

    /**
     * Find all genres with sorting
     */
    List<Genre> findAll(Sort sort);

    /**
     * Find genres with movies, sorted
     */
    @Query("SELECT DISTINCT g FROM Genre g WHERE SIZE(g.movies) > 0")
    List<Genre> findGenresWithMovies(Sort sort);

    // ========== PAGINATION ==========

    /**
     * Find all genres with pagination
     */
    Page<Genre> findAll(Pageable pageable);

    /**
     * Find genres by name containing keyword with pagination
     */
    Page<Genre> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    /**
     * Find genres with movies with pagination
     */
    @Query("SELECT DISTINCT g FROM Genre g WHERE SIZE(g.movies) > 0")
    Page<Genre> findGenresWithMovies(Pageable pageable);
}