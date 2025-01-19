package io.valentinsoare.movieservice.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieReviewClientException extends RuntimeException {
    String message;
    Integer statusCode;
}
