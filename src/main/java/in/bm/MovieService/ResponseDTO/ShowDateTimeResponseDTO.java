package in.bm.MovieService.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShowDateTimeResponseDTO {

    public long showId;

    private DayOfWeek dayOfWeek;

    private LocalDate showDate;

    private LocalTime showTime;

    private String meridiem;

}
