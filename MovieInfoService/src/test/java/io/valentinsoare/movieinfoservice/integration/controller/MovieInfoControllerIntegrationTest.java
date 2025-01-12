package io.valentinsoare.movieinfoservice.integration.controller;

import io.valentinsoare.movieinfoservice.document.MovieInfo;
import io.valentinsoare.movieinfoservice.repository.MovieInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MovieInfoControllerIntegrationTest {

    private final MovieInfoRepository movieInfoRepository;
    private final WebTestClient webTestClient;


    @Autowired
    public MovieInfoControllerIntegrationTest(
            MovieInfoRepository movieInfoRepository,
            WebTestClient webTestClient
    ) {
        this.movieInfoRepository = movieInfoRepository;
        this.webTestClient = webTestClient;
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
    void testAddMovieInfo() {
        webTestClient.post()
                .uri("/api/v1/movieInfos")
                .bodyValue(MovieInfo.builder()
                        .name("The Prestige")
                        .year(2006)
                        .cast(Arrays.asList("Christian Bale", "Hugh Jackman"))
                        .id("99")
                        .releaseDate(LocalDate.parse("2006-10-20"))
                        .build())
                .exchange()
                .expectStatus().isCreated()
                .expectBody(MovieInfo.class)
                .value(movieInfo -> {
                    assert movieInfo != null;
                    assert movieInfo.getName().equals("The Prestige");
                    assert movieInfo.getYear() == 2006;
                    assert movieInfo.getCast().containsAll(Arrays.asList("Christian Bale", "Hugh Jackman"));
                    assert movieInfo.getId() != null;
                    assert movieInfo.getReleaseDate().equals(LocalDate.parse("2006-10-20"));
                });
    }

    @Test
    void testGetMovieInfo() {
        String movieInfoId = "1";

        webTestClient.get()
                .uri(String.format("/api/v1/movieInfos/id/%s", movieInfoId))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(MovieInfo.class)
                .value(movieInfo -> {
                    assert movieInfo != null;
                    assert movieInfo.getName().equals("The Dark Knight");
                    assert movieInfo.getYear() == 2008;
                    assert movieInfo.getCast().containsAll(Arrays.asList("Christian Bale", "Heath Ledger"));
                    assert movieInfo.getId().equals(movieInfoId);
                    assert movieInfo.getReleaseDate().equals(LocalDate.parse("2008-07-18"));
                });
    }

    @Test
    void testGetAllMovieInfos() {
        webTestClient.get()
                .uri("/api/v1/movieInfos/all")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .value(movieInfos -> {
                    assert movieInfos != null;
                    assert movieInfos.size() == 3;
                });

    }

    @Test
    void testGetMovieByName() {
        String movieInfoName = "Inception";

        webTestClient.get()
                .uri(String.format("/api/v1/movieInfos/name/%s", movieInfoName))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Inception");
    }

    @Test
    void testUpdateMovieInfoById() {
        String movieInfoId = "2";

        webTestClient.put()
                .uri(String.format("/api/v1/movieInfos/id/%s", movieInfoId))
                .bodyValue(MovieInfo.builder()
                        .id("2")
                        .name("Inception")
                        .year(2010)
                        .cast(Arrays.asList("Leonardo DiCaprio", "Joseph Gordon-Levitt", "Tom Hardy"))
                        .releaseDate(LocalDate.parse("2010-07-16"))
                        .build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(MovieInfo.class)
                .value(movieInfo -> {
                    assert movieInfo != null;
                    assert movieInfo.getName().equals("Inception");
                    assert movieInfo.getCast().containsAll(Arrays.asList("Leonardo DiCaprio", "Joseph Gordon-Levitt", "Tom Hardy"));
                });
    }

    @Test
    void testDeleteMovieInfoById() {
        String movieInfoId = "3";

        webTestClient.delete()
                .uri(String.format("/api/v1/movieInfos/id/%s", movieInfoId))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testGetAllMovieInfosByYear() {
        Integer year = 2010;

        URI toRequestByYear = UriComponentsBuilder.fromUriString("/api/v1/movieInfos/all")
                .queryParam("year", year)
                .buildAndExpand().toUri();

        webTestClient.get()
                .uri(toRequestByYear)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .value(movieInfos -> {
                    assert movieInfos != null;
                    assert movieInfos.size() == 1;
                });
    }
}
