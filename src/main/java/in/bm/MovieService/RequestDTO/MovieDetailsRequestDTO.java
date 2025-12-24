package in.bm.MovieService.RequestDTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

@Data
public class MovieDetailsRequestDTO {
    @NotBlank
    private String synopsis;
    @NotEmpty
    private Map<@NotBlank String,@NotBlank String> castAndCrew;
    @NotEmpty
    private List<@NotBlank String> posters;
    @NotEmpty
    private List<@NotBlank String> movieType;
}


