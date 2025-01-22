package io.valentinsoare.movieservice.util;

import io.valentinsoare.movieservice.exception.MovieInfoServerException;
import io.valentinsoare.movieservice.exception.MovieReviewServerException;
import reactor.core.Exceptions;
import reactor.util.retry.Retry;

import java.time.Duration;

public class RetryUtil {
    public static Retry retrySpecMovieInfosServerException(int maxRetries, int retryDelay) {
        return Retry.fixedDelay(maxRetries, Duration.ofSeconds(retryDelay))
                .filter(ex -> ex instanceof MovieInfoServerException)
                .onRetryExhaustedThrow((retryBackOff, retrySignal) -> Exceptions.propagate(
                        retrySignal.failure()
                ));
    }

    public static Retry retrySpecMovieInfosServerException() {
        return retrySpecMovieInfosServerException(3,1);
    }

    public static Retry retrySpecMovieReviewsServerException(int maxRetries, int retryDelay) {
        return Retry.fixedDelay(maxRetries, Duration.ofSeconds(retryDelay))
                .filter(ex -> ex instanceof MovieReviewServerException)
                .onRetryExhaustedThrow((retryBackOff, retrySignal) -> Exceptions.propagate(
                        retrySignal.failure()
                ));
    }

    public static Retry retrySpecMovieReviewsServerException() {
        return retrySpecMovieReviewsServerException(3,1);
    }
}
