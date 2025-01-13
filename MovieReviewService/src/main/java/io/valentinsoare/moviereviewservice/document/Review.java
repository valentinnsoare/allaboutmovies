package io.valentinsoare.moviereviewservice.document;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    private String reviewId;

    @Min(value = 1L, message = "movieInfoId must be greater than 0")
    private Long movieInfoId;

    @Size(min = 100, max = 1000, message = "Comment must be between 100 and 1000 characters")
    private String comment;

    @Min(value = 1L, message = "Rating must be greater than 0")
    private Double rating;
}
