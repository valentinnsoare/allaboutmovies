package io.valentinsoare.movieinfoservice.repository;

import io.valentinsoare.movieinfoservice.document.MovieInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MovieInfoRepository extends ReactiveMongoRepository<MovieInfo, String> {
}
