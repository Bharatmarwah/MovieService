package in.bm.MovieService.ResponseDTO;

import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
