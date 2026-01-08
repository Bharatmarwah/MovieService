package in.bm.MovieService.RequestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;



@Data
public class MovieReviewRequestDTO {
    @NotBlank
    private String username;
    @NotNull
    private Double rating;
    @NotBlank
    private String comment;
}
