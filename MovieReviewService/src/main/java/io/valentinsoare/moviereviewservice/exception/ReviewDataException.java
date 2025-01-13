package io.valentinsoare.moviereviewservice.exception;

import lombok.*;

import java.time.Instant;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDataException extends RuntimeException {
    private String message;
    private Instant timestamp;
    private Integer statusCode;
    private String details;
}
