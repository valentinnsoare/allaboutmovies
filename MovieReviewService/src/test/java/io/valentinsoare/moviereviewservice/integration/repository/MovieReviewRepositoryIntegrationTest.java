package io.valentinsoare.moviereviewservice.integration.repository;

import io.valentinsoare.moviereviewservice.document.MovieReview;
import io.valentinsoare.moviereviewservice.repository.MovieReviewRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

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
                        .rating(3.8)
                        .build()
        );

        movieReviewRepository.saveAll(movieReviews).blockLast();
    }

    @AfterEach
    void tearDown() {
        movieReviewRepository.deleteAll().block();
    }
}
