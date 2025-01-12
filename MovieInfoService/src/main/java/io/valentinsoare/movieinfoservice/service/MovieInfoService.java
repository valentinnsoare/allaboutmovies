package io.valentinsoare.movieinfoservice.service;

import io.valentinsoare.movieinfoservice.document.MovieInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieInfoService {
    Mono<MovieInfo> addMovieInfo(MovieInfo movieInfo);
    Mono<MovieInfo> getMovieInfoById(String movieId);
    Mono<MovieInfo> getMovieByName(String name);
    Flux<MovieInfo> getAllMovieInfos();
    Mono<MovieInfo> updateMovieInfoById(String movieId, MovieInfo movieInfo);
    Mono<MovieInfo> deleteMovieInfoById(String movieId);
    Mono<Long> countAll();
}
