package io.valentinsoare.movieservice.exception;

import lombok.*;


@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieReviewClientException extends RuntimeException {
    String message;
    Integer statusCode;
}
