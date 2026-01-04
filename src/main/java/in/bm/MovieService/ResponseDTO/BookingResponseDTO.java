package in.bm.MovieService.ResponseDTO;

import in.bm.MovieService.ENTITY.Seat;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BookingResponseDTO {

    private String movieCode;
    private Long showId;
    private String theaterCode;
    private List<String> seatNumbers;
    private Double baseAmount;



}
