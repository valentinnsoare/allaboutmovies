package io.valentinsoare.moviereviewservice.integration.repository;

import io.valentinsoare.moviereviewservice.document.MovieReview;
import io.valentinsoare.moviereviewservice.repository.MovieReviewRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
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
                        .movieInfoId("5")
                        .comment("Awesome movie!")
                        .rating(2.3)
                        .build(),
                MovieReview.builder()
                        .reviewId("3")
                        .movieInfoId("2")
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
    void getAllReviewsByRatingAndMovieInfoId() {
        Flux<MovieReview> reviewsWithRatingAndMovieInfoId =
                movieReviewRepository.getAllReviewsByRatingAndMovieInfoId(4.5, "2");

        StepVerifier.create(reviewsWithRatingAndMovieInfoId)
                .expectNextCount(2)
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
    void findAll() {
        Flux<MovieReview> all = movieReviewRepository.findAll();

        StepVerifier.create(all)
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    void findById() {
        Mono<MovieReview> byId = movieReviewRepository.findById("1");

        StepVerifier.create(byId)
                .assertNext(movieReview -> {
                    assert movieReview.getMovieInfoId().equals("1");
                    assert movieReview.getRating() == 4.2;
                    assert movieReview.getComment().equals("Great movie!");
                })
                .verifyComplete();
    }

    @Test
    void save() {
        MovieReview movieReview = MovieReview.builder()
                .reviewId("5")
                .movieInfoId("3")
                .comment("Great movie!")
                .rating(4.2)
                .build();

        Mono<MovieReview> savedMovieReview = movieReviewRepository.save(movieReview);

        StepVerifier.create(savedMovieReview)
                .assertNext(savedReview -> {
                    assert savedReview.getMovieInfoId().equals("3");
                    assert savedReview.getRating() == 4.2;
                    assert savedReview.getComment().equals("Great movie!");
                })
                .expectComplete()
                .verify();

        movieReviewRepository.delete(movieReview).block();
    }

    @Test
    void delete() {
        Mono<MovieReview> deletedReview = movieReviewRepository.findById("1")
                .flatMap(movieReview -> movieReviewRepository.delete(movieReview)
                        .thenReturn(movieReview));

        StepVerifier.create(deletedReview)
                .assertNext(deleted -> {
                    assert deleted.getMovieInfoId().equals("1");
                    assert deleted.getRating() == 4.2;
                    assert deleted.getComment().equals("Great movie!");
                })
                .expectComplete()
                .verify();
    }

    @Test
    void updateMovieReview() {
        MovieReview movieReview = MovieReview.builder()
                .reviewId("1")
                .movieInfoId("1")
                .comment("It was like a dream!")
                .rating(4.99)
                .build();

        Mono<MovieReview> updatedReview = movieReviewRepository.findById("1")
                .flatMap(existingMovieReview -> {
                    existingMovieReview.setReviewId(movieReview.getReviewId());
                    existingMovieReview.setMovieInfoId(movieReview.getMovieInfoId());
                    existingMovieReview.setComment(movieReview.getComment());
                    existingMovieReview.setRating(movieReview.getRating());

                    return movieReviewRepository.save(existingMovieReview);
                });

        StepVerifier.create(updatedReview)
                .assertNext(updated -> {
                    assert updated.getMovieInfoId().equals("1");
                    assert updated.getRating() == 4.99;
                    assert updated.getComment().equals("It was like a dream!");
                })
                .expectComplete()
                .verify();
    }
}
