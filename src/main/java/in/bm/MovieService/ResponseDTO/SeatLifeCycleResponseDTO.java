package in.bm.MovieService.ResponseDTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SeatLifeCycleResponseDTO {

    private String seatNumber;
    private String message;


}
