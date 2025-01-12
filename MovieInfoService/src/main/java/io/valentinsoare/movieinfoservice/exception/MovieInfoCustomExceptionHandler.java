package io.valentinsoare.movieinfoservice.exception;

import io.valentinsoare.movieinfoservice.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.TreeMap;

@Slf4j
@ControllerAdvice
public class MovieInfoCustomExceptionHandler {

    @ExceptionHandler(WebExchangeBindException.class)
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
