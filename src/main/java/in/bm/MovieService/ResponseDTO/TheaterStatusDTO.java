package in.bm.MovieService.ResponseDTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TheaterStatusDTO {

    private String theaterCode;
    private String message;


}
