package io.valentinsoare.movieinfoservice.integration.repository;

import io.valentinsoare.movieinfoservice.document.MovieInfo;
import io.valentinsoare.movieinfoservice.repository.MovieInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@DataMongoTest
class MovieInfoRepositoryIntegrationTest {
    private final MovieInfoRepository movieInfoRepository;

    @Autowired
    public MovieInfoRepositoryIntegrationTest(MovieInfoRepository movieInfoRepository) {
        this.movieInfoRepository = movieInfoRepository;
    }

    @BeforeEach
    void setUp() {
        List<MovieInfo> movies = Arrays.asList(
                MovieInfo.builder()
                        .name("The Dark Knight")
                        .year(2008)
                        .cast(Arrays.asList("Christian Bale", "Heath Ledger"))
                        .id("1")
                        .releaseDate(LocalDate.parse("2008-07-18"))
                        .build(),
                MovieInfo.builder()
                        .name("Inception")
                        .year(2010)
                        .cast(Arrays.asList("Leonardo DiCaprio", "Joseph Gordon-Levitt"))
                        .id("2")
                        .releaseDate(LocalDate.parse("2010-07-16"))
                        .build(),
                MovieInfo.builder()
                        .name("Interstellar")
                        .year(2014)
                        .cast(Arrays.asList("Matthew McConaughey", "Anne Hathaway"))
                        .id("3")
                        .releaseDate(LocalDate.parse("2014-11-07"))
                        .build()
        );

        movieInfoRepository.saveAll(movies).blockLast();
    }

    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll().block();
    }

    @Test
    void findAll() {
        Flux<MovieInfo> all = movieInfoRepository.findAll();

        StepVerifier.create(all)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void findById() {
        Mono<MovieInfo> movieById = movieInfoRepository.findById("3");

        StepVerifier.create(movieById)
                .assertNext(movieInfo -> {
                    assert movieInfo.getName().equals("Interstellar");
                    assert movieInfo.getYear().equals(2014);
                })
                .expectComplete()
                .verify();
    }

    @Test
    void saveMovieInfo() {
        MovieInfo movieInfo = MovieInfo.builder()
                .name("The Prestige")
                .year(2006)
                .cast(Arrays.asList("Hugh Jackman", "Christian Bale"))
                .id(null)
                .releaseDate(LocalDate.parse("2006-10-20"))
                .build();

        Mono<MovieInfo> savedMovieInfo = movieInfoRepository.save(movieInfo);

        StepVerifier.create(savedMovieInfo)
                .assertNext(movie -> {
                    assert movie.getId() != null;
                    assert movie.getName().equals("The Prestige");
                    assert movie.getYear().equals(2006);
                })
                .expectComplete()
                .verify();
    }

    @Test
    void updateMovieInfo() {
        MovieInfo movie = movieInfoRepository.findById("2").block();

        if (Objects.nonNull(movie)) {
            movie.setName("Inception 2");
            movie.setReleaseDate(LocalDate.parse("2025-01-05"));
        }

        Mono<MovieInfo> updatedMovie = movieInfoRepository.save(movie);

        StepVerifier.create(updatedMovie)
                .assertNext(movieInfo -> {
                    assert movieInfo.getName().equals("Inception 2");
                    assert movieInfo.getReleaseDate().equals(LocalDate.parse("2025-01-05"));
                })
                .expectComplete()
                .verify();
    }

    @Test
    void deleteMovieInfo() {
        movieInfoRepository.deleteById("1").block();
        Flux<MovieInfo> all = movieInfoRepository.findAll();

        StepVerifier.create(all)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void findByYear() {
        Integer givenYear = 2010;
        Flux<MovieInfo> moviesByYear = movieInfoRepository.findByYear(givenYear);

        StepVerifier.create(moviesByYear)
                .consumeNextWith(movieInfo -> {
                    assert movieInfo.getName().equals("Inception");
                    assert movieInfo.getYear().equals(givenYear);
                })
                .expectComplete()
                .verify();
    }
}