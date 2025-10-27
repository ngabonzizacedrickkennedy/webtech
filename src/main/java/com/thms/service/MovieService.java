package com.thms.service;

import com.thms.dto.MovieDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MovieService {

    // Existing methods
    List<MovieDTO> getAllMovies();
    Optional<MovieDTO> getMovieById(Long id);
    MovieDTO createMovie(MovieDTO movieDTO);
    Optional<MovieDTO> updateMovie(Long id, MovieDTO movieDTO);
    void deleteMovie(Long id);
    List<MovieDTO> searchMoviesByTitle(String title);

    // Updated: Changed from Movie.Genre to String
    List<MovieDTO> getMoviesByGenreName(String genreName);

    List<MovieDTO> getUpcomingMovies();
    List<MovieDTO> getMoviesByIds(Set<Long> ids);

    // New pagination methods
    Page<MovieDTO> getAllMovies(Pageable pageable);
    Page<MovieDTO> searchMoviesByTitle(String title, Pageable pageable);

    // Updated: Changed from Movie.Genre to String
    Page<MovieDTO> getMoviesByGenreName(String genreName, Pageable pageable);

    Page<MovieDTO> getUpcomingMovies(Pageable pageable);
}