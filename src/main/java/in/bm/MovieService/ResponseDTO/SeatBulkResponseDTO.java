package in.bm.MovieService.ResponseDTO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SeatBulkResponseDTO {

    private Long screenId;
    private Long seatCategoryId;
    private int totalSeatsCreated;
    private List<String> seatNumbers;

}
