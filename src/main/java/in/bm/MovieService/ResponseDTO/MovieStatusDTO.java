package in.bm.MovieService.ResponseDTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MovieStatusDTO {
    private String movieCode;
    private String message;
}
