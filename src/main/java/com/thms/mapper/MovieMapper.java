package com.thms.mapper;

import com.thms.dto.MovieDTO;
import com.thms.model.Movie;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting between Movie entity and MovieDTO
 */
@Component
public class MovieMapper {

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
        copyFields(movie, movieDTO);
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
        copyFields(movieDTO, movie);
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

        copyFields(movieDTO, movie);
    }

    /**
     * Copy a predefined set of fields from src to dest using reflection so the code
     * does not rely on Lombok-generated getters/setters being present at compile time.
     */
    private void copyFields(Object src, Object dest) {
        String[] fieldNames = new String[] {
            "title",
            "description",
            "durationMinutes",
            "genre",
            "director",
            "cast",
            "releaseDate",
            "posterImageUrl",
            "trailerUrl",
            "rating"
        };

        for (String name : fieldNames) {
            java.lang.reflect.Field sf = findField(src.getClass(), name);
            java.lang.reflect.Field df = findField(dest.getClass(), name);
            if (sf == null || df == null) {
                continue;
            }
            try {
                sf.setAccessible(true);
                df.setAccessible(true);
                Object value = sf.get(src);
                df.set(dest, value);
            } catch (IllegalAccessException ignored) {
                // ignore fields we can't access
            }
        }
    }

    private java.lang.reflect.Field findField(Class<?> cls, String name) {
        Class<?> current = cls;
        while (current != null) {
            try {
                return current.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        return null;
    }
    }
