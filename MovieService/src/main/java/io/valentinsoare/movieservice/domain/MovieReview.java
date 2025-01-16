package io.valentinsoare.movieservice.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class MovieReview {
    private String reviewId;

    @Size(min = 1, max = 24, message = "MovieInfoId must be between 1 and 24 characters")
    private String movieInfoId;

    @Size(min = 2, max = 1000, message = "Comment must be between 100 and 1000 characters")
    private String comment;

    @Min(value = 1L, message = "Rating must be greater than 0")
    private Double rating;
}
