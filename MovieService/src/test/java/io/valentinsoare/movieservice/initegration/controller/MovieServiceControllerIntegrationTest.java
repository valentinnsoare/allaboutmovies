package io.valentinsoare.movieservice.initegration.controller;

import io.valentinsoare.movieservice.domain.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@AutoConfigureWebTestClient
@AutoConfigureWireMock(port = 8084)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "restClient.movieInfoServiceUrl=http://localhost:8084/api/v1/movieInfos",
        "restClient.movieReviewServiceUrl=http://localhost:8084/api/v1/movieReviews"
})
public class MovieServiceControllerIntegrationTest {

    private WebTestClient webTestClient;

    @Autowired
    public MovieServiceControllerIntegrationTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    @Test
    void getMovieById() {
        String idOfTheMovie = "1";

        stubFor(get(urlEqualTo(String.format("/api/v1/movieInfos/id/%s", idOfTheMovie)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("movieinfo.json")
                ));

        stubFor(get(urlEqualTo(String.format("/api/v1/movieReviews/all/id/%s", idOfTheMovie)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("movieReviews.json")
                ));

        webTestClient.get()
                .uri(String.format("/api/v1/movies/id/%s", idOfTheMovie))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Movie.class)
                .consumeWith(movieEntityExchangeResult -> {

                    Movie movie = movieEntityExchangeResult.getResponseBody();

                    assert movie != null;
                    assert movie.getMovieInfo().getId().equals(idOfTheMovie);
                    assert movie.getReviews().size() == 2;
                });
    }

    @Test
    void getMovieByIdNotFound() {
        String idOfTheMovie = "2";

        stubFor(get(urlEqualTo(String.format("/api/v1/movieInfos/id/%s", idOfTheMovie)))
                .willReturn(aResponse()
                        .withStatus(404)
                ));

        webTestClient.get()
                .uri(String.format("/api/v1/movies/id/%s", idOfTheMovie))
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void getMovieByIdReviewsNotFound() {
        String idOfTheMovie = "1";

        stubFor(get(urlEqualTo(String.format("/api/v1/movieInfos/id/%s", idOfTheMovie)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("movieinfo.json")
                ));

        stubFor(get(urlEqualTo(String.format("/api/v1/movieReviews/all/id/%s", idOfTheMovie)))
                .willReturn(aResponse()
                        .withStatus(404)
                ));

        webTestClient.get()
                .uri(String.format("/api/v1/movies/id/%s", idOfTheMovie))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Movie.class)
                .consumeWith(movieEntityExchangeResult -> {
                    Movie movie = movieEntityExchangeResult.getResponseBody();

                    assert movie != null;
                    assert movie.getMovieInfo().getId().equals(idOfTheMovie);
                    assert movie.getReviews().isEmpty();
                });
    }
}
