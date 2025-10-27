package com.thms.controller;

import com.thms.dto.MovieDTO;
import com.thms.dto.ScreeningDTO;
import com.thms.model.Genre;
import com.thms.model.Movie;
import com.thms.repository.GenreRepository;
import com.thms.service.MovieService;
import com.thms.service.ScreeningService;
import com.thms.service.TheatreService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;
    private final TheatreService theatreService;
    private final ScreeningService screeningService;
    private final GenreRepository genreRepository;

    public MovieController(MovieService movieService, TheatreService theatreService,
                           ScreeningService screeningService, GenreRepository genreRepository) {
        this.movieService = movieService;
        this.theatreService = theatreService;
        this.screeningService = screeningService;
        this.genreRepository = genreRepository;
    }

    @GetMapping
    public String getAllMovies(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        List<MovieDTO> movies;

        if (query != null && !query.isEmpty()) {
            movies = movieService.searchMoviesByTitle(query);
            model.addAttribute("searchQuery", query);
        } else if (genre != null && !genre.isEmpty()) {
            movies = movieService.getMoviesByGenreName(genre);
            model.addAttribute("selectedGenre", genre);
        } else {
            movies = movieService.getAllMovies();
        }

        // Get all genres for filter dropdown
        List<Genre> allGenres = genreRepository.findAllOrderedByName();
        model.addAttribute("allGenres", allGenres);
        model.addAttribute("movies", movies);
        model.addAttribute("ratings", Movie.Rating.values());
        model.addAttribute("isAdmin", isAdmin);

        return "movies/list";
    }

    @GetMapping("/{id}")
    public String getMovieDetails(@PathVariable("id") Long id, Model model) {
        MovieDTO movie = movieService.getMovieById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        List<ScreeningDTO> screenings = screeningService.getScreeningsByMovie(id);

        model.addAttribute("movie", movie);
        model.addAttribute("screenings", screenings);

        return "movies/details";
    }
}