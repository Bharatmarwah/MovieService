package in.bm.MovieService.RequestDTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class MovieReviewRequestDTO {
    @NotBlank
    private String username;
    @NotNull
    private Double rating;
    @NotBlank
    private String comment;
}
