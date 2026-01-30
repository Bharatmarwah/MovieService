package in.bm.MovieService.ResponseDTO;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter@Builder
public class InternalShowResponse {
    private String movieCode;
    private Long showId;
    private List<Long> showSeatsIds;
    private Double totalAmount;
}
