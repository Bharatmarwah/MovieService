package in.bm.MovieService.RequestDTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TheaterReviewRequestDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String comment;
    @NotNull
    private Double rating;



}
