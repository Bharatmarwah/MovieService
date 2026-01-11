package in.bm.MovieService.ResponseDTO;

import in.bm.MovieService.ENTITY.SeatFeature;
import in.bm.MovieService.ENTITY.SeatLifecycle;
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
    private List<SeatFeature> seatFeature;
    private List<SeatLifecycle> seatLifecycle;

}
