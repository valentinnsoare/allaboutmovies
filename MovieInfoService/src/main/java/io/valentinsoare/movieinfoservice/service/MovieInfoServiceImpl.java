package io.valentinsoare.movieinfoservice.service;

import io.valentinsoare.movieinfoservice.document.MovieInfo;
import io.valentinsoare.movieinfoservice.exception.NoElementsException;
import io.valentinsoare.movieinfoservice.exception.ResourceNotFoundException;
import io.valentinsoare.movieinfoservice.repository.MovieInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

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
        return movieInfoRepository.findAll()
                .switchIfEmpty(Flux.empty());
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
    public Mono<MovieInfo> deleteMovieInfoById(String movieId) {
       return movieInfoRepository.findById(movieId)
                .switchIfEmpty(Mono.empty());
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Long> countAll() {
        return movieInfoRepository.count();
    }
}
