package io.valentinsoare.movieinfoservice.controller;

import io.valentinsoare.movieinfoservice.document.MovieInfo;
import io.valentinsoare.movieinfoservice.service.MovieInfoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class MovieInfoController {
    private final MovieInfoService movieInfoService;

    @Autowired
    public MovieInfoController(MovieInfoService movieInfoService) {
        this.movieInfoService = movieInfoService;
    }

    @PostMapping("/movieInfos")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovieInfo> addMovieInfo(@RequestBody @Valid MovieInfo movieInfo) {
        return movieInfoService.addMovieInfo(movieInfo);
    }

    @GetMapping("/movieInfos/{movieId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<MovieInfo> getMovieInfo(@PathVariable @NotNull String movieId) {
        return movieInfoService.getMovieInfoById(movieId);
    }

    @GetMapping("/movieInfos/all")
    @ResponseStatus(HttpStatus.OK)
    public Flux<MovieInfo> getAllMovieInfos() {
        return movieInfoService.getAllMovieInfos();
    }

    @GetMapping("/movieInfos/count")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Long> countAll() {
        return movieInfoService.countAll();
    }
}
