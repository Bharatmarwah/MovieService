package in.bm.MovieService.RequestDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
public class ScreenRequestDTO {

    @NotBlank
    private String theaterCode;

    @NotBlank
    private String screenName;

}
