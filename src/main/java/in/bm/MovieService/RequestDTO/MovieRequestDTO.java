package in.bm.MovieService.RequestDTO;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class MovieRequestDTO {
    @NotBlank
    private String movieName;
    @NotBlank
    private String duration;
    @NotBlank
    private String movieAvatar;
    @NotBlank
    private String certificate;
    @NotBlank
    private String language;
    @NotNull
    @Valid
    private MovieDetailsRequestDTO movieDetailsRequestDTO;

}
