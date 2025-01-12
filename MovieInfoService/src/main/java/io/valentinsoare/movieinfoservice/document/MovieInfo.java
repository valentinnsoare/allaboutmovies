package io.valentinsoare.movieinfoservice.document;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
public class MovieInfo {
    @Id
    private String id;

    @Size(max = 100, message = "Name should have at most 100 characters")
    private String name;

    @Min(value = 1900, message = "Year should be at least 1900")
    @Size(max = 4, message = "Year should have at most 4 characters")
    private Integer year;

    @Size(max = 100, message = "Cast should have at most 100 characters")
    private List<String> cast;

    @Size(max = 10, message = "Release date should have at most 10 characters")
    private LocalDate releaseDate;
}
