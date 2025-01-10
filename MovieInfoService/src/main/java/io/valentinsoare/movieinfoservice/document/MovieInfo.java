package io.valentinsoare.movieinfoservice.document;

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
    private String name;
    private Integer year;
    private List<String> cast;
    private LocalDate releaseDate;
}
