package com.thms.dto;

import com.thms.model.Movie;
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

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Duration is required")
    @PositiveOrZero(message = "Duration must be a positive number")
    private Integer durationMinutes;

    // Changed from Movie.Genre to Set<String> for genre names
    private Set<String> genreNames = new HashSet<>();

    @Size(max = 255, message = "Director name cannot exceed 255 characters")
    private String director;

    @Size(max = 255, message = "Cast info cannot exceed 255 characters")
    private String cast;

    private LocalDate releaseDate;

    private String posterImageUrl;

    private String trailerUrl;

    private Movie.Rating rating;
}