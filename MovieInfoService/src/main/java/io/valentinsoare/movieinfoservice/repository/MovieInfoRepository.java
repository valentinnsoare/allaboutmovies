package io.valentinsoare.movieinfoservice.repository;

import io.valentinsoare.movieinfoservice.document.MovieInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface MovieInfoRepository extends ReactiveMongoRepository<MovieInfo, String> {
    Flux<MovieInfo> findAllBy(Pageable pageable);
}
