package io.valentinsoare.moviereviewservice.repository;

import io.valentinsoare.moviereviewservice.document.MovieReview;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MovieReviewRepository extends ReactiveMongoRepository<MovieReview, String> {
    @Query("{ 'movieInfoId' : ?0 }")
    Flux<MovieReview> getReviewsByMovieInfoId(String movieInfoId);

    @Query("{ 'rating' : { $gte : ?0 } }")
    Flux<MovieReview> getAllReviewsByRating(Double rating);

    @Query("{ 'rating' : { $gte : ?0 }, 'movieInfoId' : ?1 }")
    Flux<MovieReview> getAllReviewsByRatingAndMovieInfoId(Double rating, String movieInfoId);

    @Query("{ 'movieInfoId' : ?0 }.count()")
    Mono<Long> countAllReviewsByMovieInfoId(String movieInfoId);
}
