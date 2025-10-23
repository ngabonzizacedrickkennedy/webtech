package com.thms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Genre entity - Demonstrates MANY-TO-MANY relationship with Movie
 *
 * Each Movie can have multiple Genres, and each Genre can be associated with multiple Movies.
 * This creates a Many-to-Many relationship managed through a join table "movie_genres".
 */
@Entity
@Table(name = "genres")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(unique = true, nullable = false)
    private String name; // e.g., "ACTION", "COMEDY", "DRAMA", etc.

    @Size(max = 500)
    private String description;

    /**
     * MANY-TO-MANY bidirectional relationship with Movie
     * - mappedBy = "genres" indicates Movie is the owning side
     * - This is the inverse side of the relationship
     */
    @ManyToMany(mappedBy = "genres")
    private Set<Movie> movies = new HashSet<>();

    /**
     * Constructor for creating genre with name only
     */
    public Genre(String name) {
        this.name = name;
    }

    /**
     * Constructor for creating genre with name and description
     */
    public Genre(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Helper method to add a movie to this genre
     */
    public void addMovie(Movie movie) {
        this.movies.add(movie);
        movie.getGenres().add(this);
    }

    /**
     * Helper method to remove a movie from this genre
     */
    public void removeMovie(Movie movie) {
        this.movies.remove(movie);
        movie.getGenres().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Genre)) return false;
        Genre genre = (Genre) o;
        return id != null && id.equals(genre.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}