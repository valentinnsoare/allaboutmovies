package io.valentinsoare.movieservice.exception;

import io.valentinsoare.moviereviewservice.response.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.TreeMap;

@Slf4j
@ControllerAdvice
public class MovieServiceCustomExceptionHandler {
    @ExceptionHandler({
            MovieInfoClientException.class,
            MovieInfoServerException.class,
            MovieReviewClientException.class,
            MovieReviewServerException.class,
            NoResourceFoundException.class
    })
    public ResponseEntity<ErrorMessage> handleGlobalException(RuntimeException e) {
        ErrorMessage anErrorOccurred = ErrorMessage.builder()
                .message(e.getLocalizedMessage())
                .details("Resource from the user was not found when request was processed.")
                .timestamp(Instant.now())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .build();

        return new ResponseEntity<>(anErrorOccurred, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ WebExchangeBindException.class })
    public ResponseEntity<Object> handleResourceViolationException(WebExchangeBindException e) {
        TreeMap<String, String> errors = new TreeMap<>();

        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldNameError = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            errors.put(fieldNameError, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
