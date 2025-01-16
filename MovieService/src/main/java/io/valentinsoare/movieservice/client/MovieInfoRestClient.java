package io.valentinsoare.movieservice.client;

import io.valentinsoare.movieservice.domain.MovieInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
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
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> Mono.error())
                .bodyToMono(MovieInfo.class);
    }
}
