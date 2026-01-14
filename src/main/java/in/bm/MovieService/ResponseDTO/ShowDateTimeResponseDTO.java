package in.bm.MovieService.ResponseDTO;

import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class ShowDateTimeResponseDTO {

    public long showId;
    private DayOfWeek dayOfWeek;

    private LocalDate showDate;

    private LocalTime showTime;

    private String meridiem;

}
