package com.thms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String title;

    @Size(max = 500)
    private String description;

    @NotNull
    @PositiveOrZero
    private Integer durationMinutes;

    // MANY-TO-MANY relationship with Genre (owning side)
    // This replaces the Genre enum that was here before
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @Size(max = 255)
    private String director;

    @Size(max = 255)
    @Column(name = "movie_cast")
    private String cast;

    private LocalDate releaseDate;

    @Column(name = "poster_image_url", columnDefinition = "TEXT")
    private String posterImageUrl;

    @Column(name = "trailer_url", columnDefinition = "TEXT")
    private String trailerUrl;

    @Enumerated(EnumType.STRING)
    private Rating rating;

    // ONE-TO-MANY relationship with Screening
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private Set<Screening> screenings = new HashSet<>();

    // Keep Rating as enum (this is fine - it's for movie ratings like G, PG, R)
    public enum Rating {
        G, PG, PG13, R, NC17, UNRATED
    }

    // Helper methods for Many-to-Many relationship with Genre
    public void addGenre(Genre genre) {
        this.genres.add(genre);
        genre.getMovies().add(this);
    }

    public void removeGenre(Genre genre) {
        this.genres.remove(genre);
        genre.getMovies().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        Movie movie = (Movie) o;
        return id != null && id.equals(movie.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}