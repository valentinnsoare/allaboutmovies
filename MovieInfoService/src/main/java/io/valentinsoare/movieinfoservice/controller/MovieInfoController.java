package io.valentinsoare.movieinfoservice.controller;

import io.valentinsoare.movieinfoservice.document.MovieInfo;
import io.valentinsoare.movieinfoservice.exception.ResourceNotFoundException;
import io.valentinsoare.movieinfoservice.service.MovieInfoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class MovieInfoController {
    private final MovieInfoService movieInfoService;

    @Autowired
    public MovieInfoController(MovieInfoService movieInfoService) {
        this.movieInfoService = movieInfoService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/movieInfos")
    public Mono<MovieInfo> addMovieInfo(@RequestBody @Valid MovieInfo movieInfo) {
        return movieInfoService.addMovieInfo(movieInfo);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/movieInfos/id/{movieId}")
    public Mono<MovieInfo> getMovieInfoById(@PathVariable @NotNull String movieId) {
        return movieInfoService.getMovieInfoById(movieId)
                .switchIfEmpty(
                        Mono.error(new ResourceNotFoundException("movieInfo", Map.of("movieId", movieId)))
                );
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/movieInfos/all")
    public Flux<MovieInfo> getAllMovieInfos() {
        return movieInfoService.getAllMovieInfos()
                .switchIfEmpty(Flux.error(new RuntimeException("No movies found")));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/movieInfos/count")
    public Mono<Long> countAll() {
        return movieInfoService.countAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/movieInfos/name/{name}")
    public Mono<MovieInfo> getMovieByName(@PathVariable @NotNull String name) {
        return movieInfoService.getMovieByName(name)
                .switchIfEmpty(Mono.error(new RuntimeException("Movie not found")));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/movieInfos/id/{movieId}")
    public Mono<MovieInfo> updateMovieInfoById(
            @PathVariable @NotNull String movieId,
            @RequestBody @Valid MovieInfo movieInfo
    ) {
        return movieInfoService.updateMovieInfoById(movieId, movieInfo)
                .switchIfEmpty(Mono.error(
                        new ResourceNotFoundException("movieInfo", Map.of("movieId", movieId)))
                );
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/movieInfos/id/{movieId}")
    public Mono<String> deleteMovieInfoById(@PathVariable @NotNull String movieId) {
        return movieInfoService.deleteMovieInfoById(movieId)
                .switchIfEmpty(Mono.error(
                        new ResourceNotFoundException("movieInfo", Map.of("movieId", movieId)))
                );
    }
}
