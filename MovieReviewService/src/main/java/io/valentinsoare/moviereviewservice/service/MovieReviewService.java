package io.valentinsoare.moviereviewservice.service;

import io.valentinsoare.moviereviewservice.document.MovieReview;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieReviewService {
    Mono<MovieReview> addMovieReview(MovieReview movieReview);
    Mono<MovieReview> getMovieReviewById(String reviewId);
    Flux<MovieReview> getAllReviewsFromAMovieInfoId(String movieInfoId);
    Mono<MovieReview> updateMovieReviewById(String reviewId, MovieReview movieReview);
    Mono<MovieReview> deleteMovieReviewById(String reviewId);
    Flux<MovieReview> getAllMoviesReviewsByRating(Double rating);
    Flux<MovieReview> getAllMoviesReviewsByRatingAndMovieInfoId(Double rating, String movieInfoId);
    Mono<Long> countAllMovieReviewsByMovieInfoId(String movieInfoId);
}
