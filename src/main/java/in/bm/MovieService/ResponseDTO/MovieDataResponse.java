package in.bm.MovieService.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDataResponse {

    private String movieName;
    private List<String> posters;
    private String duration;
    private String movieAvatar;
    private String certification;
    private String language;
    private String synopsis;
    private List<String> movieType;
    private Map<String,String> castAndCrew; // name and image url
    private double avgRating;
    private int totalReviews;







}
