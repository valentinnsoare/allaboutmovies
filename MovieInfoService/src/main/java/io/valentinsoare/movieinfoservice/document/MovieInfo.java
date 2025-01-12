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
    private Integer year;

    @Size(max = 100, message = "Cast should have at most 100 characters")
    private List<String> cast;

    private LocalDate releaseDate;
}
