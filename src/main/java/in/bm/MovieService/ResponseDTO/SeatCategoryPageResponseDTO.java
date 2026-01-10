package in.bm.MovieService.ResponseDTO;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SeatCategoryPageResponseDTO {

    private List<SeatCategoryResponseDTO> seatCategories;
    private int page;
    private int size;
    private long totalElements;
    private boolean hasNext;
    private boolean hasPrevious;
    private int totalPages;



}
