package com.thms.service;

import com.thms.exception.ResourceNotFoundException;
import com.thms.model.Genre;
import com.thms.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GenreService {

    private final GenreRepository genreRepository;

    // CREATE
    public Genre createGenre(Genre genre) {
        if (genreRepository.existsByNameIgnoreCase(genre.getName())) {
            throw new IllegalStateException("Genre with name '" + genre.getName() + "' already exists");
        }
        return genreRepository.save(genre);
    }

    // READ - All
    public List<Genre> getAllGenres() {
        return genreRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    // READ - Paginated
    public Page<Genre> getAllGenresPaginated(Pageable pageable) {
        return genreRepository.findAll(pageable);
    }

    // READ - By ID
    public Genre getGenreById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + id));
    }

    // READ - By Name
    public Genre getGenreByName(String name) {
        return genreRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with name: " + name));
    }

    // UPDATE
    public Genre updateGenre(Long id, Genre genreDetails) {
        Genre genre = getGenreById(id);

        // Check if new name conflicts with existing genre
        if (!genre.getName().equalsIgnoreCase(genreDetails.getName()) &&
                genreRepository.existsByNameIgnoreCase(genreDetails.getName())) {
            throw new IllegalStateException("Genre with name '" + genreDetails.getName() + "' already exists");
        }

        genre.setName(genreDetails.getName());
        genre.setDescription(genreDetails.getDescription());

        return genreRepository.save(genre);
    }

    // DELETE
    public void deleteGenre(Long id) {
        Genre genre = getGenreById(id);
        genreRepository.delete(genre);
    }

    // Demonstrate custom query
    public List<Genre> getPopularGenres(int minimumMovies) {
        return genreRepository.findGenresWithMoreThanNMovies(minimumMovies);
    }
}