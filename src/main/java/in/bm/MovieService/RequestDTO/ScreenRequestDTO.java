package in.bm.MovieService.RequestDTO;

import lombok.*;

import javax.validation.constraints.NotBlank;


@Data
public class ScreenRequestDTO {

    @NotBlank
    private String theaterCode;

    @NotBlank
    private String screenName;

}
