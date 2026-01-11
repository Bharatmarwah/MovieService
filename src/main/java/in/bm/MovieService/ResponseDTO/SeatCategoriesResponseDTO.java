package in.bm.MovieService.ResponseDTO;

import in.bm.MovieService.ENTITY.SeatFeature;
import in.bm.MovieService.ENTITY.ViewType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SeatCategoriesResponseDTO {

    private long seatId;

    private String seatNumber;

    private SeatFeature seatFeature;

    private ViewType viewType;

}
