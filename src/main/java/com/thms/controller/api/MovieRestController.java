package com.thms.controller.api;

import com.thms.dto.ApiResponse;
import com.thms.dto.MovieDTO;
import com.thms.dto.ScreeningDTO;
import com.thms.exception.ResourceNotFoundException;
import com.thms.model.Genre;
import com.thms.model.Movie;
import com.thms.repository.GenreRepository;
import com.thms.service.MovieService;
import com.thms.service.ScreeningService;
import com.thms.service.TheatreService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/movies")
public class MovieRestController {

    private final MovieService movieService;
    private final TheatreService theatreService;
    private final ScreeningService screeningService;
    private final GenreRepository genreRepository;

    public MovieRestController(MovieService movieService, TheatreService theatreService,
                               ScreeningService screeningService, GenreRepository genreRepository) {
        this.movieService = movieService;
        this.theatreService = theatreService;
        this.screeningService = screeningService;
        this.genreRepository = genreRepository;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MovieDTO>>> getAllMovies(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<MovieDTO> movies;

        if (query != null && !query.isEmpty()) {
            movies = movieService.searchMoviesByTitle(query);
        } else if (genre != null && !genre.isEmpty()) {
            movies = movieService.getMoviesByGenreName(genre);
        } else {
            movies = movieService.getAllMovies();
        }

        return ResponseEntity.ok(ApiResponse.success(movies));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MovieDTO>> getMovieById(@PathVariable("id") Long id) {
        MovieDTO movie = movieService.getMovieById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
        return ResponseEntity.ok(ApiResponse.success(movie));
    }

    @GetMapping("/{id}/screenings")
    public ResponseEntity<ApiResponse<List<ScreeningDTO>>> getMovieScreenings(
            @PathVariable("id") Long id,
            @RequestParam(value = "days", defaultValue = "7") int days) {

        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.plusDays(days);

        List<ScreeningDTO> screenings = screeningService.getScreeningsByMovie(id)
                .stream()
                .filter(s -> s.getStartTime().isAfter(startDateTime) && s.getStartTime().isBefore(endDateTime))
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(screenings));
    }

    @GetMapping("/{id}/screenings/grouped")
    public ResponseEntity<ApiResponse<Map<LocalDate, List<ScreeningDTO>>>> getMovieScreeningsGrouped(
            @PathVariable("id") Long id,
            @RequestParam(value = "days", defaultValue = "7") int days) {

        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.plusDays(days);

        List<ScreeningDTO> screenings = screeningService.getScreeningsByMovie(id)
                .stream()
                .filter(s -> s.getStartTime().isAfter(startDateTime) && s.getStartTime().isBefore(endDateTime))
                .collect(Collectors.toList());

        Map<LocalDate, List<ScreeningDTO>> screeningsByDate = screenings.stream()
                .collect(Collectors.groupingBy(s -> s.getStartTime().toLocalDate()));

        return ResponseEntity.ok(ApiResponse.success(screeningsByDate));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<MovieDTO>>> searchMovies(@RequestParam(value = "query") String query) {
        List<MovieDTO> movies = movieService.searchMoviesByTitle(query);
        return ResponseEntity.ok(ApiResponse.success(movies));
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<ApiResponse<List<MovieDTO>>> getMoviesByGenre(@PathVariable("genre") String genre) {
        List<MovieDTO> movies = movieService.getMoviesByGenreName(genre);
        return ResponseEntity.ok(ApiResponse.success(movies));
    }

    @GetMapping("/genres")
    public ResponseEntity<ApiResponse<List<String>>> getAllGenres() {
        List<String> genres = genreRepository.findAllOrderedByName()
                .stream()
                .map(Genre::getName)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(genres));
    }

    @GetMapping("/ratings")
    public ResponseEntity<ApiResponse<List<String>>> getAllRatings() {
        List<String> ratings = java.util.Arrays.stream(Movie.Rating.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(ratings));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<ApiResponse<List<MovieDTO>>> getUpcomingMovies() {
        List<MovieDTO> movies = movieService.getUpcomingMovies();
        return ResponseEntity.ok(ApiResponse.success(movies));
    }
}