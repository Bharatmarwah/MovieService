package in.bm.MovieService.RequestDTO;

import in.bm.MovieService.ENTITY.SeatType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class SeatCategoryRequestDTO {

    @NotNull
    private SeatType seatType;

    @NotNull
    private Double prize;

    @NotNull
    private Long screenId;


}
