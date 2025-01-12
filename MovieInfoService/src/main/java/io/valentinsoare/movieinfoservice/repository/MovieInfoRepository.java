package io.valentinsoare.movieinfoservice.repository;

import io.valentinsoare.movieinfoservice.document.MovieInfo;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieInfoRepository extends ReactiveMongoRepository<MovieInfo, String> {
    @Query("{ 'name' : ?0 }")
    Mono<MovieInfo> getMovieByName(String name);

    @Query(value = "{ 'year' : ?0 }", sort = "{ 'id' : 1 }")
    Flux<MovieInfo> findByYear(Integer year);
}
