package in.bm.MovieService.ResponseDTO;

import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
@Builder
public class ShowPageResponseDTO {

    private List<ShowResponseDTO> showResponses;
    private int page;
    private int size;
    private long totalElements;
    private boolean hasNext;
    private boolean hasPrevious;
    private int totalPages;

}
