package io.valentinsoare.moviereviewservice.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NoMovieReviewException extends RuntimeException {
    private String resourceName;

    public NoMovieReviewException(String resourceName) {
        super(String.format("No %s found in the database.", resourceName));

        this.resourceName = resourceName;
    }
}
