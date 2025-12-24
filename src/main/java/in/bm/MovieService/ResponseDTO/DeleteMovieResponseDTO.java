package in.bm.MovieService.ResponseDTO;

import lombok.*;

@Getter@Setter@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteMovieResponseDTO {
    private String movieCode;
    private String message;

}
