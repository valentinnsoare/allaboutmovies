package io.valentinsoare.movieinfoservice.service;

import io.valentinsoare.movieinfoservice.document.MovieInfo;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieInfoService {
    Mono<MovieInfo> addMovieInfo(MovieInfo movieInfo);
    Mono<MovieInfo> getMovieInfo(String movieId);
    Flux<MovieInfo> getAllMovieInfos();
    Mono<Long> countAll();
}
