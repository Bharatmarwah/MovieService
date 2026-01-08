package in.bm.MovieService.RequestDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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
