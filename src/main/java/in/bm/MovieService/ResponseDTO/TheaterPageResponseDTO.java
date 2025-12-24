package in.bm.MovieService.ResponseDTO;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TheaterPageResponseDTO {

    private List<TheaterInfoDTO> theaters;

    private int page;
    private int size;

    private long totalElements;
    private int totalPages;

    private boolean hasNext;
    private boolean hasPrevious;


}
