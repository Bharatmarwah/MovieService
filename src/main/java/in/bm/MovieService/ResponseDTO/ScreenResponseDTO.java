package in.bm.MovieService.ResponseDTO;

import in.bm.MovieService.ENTITY.ScreenLifeCycle;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScreenResponseDTO {

    private Long screenId;

    private String screenName;

    private String theaterCode;

    private ScreenLifeCycle screenLifecycle;





}
