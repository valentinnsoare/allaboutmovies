package io.valentinsoare.moviereviewservice.integration.controller;

import io.valentinsoare.moviereviewservice.document.MovieReview;
import io.valentinsoare.moviereviewservice.repository.MovieReviewRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Arrays;
import java.util.List;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MovieReviewControllerIntegrationTest {
    private final MovieReviewRepository movieReviewRepository;
    private final WebTestClient webTestClient;

    @Autowired
    public MovieReviewControllerIntegrationTest(
            MovieReviewRepository movieReviewRepository,
            WebTestClient webTestClient
    ) {
        this.movieReviewRepository = movieReviewRepository;
        this.webTestClient = webTestClient;
    }

    @BeforeEach
    void setUp() {
        List<MovieReview> reviews = Arrays.asList(
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

        movieReviewRepository.saveAll(reviews).blockLast();
    }

    @AfterEach
    void tearDown() {
        movieReviewRepository.deleteAll().block();
    }

    @Test
    void addMovieReview() {
        MovieReview movieReview = MovieReview.builder()
                .reviewId("5")
                .movieInfoId("3")
                .comment("Great movie!")
                .rating(4.2)
                .build();

        webTestClient.post()
                .uri("/api/v1/movieReviews")
                .bodyValue(movieReview)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieReview.class)
                .isEqualTo(movieReview);
    }

    @Test
    void getMovieReviewById() {
        String id = "1";

        webTestClient.get()
                .uri(String.format("/api/v1/movieReviews/id/%s", id))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(MovieReview.class)
                .value(movieReview -> {
                    assert movieReview != null;
                    assert movieReview.getReviewId().equals(id);
                });
    }

    @Test
    void updateMovieReviewById() {
        String id = "1";

        MovieReview mr = MovieReview.builder()
                .reviewId(id)
                .movieInfoId("99")
                .comment("The Shit!")
                .rating(4.99)
                .build();

        webTestClient.put()
                .uri(String.format("/api/v1/movieReviews/id/%s", id))
                .bodyValue(mr)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(MovieReview.class)
                .value(m -> {
                    assert m.getReviewId().equals(id);
                    assert m.getRating().equals(mr.getRating());
                    assert m.getMovieInfoId().equals(mr.getMovieInfoId());
                });
    }

    @Test
    void deleteMovieReviewById() {
        String movieReviewId = "3";

        webTestClient.delete()
                .uri(String.format("/api/v1/movieReviews/id/%s", movieReviewId))
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void getAllMovieReviewsByRating() {
        double rating = 4.5;

        webTestClient.get()
            .uri(String.format("/api/v1/movieReviews//rating/%s", rating))
            .exchange()
            .expectStatus()
            .isOk()
            .expectBodyList(MovieReview.class)
            .value(movieReviews -> {
                assert movieReviews != null;
                assert movieReviews.size() == 2;
                assert movieReviews.stream().allMatch(review -> review.getRating() == rating);
            });
    }
}
