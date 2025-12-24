package in.bm.MovieService.RequestDTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

    @NotNull
    private Boolean isWheelchair;

    @NotNull
    private Boolean isBlockedView;

}
