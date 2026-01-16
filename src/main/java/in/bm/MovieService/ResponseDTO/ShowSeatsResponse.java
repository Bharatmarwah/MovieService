package in.bm.MovieService.ResponseDTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;
@JsonPropertyOrder({
        "showId"
        ,"screenName"
        ,"movieCode"
        ,"theaterCode"
        ,"startTime",
        "seats"})

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
