package io.valentinsoare.moviereviewservice.exception;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewNotFoundException extends RuntimeException {
    private String message;
    private Instant timestamp;
    private String statusCode;
    private String details;
    private Throwable ex;
}
