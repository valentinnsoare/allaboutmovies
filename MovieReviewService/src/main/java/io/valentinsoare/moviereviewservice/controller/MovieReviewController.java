package io.valentinsoare.moviereviewservice.controller;

import io.valentinsoare.moviereviewservice.document.MovieReview;
import io.valentinsoare.moviereviewservice.service.MovieReviewService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@RestController
@RequestMapping("/api/v1/movieReviews")
public class MovieReviewController {
    private final MovieReviewService movieReviewService;
    private final Sinks.Many<MovieReview> movieReviewSink;

    @Autowired
    public MovieReviewController(MovieReviewService movieReviewService) {
        this.movieReviewService = movieReviewService;
        this.movieReviewSink = Sinks.many().multicast().onBackpressureBuffer();
    }

    @PostMapping
    public Mono<ResponseEntity<MovieReview>> addMovieReview(@RequestBody @Valid MovieReview movieReview) {
        return movieReviewService.addMovieReview(movieReview)
                .doOnNext(movieReviewSink::tryEmitNext)
                .map(m -> new ResponseEntity<>(m, HttpStatus.CREATED));
    }

    @GetMapping(value = "/stream", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<MovieReview> getStreamMovieReviews() {
        return movieReviewSink.asFlux();
    }

    @GetMapping(value = "/stream/id/{movieInfoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<MovieReview> getStreamMovieReviewsByMovieInfoId(@PathVariable @NotNull String movieInfoId) {
        return movieReviewSink.asFlux()
                .filter(m -> m.getMovieInfoId().equals(movieInfoId));
    }

    @GetMapping("/id/{reviewId}")
    public Mono<ResponseEntity<MovieReview>> getMovieReviewById(@PathVariable @NotNull String reviewId) {
        return movieReviewService.getMovieReviewById(reviewId)
                .map(m -> new ResponseEntity<>(m, HttpStatus.OK));
    }

    @PutMapping("/id/{reviewId}")
    public Mono<ResponseEntity<MovieReview>> updateMovieReviewById(
            @PathVariable @NotNull String reviewId,
            @RequestBody @Valid MovieReview movieReview
    ) {
        return movieReviewService.updateMovieReviewById(reviewId, movieReview)
                .map(m -> new ResponseEntity<>(m, HttpStatus.OK));
    }

    @DeleteMapping("/id/{reviewId}")
    public Mono<ResponseEntity<String>> deleteMovieReviewById(@PathVariable @NotNull String reviewId) {
        return movieReviewService.deleteMovieReviewById(reviewId)
                .map(m -> new ResponseEntity<>(
                        String.format("Movie review with id %s deleted successfully!", reviewId), HttpStatus.OK)
                );
    }

    @DeleteMapping("/all/{movieInfoId}")
    public Mono<ResponseEntity<String>> deleteAllMovieReviewsByMovieInfoId(@PathVariable @NotNull String movieInfoId) {
        return movieReviewService.deleteAllMovieReviewsByMovieInfoId(movieInfoId)
                .map(m -> new ResponseEntity<>(
                        String.format("All movie reviews with movieInfoId %s deleted successfully!", movieInfoId), HttpStatus.OK)
                );
    }

    @GetMapping("/rating/{rating}")
    public Flux<MovieReview> getAllMovieReviewsByRating(@PathVariable @NotNull Double rating) {
        return movieReviewService.getAllMoviesReviewsByRating(rating);
    }

    @GetMapping("/all/id/{movieInfoId}")
    public Flux<MovieReview> getAllMovieReviewsFromAMovieInfoId(@PathVariable @NotNull String movieInfoId) {
        return movieReviewService.getAllReviewsFromAMovieInfoId(movieInfoId);
    }

    @GetMapping("/count/{movieInfoId}")
    public Mono<ResponseEntity<Long>> countAllMovieReviewsByMovieInfoId(@PathVariable @NotNull String movieInfoId) {
        return movieReviewService.countAllMovieReviewsByMovieInfoId(movieInfoId)
                .map(count -> new ResponseEntity<>(count, HttpStatus.OK));
    }

    @GetMapping("/rating/{rating}/id/{movieInfoId}")
    public Flux<MovieReview> getAllMoviesReviewsByRatingAndMovieInfoId(
            @PathVariable @NotNull Double rating,
            @PathVariable @NotNull String movieInfoId
    ) {
        return movieReviewService.getAllMoviesReviewsByRatingAndMovieInfoId(rating, movieInfoId);
    }
}
