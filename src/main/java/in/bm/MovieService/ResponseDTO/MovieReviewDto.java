package in.bm.MovieService.ResponseDTO;

import lombok.*;

import java.time.Instant;


@Getter
@Builder
public class MovieReviewDto {

    private long id;
    private String username;
    private double rating;
    private String comment;
    private Instant createdAt;

}
