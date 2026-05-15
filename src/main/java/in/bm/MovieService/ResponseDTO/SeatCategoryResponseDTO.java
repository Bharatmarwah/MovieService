package in.bm.MovieService.ResponseDTO;

import in.bm.MovieService.ENTITY.SeatType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeatCategoryResponseDTO {

    private Long seatCategoryId;

    private SeatType seatType;

    private Double prize;

    private Long screenId;


}
