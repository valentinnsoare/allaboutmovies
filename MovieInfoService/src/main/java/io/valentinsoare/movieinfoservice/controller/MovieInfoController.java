package io.valentinsoare.movieinfoservice.controller;

import io.valentinsoare.movieinfoservice.document.MovieInfo;
import io.valentinsoare.movieinfoservice.service.MovieInfoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@RestController
@RequestMapping("/api/v1/movieInfos")
public class MovieInfoController {
    private final MovieInfoService movieInfoService;
    private Sinks.Many<MovieInfo> movieInfoSink;

    @Autowired
    public MovieInfoController(MovieInfoService movieInfoService) {
        this.movieInfoService = movieInfoService;
        this.movieInfoSink = Sinks.many().multicast().onBackpressureBuffer();
    }

    @PostMapping
    public Mono<ResponseEntity<MovieInfo>> addMovieInfo(@RequestBody @Valid MovieInfo movieInfo) {
        return movieInfoService.addMovieInfo(movieInfo)
                .doOnNext(movieInfoSink::tryEmitNext)
                .map(m -> new ResponseEntity<>(m, HttpStatus.CREATED));
    }

    @GetMapping(value = "/stream", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<MovieInfo> getStreamMovieInfo() {
        return movieInfoSink.asFlux();
    }

    @GetMapping("/id/{movieId}")
    public Mono<ResponseEntity<MovieInfo>> getMovieInfoById(@PathVariable @NotNull String movieId) {
        return movieInfoService.getMovieInfoById(movieId)
                .map(m -> new ResponseEntity<>(m, HttpStatus.OK));
    }

    @GetMapping("/all")
    public Flux<MovieInfo> getAllMovieInfos(
            @RequestParam(required = false) Integer year
    ) {
        if (year != null) {
            return movieInfoService.getAllMoviesInfosByYear(year);
        }

        return movieInfoService.getAllMovieInfos();
    }

    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countAll() {
        return movieInfoService.countAll()
                .map(count -> new ResponseEntity<>(count, HttpStatus.OK));
    }

    @GetMapping("/name/{name}")
    public Mono<ResponseEntity<MovieInfo>> getMovieByName(@PathVariable @NotNull String name) {
        return movieInfoService.getMovieByName(name)
                .map(m -> new ResponseEntity<>(m, HttpStatus.OK));
    }

    @PutMapping("/id/{movieId}")
    public Mono<ResponseEntity<MovieInfo>> updateMovieInfoById(
            @PathVariable @NotNull String movieId,
            @RequestBody @Valid MovieInfo movieInfo
    ) {
        return movieInfoService.updateMovieInfoById(movieId, movieInfo)
                .map(m -> new ResponseEntity<>(m, HttpStatus.OK));
    }

    @DeleteMapping("/id/{movieId}")
    public Mono<ResponseEntity<String>> deleteMovieInfoById(@PathVariable @NotNull String movieId) {
        return movieInfoService.deleteMovieInfoById(movieId)
                .map(m -> new ResponseEntity<>(
                        String.format("MovieInfo with id %s has been deleted", movieId), HttpStatus.NO_CONTENT));
    }
}
