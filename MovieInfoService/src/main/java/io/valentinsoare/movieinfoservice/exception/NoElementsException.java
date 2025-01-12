package io.valentinsoare.movieinfoservice.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoElementsException extends RuntimeException {
    private String resourceName;

    public NoElementsException(String resourceName) {
        super(String.format("No %s found in the database.", resourceName));

        this.resourceName = resourceName;
    }
}
