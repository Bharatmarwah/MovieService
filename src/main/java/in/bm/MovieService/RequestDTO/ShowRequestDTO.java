package in.bm.MovieService.RequestDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ShowRequestDTO {

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate showDate;

    @NotNull
    @JsonFormat(pattern = "HH:mm")
    private LocalTime showTime;

    @NotNull
    private Long screenId;

    @NotBlank
    private String movieCode;
}
