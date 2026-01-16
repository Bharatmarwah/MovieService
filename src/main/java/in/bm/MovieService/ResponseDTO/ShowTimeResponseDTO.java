package in.bm.MovieService.ResponseDTO;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Builder
@Getter
public class ShowTimeResponseDTO {

    private Long showId;
    private LocalTime startTime;


}
