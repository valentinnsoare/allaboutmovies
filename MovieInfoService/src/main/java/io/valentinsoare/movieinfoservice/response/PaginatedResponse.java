package io.valentinsoare.movieinfoservice.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponse<T> {
    private List<T> items;
    private long totalItems;
    private int pageNo;
    private int pageSize;
    private int totalPages;
    private boolean isLast;
    private boolean isEmpty;
}
