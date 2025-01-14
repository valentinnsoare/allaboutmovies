package io.valentinsoare.moviereviewservice.unit.controller;

import io.valentinsoare.moviereviewservice.controller.MovieReviewController;
import io.valentinsoare.moviereviewservice.document.MovieReview;
import io.valentinsoare.moviereviewservice.service.MovieReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@AutoConfigureWebTestClient
@WebFluxTest(controllers = MovieReviewController.class)
public class MovieReviewControllerUnitTest {
    private final WebTestClient webTestClient;

    @MockitoBean
    private MovieReviewService movieReviewServiceMock;

    @Autowired
    public MovieReviewControllerUnitTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    @Test
    void addMovieReview() {
        String movieInfoId = "1";

        MovieReview movieReview = MovieReview.builder()
                .movieInfoId(movieInfoId)
                .comment("Great movie!")
                .rating(5.0)
                .reviewId("1")
                .build();

        when(movieReviewServiceMock.addMovieReview(isA(MovieReview.class)))
                .thenReturn(Mono.just(movieReview));

        webTestClient.post()
                .uri("/api/v1/movieReviews")
                .bodyValue(movieReview)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(MovieReview.class)
                .isEqualTo(movieReview);
    }

    @Test
    void getMovieReviewById() {
        String reviewId = "2";

        MovieReview movieReview = MovieReview.builder()
                .movieInfoId(reviewId)
                .comment("Excellent!")
                .rating(4.2)
                .reviewId(reviewId)
                .build();

        when(movieReviewServiceMock.getMovieReviewById(isA(String.class)))
                .thenReturn(Mono.just(movieReview));

        webTestClient.get()
                .uri(String.format("/api/v1/movieReviews/id/%s", reviewId))
                .exchange()
                .expectStatus().isOk()
                .expectBody(MovieReview.class)
                .isEqualTo(movieReview);
    }

    @Test
    void updateMovieReviewById() {
        String reviewId = "3";

        MovieReview movieReview = MovieReview.builder()
                .movieInfoId(reviewId)
                .comment("Good movie!")
                .rating(3.5)
                .reviewId(reviewId)
                .build();

        when(movieReviewServiceMock.updateMovieReviewById(isA(String.class), isA(MovieReview.class)))
                .thenReturn(Mono.just(movieReview));

        webTestClient.put()
                .uri(String.format("/api/v1/movieReviews/id/%s", reviewId))
                .bodyValue(movieReview)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MovieReview.class)
                .isEqualTo(movieReview);
    }

    @Test
    void deleteMovieReviewById() {
        String reviewId = "4";

        MovieReview movieReview = MovieReview.builder()
                .movieInfoId(reviewId)
                .comment("Not bad!")
                .rating(2.5)
                .reviewId(reviewId)
                .build();


        when(movieReviewServiceMock.deleteMovieReviewById(isA(String.class)))
                .thenReturn(
                        Mono.just(movieReview)
                );

        webTestClient.delete()
                .uri(String.format("/api/v1/movieReviews/id/%s", reviewId))
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void getAllMovieReviewsByRating() {
        Double rating = 4.0;

        MovieReview m = MovieReview.builder()
                .movieInfoId("44")
                .comment("Great movie!")
                .rating(rating)
                .reviewId("1")
                .build();

        MovieReview n = MovieReview.builder()
                .movieInfoId("44")
                .comment("Great movie!")
                .rating(rating)
                .reviewId("2")
                .build();

        when(movieReviewServiceMock.getAllMoviesReviewsByRating(isA(Double.class)))
                .thenReturn(Flux.just(m, n));

        webTestClient.get()
                .uri(String.format("/api/v1/movieReviews/rating/%s", rating))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(MovieReview.class)
                .hasSize(2);
    }

   @Test
   void getAllReviewsFromAMovieInfoId() {
         String movieInfoId = "44";

         MovieReview m = MovieReview.builder()
                .movieInfoId(movieInfoId)
                .comment("Great movie!")
                .rating(4.0)
                .reviewId("1")
                .build();

         MovieReview n = MovieReview.builder()
                .movieInfoId(movieInfoId)
                .comment("Not so great!")
                .rating(4.5)
                .reviewId("2")
                .build();

         when(movieReviewServiceMock.getAllReviewsFromAMovieInfoId(isA(String.class)))
                .thenReturn(Flux.just(m, n));

         webTestClient.get()
                .uri(String.format("/api/v1/movieReviews/all/id/%s", movieInfoId))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(MovieReview.class)
                .hasSize(2);
   }

   @Test
   void countAllMovieReviewsByMovieInfoId() {
         String movieInfoId = "44";

         when(movieReviewServiceMock.countAllMovieReviewsByMovieInfoId(isA(String.class)))
                .thenReturn(Mono.just(2L));

         webTestClient.get()
                .uri(String.format("/api/v1/movieReviews//count/%s", movieInfoId))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Long.class)
                .isEqualTo(2L);
   }

   @Test
   void getAllMoviesReviewsByRatingAndMovieInfoId() {
            Double rating = 4.0;
            String movieInfoId = "44";

            MovieReview m = MovieReview.builder()
                    .movieInfoId(movieInfoId)
                    .comment("Great movie!")
                    .rating(rating)
                    .reviewId("1")
                    .build();

            MovieReview n = MovieReview.builder()
                    .movieInfoId(movieInfoId)
                    .comment("Not so great!")
                    .rating(rating)
                    .reviewId("2")
                    .build();

            when(movieReviewServiceMock.getAllMoviesReviewsByRatingAndMovieInfoId(isA(Double.class), isA(String.class)))
                    .thenReturn(Flux.just(m, n));

            webTestClient.get()
                    .uri(String.format("/api/v1/movieReviews/rating/%s/id/%s", rating, movieInfoId))
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBodyList(MovieReview.class)
                    .hasSize(2);
   }
}
