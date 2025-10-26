package com.thms.controller;

import com.thms.dto.ApiResponse;
import com.thms.model.Genre;
import com.thms.service.GenreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    // CREATE
    @PostMapping
    public ResponseEntity<ApiResponse<Genre>> createGenre(@Valid @RequestBody Genre genre) {
        Genre created = genreService.createGenre(genre);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Genre created successfully"));
    }

    // READ - All
    @GetMapping
    public ResponseEntity<ApiResponse<List<Genre>>> getAllGenres() {
        List<Genre> genres = genreService.getAllGenres();
        return ResponseEntity.ok(ApiResponse.success(genres));
    }

    // READ - Paginated with sorting
    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<Page<Genre>>> getAllGenresPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Genre> genres = genreService.getAllGenresPaginated(pageable);
        return ResponseEntity.ok(ApiResponse.success(genres));
    }

    // READ - By ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Genre>> getGenreById(@PathVariable Long id) {
        Genre genre = genreService.getGenreById(id);
        return ResponseEntity.ok(ApiResponse.success(genre));
    }

    // READ - By Name
    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<Genre>> getGenreByName(@PathVariable String name) {
        Genre genre = genreService.getGenreByName(name);
        return ResponseEntity.ok(ApiResponse.success(genre));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Genre>> updateGenre(
            @PathVariable Long id,
            @Valid @RequestBody Genre genre) {
        Genre updated = genreService.updateGenre(id, genre);
        return ResponseEntity.ok(ApiResponse.success(updated, "Genre updated successfully"));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Genre deleted successfully"));
    }

    // Custom query endpoint - Popular genres
    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<List<Genre>>> getPopularGenres(
            @RequestParam(defaultValue = "5") int minimumMovies) {
        List<Genre> genres = genreService.getPopularGenres(minimumMovies);
        return ResponseEntity.ok(ApiResponse.success(genres));
    }
}