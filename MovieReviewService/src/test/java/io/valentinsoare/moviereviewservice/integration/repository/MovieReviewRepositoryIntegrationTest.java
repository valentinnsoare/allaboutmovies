package io.valentinsoare.moviereviewservice.integration.repository;

import io.valentinsoare.moviereviewservice.document.MovieReview;
import io.valentinsoare.moviereviewservice.repository.MovieReviewRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
public class MovieReviewRepositoryIntegrationTest {
    private final MovieReviewRepository movieReviewRepository;

    @Autowired
    public MovieReviewRepositoryIntegrationTest(MovieReviewRepository movieReviewRepository) {
        this.movieReviewRepository = movieReviewRepository;
    }

    @BeforeEach
    void setUp() {
        List<MovieReview> movieReviews = Arrays.asList(
                MovieReview.builder()
                        .reviewId("1")
                        .movieInfoId("1")
                        .comment("Great movie!")
                        .rating(4.2)
                        .build(),
                MovieReview.builder()
                        .reviewId("2")
                        .movieInfoId("2")
                        .comment("Awesome movie!")
                        .rating(4.5)
                        .build(),
                MovieReview.builder()
                        .reviewId("3")
                        .movieInfoId("3")
                        .comment("Not good, terrible movie!")
                        .rating(4.5)
                        .build(),
                MovieReview.builder()
                        .reviewId("4")
                        .movieInfoId("2")
                        .comment("Next level dude!!")
                        .rating(4.5)
                        .build()
        );

        movieReviewRepository.saveAll(movieReviews).blockLast();
    }

    @AfterEach
    void tearDown() {
        movieReviewRepository.deleteAll().block();
    }

    @Test
    void getReviewsByMovieInfoId() {
        Flux<MovieReview> reviewsForAnId = movieReviewRepository.getReviewsByMovieInfoId("1");

        StepVerifier.create(reviewsForAnId)
                .consumeNextWith(movieReview -> {
                    assert movieReview.getMovieInfoId().equals("1");
                    assert movieReview.getRating() == 4.2;
                    assert movieReview.getComment().equals("Great movie!");
                })
                .verifyComplete();
    }

    @Test
    void getAllReviewsByRating() {
        Flux<MovieReview> reviewsWithRating = movieReviewRepository.getAllReviewsByRating(4.5);

        StepVerifier.create(reviewsWithRating)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void getAllReviewsByRatingAndMovieInfoId() {
        Flux<MovieReview> reviewsWithRatingAndMovieInfoId =
                movieReviewRepository.getAllReviewsByRatingAndMovieInfoId(4.5, "2");

        StepVerifier.create(reviewsWithRatingAndMovieInfoId)
                .expectNextCount(2)
                .verifyComplete();
    }
}
