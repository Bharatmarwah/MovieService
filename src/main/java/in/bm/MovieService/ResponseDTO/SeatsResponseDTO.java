package in.bm.MovieService.ResponseDTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import in.bm.MovieService.ENTITY.SeatFeature;
import in.bm.MovieService.ENTITY.SeatStatus;
import in.bm.MovieService.ENTITY.SeatType;
import in.bm.MovieService.ENTITY.ViewType;
import lombok.Builder;
import lombok.Getter;
@JsonPropertyOrder({
        "seatId",
        "seatNumber",
        "seatType",
        "view",
        "feature",
        "price",
        "status"
})
@Builder
@Getter
public class SeatsResponseDTO {


    private long seatId;
    private long showSeatId;
    private String seatNumber;
    private ViewType view;
    private SeatStatus status;
    private double price;
    private SeatFeature feature;
    private SeatType seatType;

}
