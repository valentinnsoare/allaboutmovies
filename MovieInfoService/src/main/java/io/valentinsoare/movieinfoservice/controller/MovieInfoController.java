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
@RequestMapping("/api/v1/movieInfos")
public class MovieInfoController {
    private final MovieInfoService movieInfoService;

    @Autowired
    public MovieInfoController(MovieInfoService movieInfoService) {
        this.movieInfoService = movieInfoService;
    }

    @PostMapping
    public Mono<ResponseEntity<MovieInfo>> addMovieInfo(@RequestBody @Valid MovieInfo movieInfo) {
        return movieInfoService.addMovieInfo(movieInfo)
                .map(m -> new ResponseEntity<>(m, HttpStatus.CREATED));
    }

    @GetMapping("/id/{movieId}")
    public Mono<ResponseEntity<MovieInfo>> getMovieInfoById(@PathVariable @NotNull String movieId) {
        return movieInfoService.getMovieInfoById(movieId)
                .map(m -> new ResponseEntity<>(m, HttpStatus.OK))
                .switchIfEmpty(
                        Mono.error(
                                new ResourceNotFoundException("movieInfo", Map.of("movieId", movieId))
                        )
                );
    }

    @GetMapping("/all")
    public Flux<MovieInfo> getAllMovieInfos(
            @RequestParam(required = false) Integer year
    ) {
        if (year != null) {
            return movieInfoService.getAllMoviesInfosByYear(year)
                .switchIfEmpty(
                        Flux.error(new ResourceNotFoundException("movieInfo", Map.of("year", String.valueOf(year))))
                );
        }

        return movieInfoService.getAllMovieInfos()
                .switchIfEmpty(
                        Flux.error(new NoElementsException("movieInfo"))
                );
    }

    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countAll() {
        return movieInfoService.countAll()
                .map(count -> new ResponseEntity<>(count, HttpStatus.OK))
                .switchIfEmpty(
                        Mono.error(new NoElementsException("movieInfo"))
                );
    }

    @GetMapping("/name/{name}")
    public Mono<ResponseEntity<MovieInfo>> getMovieByName(@PathVariable @NotNull String name) {
        return movieInfoService.getMovieByName(name)
                .map(m -> new ResponseEntity<>(m, HttpStatus.OK))
                .switchIfEmpty(Mono.error(
                        new ResourceNotFoundException("movieInfo", Map.of("name", name)))
                );
    }

    @PutMapping("/id/{movieId}")
    public Mono<ResponseEntity<MovieInfo>> updateMovieInfoById(
            @PathVariable @NotNull String movieId,
            @RequestBody @Valid MovieInfo movieInfo
    ) {
        return movieInfoService.updateMovieInfoById(movieId, movieInfo)
                .map(m -> new ResponseEntity<>(m, HttpStatus.OK))
                .switchIfEmpty(Mono.error(
                        new ResourceNotFoundException("movieInfo", Map.of("movieId", movieId)))
                );
    }

    @DeleteMapping("/id/{movieId}")
    public Mono<ResponseEntity<String>> deleteMovieInfoById(@PathVariable @NotNull String movieId) {
        return movieInfoService.deleteMovieInfoById(movieId)
                .map(m -> new ResponseEntity<>(
                        String.format("MovieInfo with id %s has been deleted", movieId), HttpStatus.OK))
                .switchIfEmpty(Mono.error(
                        new ResourceNotFoundException("movieInfo", Map.of("movieId", movieId)))
                );
    }
}
