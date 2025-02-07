package io.valentinsoare.movieinfoservice.unit.controller;

import io.valentinsoare.movieinfoservice.controller.MovieInfoController;
import io.valentinsoare.movieinfoservice.document.MovieInfo;
import io.valentinsoare.movieinfoservice.service.MovieInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@AutoConfigureWebTestClient
@WebFluxTest(controllers = MovieInfoController.class)
public class MovieInfoControllerUnitTest {
    private final WebTestClient webTestClient;

    @MockitoBean
    private MovieInfoService movieInfoServiceMock;

    @Autowired
    public MovieInfoControllerUnitTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    @Test
    void getAllMovieInfos() {
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

        when(movieInfoServiceMock.getAllMovieInfos())
                .thenReturn(Flux.fromIterable(movies));

        webTestClient.get()
                .uri("/api/v1/movieInfos/all")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MovieInfo.class)
                .hasSize(3);
    }

    @Test
    void countAll() {
        when(movieInfoServiceMock.countAll())
                .thenReturn(Mono.just(3L));

        webTestClient.get()
                .uri("/api/v1/movieInfos/count")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Long.class)
                .isEqualTo(3L);
    }

    @Test
    void getMovieInfoById() {
        String searchedForId = "1";

        MovieInfo darkKnight = MovieInfo.builder()
                .name("The Dark Knight")
                .year(2008)
                .cast(Arrays.asList("Christian Bale", "Heath Ledger"))
                .id(searchedForId)
                .releaseDate(LocalDate.parse("2008-07-18"))
                .build();

        when(movieInfoServiceMock.getMovieInfoById(isA(String.class)))
                .thenReturn(Mono.just(darkKnight));

        webTestClient.get()
                .uri(String.format("/api/v1/movieInfos/id/%s", searchedForId))
                .exchange()
                .expectStatus().isOk()
                .expectBody(MovieInfo.class)
                .isEqualTo(darkKnight);
    }

    @Test
    void getMovieInfoByName() {
        String searchedForName = "The Dark Knight";

        MovieInfo darkKnight = MovieInfo.builder()
                .name(searchedForName)
                .year(2008)
                .cast(Arrays.asList("Christian Bale", "Heath Ledger"))
                .id("1")
                .releaseDate(LocalDate.parse("2008-07-18"))
                .build();

        when(movieInfoServiceMock.getMovieByName(isA(String.class)))
                .thenReturn(Mono.just(darkKnight));

        webTestClient.get()
                .uri(String.format("/api/v1/movieInfos/name/%s", searchedForName))
                .exchange()
                .expectStatus().isOk()
                .expectBody(MovieInfo.class)
                .isEqualTo(darkKnight);
    }

    @Test
    void updateMovieInfoById() {
        String searchedForId = "1";

        MovieInfo darkKnight = MovieInfo.builder()
                .name("The Dark Knight")
                .year(2021)
                .cast(Arrays.asList("Christian Bale", "Heath Ledger", "Morgan Freeman"))
                .id(searchedForId)
                .releaseDate(LocalDate.parse("2021-01-01"))
                .build();

        when(movieInfoServiceMock.updateMovieInfoById(isA(String.class), isA(MovieInfo.class)))
                .thenReturn(Mono.just(darkKnight));

        webTestClient.put()
                .uri(String.format("/api/v1/movieInfos/id/%s", searchedForId))
                .bodyValue(darkKnight)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MovieInfo.class)
                .isEqualTo(darkKnight);
    }

    @Test
    void deleteMovieInfoById() {
        String searchedForId = "1";

        when(movieInfoServiceMock.deleteMovieInfoById(isA(String.class)))
                .thenReturn(
                        Mono.just(
                                MovieInfo.builder()
                                        .name("The Dark Knight")
                                        .year(2008)
                                        .cast(Arrays.asList("Christian Bale", "Heath Ledger"))
                                        .id(searchedForId)
                                        .releaseDate(LocalDate.parse("2008-07-18"))
                                        .build())
                );

        webTestClient.delete()
                .uri(String.format("/api/v1/movieInfos/id/%s", searchedForId))
                .exchange()
                .expectStatus()
                .isOk();
    }
}
