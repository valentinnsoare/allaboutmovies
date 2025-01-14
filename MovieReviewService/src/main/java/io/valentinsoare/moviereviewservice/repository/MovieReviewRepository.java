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
    Flux<MovieReview> getReviewsByMovieInfoId(Long movieInfoId);

    @Query("{ 'movieInfoId' : ?0, 'reviewId' : ?1 }")
    Mono<MovieReview> findByMovieInfoIdAndReviewId(Long movieInfoId, String reviewId);
}
