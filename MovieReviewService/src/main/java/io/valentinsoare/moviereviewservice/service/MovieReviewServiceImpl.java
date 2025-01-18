package io.valentinsoare.moviereviewservice.service;

import io.valentinsoare.moviereviewservice.document.MovieReview;
import io.valentinsoare.moviereviewservice.exception.MovieReviewNotFoundException;
import io.valentinsoare.moviereviewservice.exception.NoMovieReviewException;
import io.valentinsoare.moviereviewservice.repository.MovieReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class MovieReviewServiceImpl implements MovieReviewService {
    private final MovieReviewRepository movieReviewRepository;

    @Autowired
    public MovieReviewServiceImpl(MovieReviewRepository movieReviewRepository) {
        this.movieReviewRepository = movieReviewRepository;
    }

    @Override
    @Transactional
    public Mono<MovieReview> addMovieReview(MovieReview movieReview) {
        return movieReviewRepository.save(movieReview);
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<MovieReview> getMovieReviewById(String reviewId) {
        return movieReviewRepository.findById(reviewId)
                .switchIfEmpty(
                        Mono.error(new MovieReviewNotFoundException("movieReview", Map.of("reviewId", reviewId)))
                );
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<MovieReview> getAllReviewsFromAMovieInfoId(String movieInfoId) {
        return movieReviewRepository.getReviewsByMovieInfoId(movieInfoId)
                .switchIfEmpty(
                        Flux.error(
                                new MovieReviewNotFoundException("movieReview", Map.of("movieInfoId", movieInfoId)))
                );
    }

    @Override
    @Transactional
    public Mono<MovieReview> updateMovieReviewById(String reviewId, MovieReview movieReview) {
        return movieReviewRepository.findById(reviewId)
                .switchIfEmpty(
                        Mono.error(new MovieReviewNotFoundException("movieReview", Map.of("reviewId", reviewId))))
                .flatMap(existingMovieReview -> {
                    existingMovieReview.setReviewId(movieReview.getReviewId());
                    existingMovieReview.setMovieInfoId(movieReview.getMovieInfoId());
                    existingMovieReview.setComment(movieReview.getComment());
                    existingMovieReview.setRating(movieReview.getRating());

                    return movieReviewRepository.save(existingMovieReview);
                });
    }

    @Override
    @Transactional
    public Mono<MovieReview> deleteMovieReviewById(String reviewId) {
        return movieReviewRepository.findById(reviewId)
                .switchIfEmpty(
                        Mono.error(new MovieReviewNotFoundException("movieReview", Map.of("reviewId", reviewId))))
                .flatMap(existingMovieReview -> movieReviewRepository.delete(existingMovieReview)
                        .thenReturn(existingMovieReview));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<MovieReview> getAllMoviesReviewsByRating(Double rating) {
        return movieReviewRepository.getAllReviewsByRating(rating)
                .switchIfEmpty(
                        Flux.error(new NoMovieReviewException("movieReview"))
                );
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<MovieReview> getAllMoviesReviewsByRatingAndMovieInfoId(Double rating, String movieInfoId) {
        return movieReviewRepository.getAllReviewsByRatingAndMovieInfoId(rating, movieInfoId)
                .switchIfEmpty(
                        Flux.error(new NoMovieReviewException("movieReview"))
                );
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Long> countAllMovieReviewsByMovieInfoId(String movieInfoId) {
        return movieReviewRepository.countAllReviewsByMovieInfoId(movieInfoId)
                .switchIfEmpty(
                        Mono.error(new NoMovieReviewException("movieReview"))
                );
    }
}
