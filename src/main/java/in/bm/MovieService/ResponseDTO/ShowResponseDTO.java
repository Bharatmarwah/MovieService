package in.bm.MovieService.ResponseDTO;

import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class ShowResponseDTO {

    private Long showId;

    private String movieCode;

    private Long screenId;

    private DayOfWeek dayOfWeek;

    private LocalDate showDate;

    private LocalTime showTime;

    private String meridiem;








}
