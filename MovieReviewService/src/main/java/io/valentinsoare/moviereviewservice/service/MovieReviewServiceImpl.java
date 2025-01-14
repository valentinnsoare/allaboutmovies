package io.valentinsoare.moviereviewservice.service;

import io.valentinsoare.moviereviewservice.document.MovieReview;
import io.valentinsoare.moviereviewservice.repository.MovieReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
        return movieReviewRepository.findById(reviewId);
    }

    @Override
    public Flux<MovieReview> getAllReviewsFromAMovieInfoId(String movieInfoId) {
        return null;
    }

    @Override
    @Transactional
    public Mono<MovieReview> updateMovieReviewById(String reviewId, MovieReview movieReview) {
        return movieReviewRepository.findById(reviewId)
                .flatMap(existingMovieReview -> {
                    existingMovieReview.setReviewId(movieReview.getReviewId());
                    existingMovieReview.setMovieInfoId(movieReview.getMovieInfoId());
                    existingMovieReview.setComment(movieReview.getComment());
                    existingMovieReview.setRating(movieReview.getRating());

                    return movieReviewRepository.save(existingMovieReview);
                });
    }

    @Override
    public Mono<MovieReview> deleteMovieReviewById(String reviewId) {
        return null;
    }

    @Override
    public Flux<MovieReview> getAllMoviesReviewsByRating(Double rating) {
        return null;
    }

    @Override
    public Mono<Long> countAllMovieReviewsByMovieInfoId(String movieInfoId) {
        return null;
    }
}
