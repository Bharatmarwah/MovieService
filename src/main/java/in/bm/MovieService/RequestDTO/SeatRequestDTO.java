package in.bm.MovieService.RequestDTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;



@Data
public class SeatRequestDTO {

    @NotBlank
    private String rowStart;

    @NotBlank
    private String rowEnd;

    @NotNull
    private Integer seatsPerRow;

    @NotNull
    private Long screenId;

    @NotNull
    private Long seatCategoryId;


}
