package in.bm.MovieService.ResponseDTO;

import in.bm.MovieService.ENTITY.SeatFeature;
import in.bm.MovieService.ENTITY.SeatLifecycle;
import in.bm.MovieService.ENTITY.ViewType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SeatResponseDTO {

    private long screenId;
    private long seatCategoryId;
    private long seatId;
    private SeatFeature seatFeature;
    private String seatNumber;
    private ViewType viewType;
    private SeatLifecycle seatLifecycle;

}
