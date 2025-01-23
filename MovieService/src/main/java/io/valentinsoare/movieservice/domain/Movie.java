package io.valentinsoare.movieservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @NotNull(message = "Movie info is mandatory")
    private MovieInfo movieInfo;

    private List<@NotBlank(message = "Reviews included in the reviews list should not be blank.") MovieReview> reviews;
}
