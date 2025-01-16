package io.valentinsoare.movieservice.client;

import io.valentinsoare.movieservice.domain.MovieReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

@Component
public class MovieReviewRestClient {
    private final WebClient webClient;

    @Value("${restClient.movieReviewServiceUrl}")
    private String urlMovieReviewService;

    @Autowired
    public MovieReviewRestClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<MovieReview> getAllReviewsFromMovieInfoId(String movieInfoId) {
        String url = UriComponentsBuilder.fromUriString(urlMovieReviewService)
                .path("/all/id/{movieInfoId}")
                .queryParam("movieInfoId", movieInfoId)
                .buildAndExpand()
                .toUriString();

        return webClient.get()
                .uri(url, movieInfoId)
                .retrieve()
                .bodyToFlux(MovieReview.class);
    }
}
