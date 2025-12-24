package in.bm.MovieService.RequestDTO;

import in.bm.MovieService.ENTITY.SeatType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SeatCategoryRequestDTO {

    @NotNull
    private SeatType seatType;

    @NotNull
    private Double prize;

    @NotNull
    private Long screenId;


}
