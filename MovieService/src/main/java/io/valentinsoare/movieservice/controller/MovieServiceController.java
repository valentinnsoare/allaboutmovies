package io.valentinsoare.movieservice.controller;

import io.valentinsoare.movieservice.client.MovieInfoRestClient;
import io.valentinsoare.movieservice.client.MovieReviewRestClient;
import io.valentinsoare.movieservice.domain.Movie;
import io.valentinsoare.movieservice.domain.MovieInfo;
import io.valentinsoare.movieservice.domain.MovieReview;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
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

    @GetMapping(value = "/stream/movieinfos", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<MovieInfo> getStreamMovieInfos() {
        return movieInfoRestClient.getStreamMovieInfos();
    }

    @GetMapping(value = "/stream/movieInfos/id/{movieInfoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<MovieInfo> getStreamMovieInfoByIdIfUpdated(@PathVariable @NotNull String movieInfoId) {
        return movieInfoRestClient.getStreamMovieInfosByIdIfUpdated(movieInfoId);
    }

    @GetMapping(value = "/stream/moviereviews", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<MovieReview> getStreamMovieReviews() {
        return movieReviewRestClient.getStreamMovieReviews();
    }

    @GetMapping(value = "/stream/moviereviews/id/{movieInfoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<MovieReview> getStreamMovieReviewsByMovieInfoIdIfUpdated(@PathVariable @NotNull String movieInfoId) {
        return movieReviewRestClient.getStreamMovieReviewsByMovieInfoIdIfUpdated(movieInfoId);
    }

    @PostMapping
    public Mono<Movie> addMovie(@RequestBody @Valid Movie movie) {
        return movieInfoRestClient.addMovieInfo(movie.getMovieInfo())
                .flatMap(movieInfo -> {
                    movie.setMovieInfo(movieInfo);

                    return Flux.fromIterable(movie.getReviews())
                            .flatMap(movieReviewRestClient::addMovieReview)
                            .collectList()
                            .map(movieReviews -> new Movie(movieInfo, movieReviews));
                });
    }

    @DeleteMapping("/id/{movieId}")
    public Mono<String> deleteMovieById(@PathVariable @NotNull String movieId) {
        return movieInfoRestClient.deleteMovieInfoById(movieId)
                .then(movieReviewRestClient.deleteAllReviewsByMovieInfoId(movieId));
    }
}
