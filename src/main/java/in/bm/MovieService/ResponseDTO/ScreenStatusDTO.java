package in.bm.MovieService.ResponseDTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScreenStatusDTO {
    private Long screenId;
    private String message;
}
