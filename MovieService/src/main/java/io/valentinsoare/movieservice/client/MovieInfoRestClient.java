package io.valentinsoare.movieservice.client;

import io.valentinsoare.movieservice.domain.MovieInfo;
import io.valentinsoare.movieservice.exception.MovieInfoClientException;
import io.valentinsoare.movieservice.exception.MovieInfoServerException;
import io.valentinsoare.movieservice.util.RetryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Component
public class MovieInfoRestClient {
    private final WebClient webClient;

    @Value("${restClient.movieInfoServiceUrl}")
    private String urlMovieInfoService;

    @Autowired
    public MovieInfoRestClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<MovieInfo> getMovieInfo(String movieId) {
        String url = urlMovieInfoService.concat("/id/{movieId}");

        return webClient.get()
                .uri(url, movieId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    if (clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
                        return Mono.error(new MovieInfoClientException(String.format("MovieInfo not found with id: %s", movieId),
                                String.valueOf(clientResponse.statusCode().value())));
                    }

                    return clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> Mono.error(new MovieInfoClientException(
                                    errorBody, String.valueOf(clientResponse.statusCode().value()))));
                })
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new MovieInfoServerException(
                                String.format("Server exception in MovieInfoService: %s",
                                        errorBody)))
                        ))
                .bodyToMono(MovieInfo.class)
                .retryWhen(RetryUtil.retrySpec());
    }

    public Flux<MovieInfo> getStreamMovieInfos() {
        String url = urlMovieInfoService.concat("/stream");

        return webClient.get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new MovieInfoClientException(
                                errorBody, String.valueOf(clientResponse.statusCode().value())))))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new MovieInfoServerException(
                                String.format("Server exception in MovieInfoService: %s",
                                        errorBody)))
                        ))
                .bodyToFlux(MovieInfo.class);
    }
}
