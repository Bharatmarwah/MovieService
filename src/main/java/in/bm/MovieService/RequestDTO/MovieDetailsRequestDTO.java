package in.bm.MovieService.RequestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


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


