package io.valentinsoare.movieservice.exception;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieInfoClientException extends RuntimeException {
    private String message;
    private Integer statusCode;
}
