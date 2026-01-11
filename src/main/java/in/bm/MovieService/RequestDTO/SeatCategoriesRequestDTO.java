package in.bm.MovieService.RequestDTO;

import in.bm.MovieService.ENTITY.SeatFeature;
import in.bm.MovieService.ENTITY.ViewType;
import lombok.Data;

@Data
public class SeatCategoriesRequestDTO {

    private SeatFeature seatFeature;

    private ViewType viewType;
}
