package in.bm.MovieService.ResponseDTO;

import in.bm.MovieService.ENTITY.SeatType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SeatCategoryResponseDTO {

    private Long seatCategoryId;

    private SeatType seatType;

    private Double prize;

    private Long screenId;


}
