package in.bm.MovieService.RequestDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;




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
