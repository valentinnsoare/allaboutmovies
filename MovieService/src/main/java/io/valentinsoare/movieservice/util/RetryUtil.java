package io.valentinsoare.movieservice.util;

import io.valentinsoare.movieservice.exception.MovieInfoServerException;
import reactor.core.Exceptions;
import reactor.util.retry.Retry;

import java.time.Duration;

public class RetryUtil {
    public static Retry retrySpec(int maxRetries, int retryDelay) {
        return Retry.fixedDelay(maxRetries, Duration.ofSeconds(retryDelay))
                .filter(ex -> ex instanceof MovieInfoServerException)
                .onRetryExhaustedThrow((retryBackOff, retrySignal) -> Exceptions.propagate(
                        retrySignal.failure()
                ));
    }

    public static Retry retrySpec() {
        return retrySpec(3, 1);
    }
}
