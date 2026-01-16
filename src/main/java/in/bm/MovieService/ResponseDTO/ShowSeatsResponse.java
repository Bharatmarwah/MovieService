package in.bm.MovieService.ResponseDTO;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Builder
@Getter
public class ShowSeatsResponse {

    private long showId;
    private String screenName;
    private String movieCode;
    private String theaterCode;
    private LocalTime startTime;

    private List<SeatsResponseDTO> seats;




}
