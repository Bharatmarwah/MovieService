package in.bm.MovieService.ResponseDTO;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDetailsResponseDTO {

    private long id;
    private String synopsis;
    private List<MovieReviewDto> reviews;
    private Map<String,String> castAndCrew;
    private List<String> movieType;
    private List<String> poster;
    private double avgRating;
    private int totalReviews;



}
