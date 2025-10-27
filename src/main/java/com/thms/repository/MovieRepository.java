package com.thms.repository;

import com.thms.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    // ========== NON-PAGEABLE METHODS ==========

    // Find by title (without pagination)
    List<Movie> findByTitleContainingIgnoreCase(String title);

    // Find by genre name (without pagination)
    @Query("SELECT DISTINCT m FROM Movie m JOIN m.genres g WHERE UPPER(g.name) = UPPER(:genreName)")
    List<Movie> findByGenreName(@Param("genreName") String genreName);

    // Find movies after a certain release date
    List<Movie> findByReleaseDateAfter(LocalDate date);

    // Find upcoming movies (without pagination)
    @Query("SELECT m FROM Movie m WHERE m.releaseDate > :currentDate ORDER BY m.releaseDate ASC")
    List<Movie> findUpcomingMovies(@Param("currentDate") LocalDate currentDate);

    // ========== PAGEABLE METHODS ==========

    // Find by title (with pagination)
    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    // Find by genre name (with pagination)
    @Query("SELECT DISTINCT m FROM Movie m JOIN m.genres g WHERE UPPER(g.name) = UPPER(:genreName)")
    Page<Movie> findByGenreName(@Param("genreName") String genreName, Pageable pageable);

    // Find movies after a certain release date (with pagination)
    Page<Movie> findByReleaseDateAfter(LocalDate date, Pageable pageable);

    // Find upcoming movies (with pagination)
    @Query("SELECT m FROM Movie m WHERE m.releaseDate > :currentDate ORDER BY m.releaseDate ASC")
    Page<Movie> findUpcomingMovies(@Param("currentDate") LocalDate currentDate, Pageable pageable);

    // Advanced filter query with pagination
    @Query("SELECT m FROM Movie m LEFT JOIN m.genres g WHERE " +
            "(:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:genreName IS NULL OR UPPER(g.name) = UPPER(:genreName))")
    Page<Movie> findMoviesWithFilters(@Param("title") String title,
                                      @Param("genreName") String genreName,
                                      Pageable pageable);

    // ========== GLOBAL SEARCH METHODS ==========

    // Global search by title, director, or cast (with pagination)
    @Query("SELECT DISTINCT m FROM Movie m WHERE " +
            "LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(m.director) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(m.cast) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Movie> findByTitleContainingIgnoreCaseOrDirectorContainingIgnoreCaseOrCastContainingIgnoreCase(
            @Param("query") String query,
            Pageable pageable);

    // Advanced search including description
    @Query("SELECT DISTINCT m FROM Movie m WHERE " +
            "LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(m.director) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(m.cast) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(m.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Movie> searchMovies(@Param("query") String query, Pageable pageable);
}