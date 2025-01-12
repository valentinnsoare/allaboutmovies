package io.valentinsoare.movieinfoservice.response;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {
    @NotNull
    private Instant timestamp;

    @Size(max = 500)
    @NotBlank(message = "Message is mandatory!")
    private String message;

    @Size(max = 255)
    @NotBlank(message = "Details is mandatory!")
    private String details;

    @Min(value = 100, message = "Status code must be greater than or equal to 100!")
    private int statusCode;
}
