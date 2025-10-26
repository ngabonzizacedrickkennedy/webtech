package com.thms.repository;

import com.thms.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    // Find by name (case-insensitive)
    Optional<Genre> findByNameIgnoreCase(String name);

    // Check if genre exists by name
    boolean existsByNameIgnoreCase(String name);

    // Custom query example - Find genres with more than N movies
    @Query("SELECT g FROM Genre g WHERE SIZE(g.movies) > :count")
    List<Genre> findGenresWithMoreThanNMovies(@Param("count") int count);

    // Find all genres ordered by name
    @Query("SELECT g FROM Genre g ORDER BY g.name ASC")
    List<Genre> findAllOrderedByName();
}