package io.valentinsoare.movieservice.controller;

import io.valentinsoare.movieservice.client.MovieInfoRestClient;
import io.valentinsoare.movieservice.client.MovieReviewRestClient;
import io.valentinsoare.movieservice.domain.Movie;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieServiceController {
    private final MovieInfoRestClient movieInfoRestClient;
    private final MovieReviewRestClient movieReviewRestClient;

    @Autowired
    public MovieServiceController(MovieInfoRestClient movieInfoRestClient,
                                  MovieReviewRestClient movieReviewRestClient
    ) {
        this.movieInfoRestClient = movieInfoRestClient;
        this.movieReviewRestClient = movieReviewRestClient;
    }

    @GetMapping("/id/{movieId}")
    public Mono<Movie> getMovieById(@PathVariable @NotNull String movieId) {
        return movieInfoRestClient.getMovieInfo(movieId)
                .flatMap(movieInfo -> movieReviewRestClient.getAllReviewsFromMovieInfoId(movieId)
                        .collectList()
                        .map(movieReviews -> new Movie(movieInfo, movieReviews))
                );
    }
}
