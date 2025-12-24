package in.bm.MovieService.ResponseDTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MoviePageResponseDTO {

    private List<MovieResponseDTO> movies;

    private int page;
    private int size;

    private long totalElements;
    private int totalPages;

    private boolean hasNext;
    private boolean hasPrevious;
}
