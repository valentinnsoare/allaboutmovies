package io.valentinsoare.movieinfoservice.service;

import io.valentinsoare.movieinfoservice.document.MovieInfo;
import io.valentinsoare.movieinfoservice.repository.MovieInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MovieInfoServiceImpl implements MovieInfoService {
    private final MovieInfoRepository movieInfoRepository;

    @Autowired
    public MovieInfoServiceImpl(MovieInfoRepository movieInfoRepository) {
        this.movieInfoRepository = movieInfoRepository;
    }

    @Override
    @Transactional
    public Mono<MovieInfo> addMovieInfo(MovieInfo movieInfo) {
        return movieInfoRepository.save(movieInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<MovieInfo> getMovieInfoById(String movieId) {
        return movieInfoRepository.findById(movieId);
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<MovieInfo> getMovieByName(String name) {
        return movieInfoRepository.getMovieByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<MovieInfo> getAllMovieInfos() {
        return movieInfoRepository.findAll();
    }

    @Override
    @Transactional
    public Mono<MovieInfo> updateMovieInfoById(String movieId, MovieInfo movieInfo) {
        return movieInfoRepository.findById(movieId)
                .flatMap(existingMovieInfo -> {
                    existingMovieInfo.setName(movieInfo.getName());
                    existingMovieInfo.setCast(movieInfo.getCast());
                    existingMovieInfo.setYear(movieInfo.getYear());
                    existingMovieInfo.setReleaseDate(movieInfo.getReleaseDate());

                    return movieInfoRepository.save(existingMovieInfo);
                });
    }

    @Override
    @Transactional
    public Mono<String> deleteMovieInfoById(String movieId) {
       return movieInfoRepository.findById(movieId)
                .defaultIfEmpty(new MovieInfo())
                .flatMap(movieInfo -> {
                    if (movieInfo.getId() == null) {
                        return Mono.error(new RuntimeException("Movie not found"));
                    }

                    return movieInfoRepository.delete(movieInfo)
                            .then(Mono.just("Movie deleted"));
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Long> countAll() {
        return movieInfoRepository.count();
    }
}
