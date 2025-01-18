package io.valentinsoare.moviereviewservice.controller;

import io.valentinsoare.moviereviewservice.document.MovieReview;
import io.valentinsoare.moviereviewservice.service.MovieReviewService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/movieReviews")
public class MovieReviewController {
    private final MovieReviewService movieReviewService;

    @Autowired
    public MovieReviewController(MovieReviewService movieReviewService) {
        this.movieReviewService = movieReviewService;
    }

    @PostMapping
    public Mono<ResponseEntity<MovieReview>> addMovieReview(@RequestBody @Valid MovieReview movieReview) {
        return movieReviewService.addMovieReview(movieReview)
                .map(m -> new ResponseEntity<>(m, HttpStatus.CREATED));
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
