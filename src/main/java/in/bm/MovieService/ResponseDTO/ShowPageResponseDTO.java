package in.bm.MovieService.ResponseDTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShowPageResponseDTO {

    private List<ShowResponseDTO> showResponses;
    private int page;
    private int size;
    private long totalElements;
    private boolean hasNext;
    private boolean hasPrevious;
    private int totalPages;

}
