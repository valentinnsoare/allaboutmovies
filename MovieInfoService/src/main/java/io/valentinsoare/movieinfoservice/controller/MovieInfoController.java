package io.valentinsoare.movieinfoservice.controller;

import io.valentinsoare.movieinfoservice.document.MovieInfo;
import io.valentinsoare.movieinfoservice.exception.NoElementsException;
import io.valentinsoare.movieinfoservice.exception.ResourceNotFoundException;
import io.valentinsoare.movieinfoservice.service.MovieInfoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/movieInfos")
    public Mono<ResponseEntity<MovieInfo>> addMovieInfo(@RequestBody @Valid MovieInfo movieInfo) {
        return movieInfoService.addMovieInfo(movieInfo)
                .map(m -> new ResponseEntity<>(m, HttpStatus.CREATED));
    }

    @GetMapping("/movieInfos/id/{movieId}")
    public Mono<ResponseEntity<MovieInfo>> getMovieInfoById(@PathVariable @NotNull String movieId) {
        return movieInfoService.getMovieInfoById(movieId)
                .switchIfEmpty(
                        Mono.error(
                                new ResourceNotFoundException("movieInfo", Map.of("movieId", movieId))
                        )
                )
                .map(m -> new ResponseEntity<>(m, HttpStatus.OK));
    }

    @GetMapping("/movieInfos/all")
    public Flux<ResponseEntity<MovieInfo>> getAllMovieInfos() {
        return movieInfoService.getAllMovieInfos()
                .switchIfEmpty(
                        Flux.error(new NoElementsException("movieInfo"))
                )
                .map(m -> new ResponseEntity<>(m, HttpStatus.OK));
    }

    @GetMapping("/movieInfos/count")
    public Mono<ResponseEntity<Long>> countAll() {
        return movieInfoService.countAll()
                .map(count -> new ResponseEntity<>(count, HttpStatus.OK));
    }

    @GetMapping("/movieInfos/name/{name}")
    public Mono<ResponseEntity<MovieInfo>> getMovieByName(@PathVariable @NotNull String name) {
        return movieInfoService.getMovieByName(name)
                .switchIfEmpty(Mono.error(
                        new ResourceNotFoundException("movieInfo", Map.of("name", name)))
                )
                .map(m -> new ResponseEntity<>(m, HttpStatus.OK));
    }

    @PutMapping("/movieInfos/id/{movieId}")
    public Mono<ResponseEntity<MovieInfo>> updateMovieInfoById(
            @PathVariable @NotNull String movieId,
            @RequestBody @Valid MovieInfo movieInfo
    ) {
        return movieInfoService.updateMovieInfoById(movieId, movieInfo)
                .switchIfEmpty(Mono.error(
                        new ResourceNotFoundException("movieInfo", Map.of("movieId", movieId)))
                )
                .map(m -> new ResponseEntity<>(m, HttpStatus.OK));
    }

    @DeleteMapping("/movieInfos/id/{movieId}")
    public Mono<ResponseEntity<String>> deleteMovieInfoById(@PathVariable @NotNull String movieId) {
        return movieInfoService.deleteMovieInfoById(movieId)
                .switchIfEmpty(Mono.error(
                        new ResourceNotFoundException("movieInfo", Map.of("movieId", movieId)))
                )
                .then(Mono.just("MovieInfo with id: " + movieId + " deleted successfully."))
                .map(m -> new ResponseEntity<>(m, HttpStatus.OK));
    }
}
