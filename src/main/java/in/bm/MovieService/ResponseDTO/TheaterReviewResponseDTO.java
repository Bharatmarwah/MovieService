package in.bm.MovieService.ResponseDTO;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TheaterReviewResponseDTO {

    private long id;
    private String username;
    private String comment;
    private double rating;
    private Instant createdAt;


}
