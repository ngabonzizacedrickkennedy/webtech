package com.thms.mapper;

import com.thms.dto.MovieDTO;
import com.thms.model.Genre;
import com.thms.model.Movie;
import com.thms.repository.GenreRepository;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper class for converting between Movie entity and MovieDTO
 */
@Component
public class MovieMapper {

    private final GenreRepository genreRepository;

    public MovieMapper(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    /**
     * Convert Movie entity to MovieDTO
     * @param movie Movie entity
     * @return MovieDTO
     */
    public MovieDTO toDTO(Movie movie) {
        if (movie == null) {
            return null;
        }

        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(movie.getId());
        movieDTO.setTitle(movie.getTitle());
        movieDTO.setDescription(movie.getDescription());
        movieDTO.setDurationMinutes(movie.getDurationMinutes());

        // Convert Genre entities to genre names
        Set<String> genreNames = movie.getGenres().stream()
                .map(Genre::getName)
                .collect(Collectors.toSet());
        movieDTO.setGenreNames(genreNames);

        movieDTO.setDirector(movie.getDirector());
        movieDTO.setCast(movie.getCast());
        movieDTO.setReleaseDate(movie.getReleaseDate());
        movieDTO.setPosterImageUrl(movie.getPosterImageUrl());
        movieDTO.setTrailerUrl(movie.getTrailerUrl());
        movieDTO.setRating(movie.getRating());

        return movieDTO;
    }

    /**
     * Convert MovieDTO to Movie entity
     * @param movieDTO MovieDTO
     * @return Movie entity
     */
    public Movie toEntity(MovieDTO movieDTO) {
        if (movieDTO == null) {
            return null;
        }

        Movie movie = new Movie();
        movie.setId(movieDTO.getId());
        movie.setTitle(movieDTO.getTitle());
        movie.setDescription(movieDTO.getDescription());
        movie.setDurationMinutes(movieDTO.getDurationMinutes());

        // Convert genre names to Genre entities
        Set<Genre> genres = new HashSet<>();
        if (movieDTO.getGenreNames() != null) {
            for (String genreName : movieDTO.getGenreNames()) {
                Genre genre = genreRepository.findByNameIgnoreCase(genreName)
                        .orElseGet(() -> {
                            Genre newGenre = new Genre(genreName);
                            return genreRepository.save(newGenre);
                        });
                genres.add(genre);
            }
        }
        movie.setGenres(genres);

        movie.setDirector(movieDTO.getDirector());
        movie.setCast(movieDTO.getCast());
        movie.setReleaseDate(movieDTO.getReleaseDate());
        movie.setPosterImageUrl(movieDTO.getPosterImageUrl());
        movie.setTrailerUrl(movieDTO.getTrailerUrl());
        movie.setRating(movieDTO.getRating());

        return movie;
    }

    /**
     * Update existing Movie entity with data from MovieDTO
     * @param movieDTO Source DTO
     * @param movie Target entity to update
     */
    public void updateEntityFromDTO(MovieDTO movieDTO, Movie movie) {
        if (movieDTO == null || movie == null) {
            return;
        }

        movie.setTitle(movieDTO.getTitle());
        movie.setDescription(movieDTO.getDescription());
        movie.setDurationMinutes(movieDTO.getDurationMinutes());

        // Update genres - clear existing and add new ones
        movie.getGenres().clear();
        if (movieDTO.getGenreNames() != null) {
            for (String genreName : movieDTO.getGenreNames()) {
                Genre genre = genreRepository.findByNameIgnoreCase(genreName)
                        .orElseGet(() -> {
                            Genre newGenre = new Genre(genreName);
                            return genreRepository.save(newGenre);
                        });
                movie.getGenres().add(genre);
            }
        }

        movie.setDirector(movieDTO.getDirector());
        movie.setCast(movieDTO.getCast());
        movie.setReleaseDate(movieDTO.getReleaseDate());
        movie.setPosterImageUrl(movieDTO.getPosterImageUrl());
        movie.setTrailerUrl(movieDTO.getTrailerUrl());
        movie.setRating(movieDTO.getRating());
    }
}