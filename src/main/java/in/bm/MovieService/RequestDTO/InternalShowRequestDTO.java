package in.bm.MovieService.RequestDTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InternalShowRequestDTO {
    @NotNull
    private Long showId;

    @NotNull
    private List<Long> showSeatIds;
}
