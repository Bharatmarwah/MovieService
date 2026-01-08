package in.bm.MovieService.RequestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;



@Data
public class TheaterReviewRequestDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String comment;
    @NotNull
    private Double rating;



}
