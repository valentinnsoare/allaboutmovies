package io.valentinsoare.movieservice.client;

import io.valentinsoare.movieservice.domain.MovieReview;
import io.valentinsoare.movieservice.exception.MovieReviewClientException;
import io.valentinsoare.movieservice.exception.MovieReviewServerException;
import io.valentinsoare.movieservice.util.RetryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
                .path(String.format("/all/id/%s", movieInfoId))
                .buildAndExpand()
                .toUriString();

        return webClient.get()
                .uri(url, movieInfoId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    if (clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
                        return Mono.empty();
                    }

                    return clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> Mono.error(new MovieReviewClientException(
                                    errorBody, clientResponse.statusCode().value())));
                })
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new MovieReviewServerException(
                                String.format("Server exception in MovieReviewService: %s",
                                        errorBody)
                        )))
                )
                .bodyToFlux(MovieReview.class)
                .retryWhen(RetryUtil.retrySpec());
    }

    public Mono<MovieReview> addMovieReview(MovieReview movieReview) {
        String url = urlMovieReviewService;

        return webClient.post()
                .uri(url)
                .body(Mono.just(movieReview), MovieReview.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new MovieReviewClientException(
                                errorBody, clientResponse.statusCode().value())))
                )
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new MovieReviewServerException(
                                String.format("Server exception in MovieReviewService: %s",
                                        errorBody)
                        )))
                )
                .bodyToMono(MovieReview.class)
                .retryWhen(RetryUtil.retrySpec());
    }

    public Mono<String> deleteAllReviewsByMovieInfoId(String movieInfoId) {
        String url = UriComponentsBuilder.fromUriString(urlMovieReviewService)
                .path(String.format("/all/%s", movieInfoId))
                .buildAndExpand()
                .toUriString();

        return webClient.delete()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new MovieReviewClientException(
                                errorBody, clientResponse.statusCode().value())))
                )
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new MovieReviewServerException(
                                String.format("Server exception in MovieReviewService: %s",
                                        errorBody)
                        )))
                )
                .bodyToMono(String.class)
                .retryWhen(RetryUtil.retrySpec());
    }

    private Flux<MovieReview> getMovieReviewFlux(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new MovieReviewClientException(
                                errorBody, clientResponse.statusCode().value())))
                )
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new MovieReviewServerException(
                                String.format("Server exception in MovieReviewService: %s",
                                        errorBody)
                        )))
                )
                .bodyToFlux(MovieReview.class)
                .retryWhen(RetryUtil.retrySpec());
    }

    public Flux<MovieReview> getStreamMovieReviews() {
        String url = urlMovieReviewService.concat("/stream");
        return getMovieReviewFlux(url);
    }

    public Flux<MovieReview> getStreamMovieReviewsByMovieInfoIdIfUpdated(String movieInfoId) {
        String url = UriComponentsBuilder.fromUriString(urlMovieReviewService)
                .path(String.format("/stream/id/%s", movieInfoId))
                .buildAndExpand()
                .toUriString();

        return getMovieReviewFlux(url);
    }
}
