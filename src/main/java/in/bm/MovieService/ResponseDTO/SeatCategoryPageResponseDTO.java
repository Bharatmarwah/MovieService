package in.bm.MovieService.ResponseDTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
