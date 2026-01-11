package in.bm.MovieService.RequestDTO;

import lombok.Data;

@Data
public class SeatRequestDTO {
    private Long seatCategoryId;
    private Long screenId;
    private String seatNumber;
}
