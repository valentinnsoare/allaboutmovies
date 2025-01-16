package io.valentinsoare.movieservice.controller;

import io.valentinsoare.movieservice.domain.Movie;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieServiceController {

    @GetMapping("/id/{movieId}")
    public Mono<Movie> getMovieById(@PathVariable @NotNull String movieId) {
        return null;
    }
}
