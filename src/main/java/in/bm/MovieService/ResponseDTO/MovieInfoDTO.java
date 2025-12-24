package in.bm.MovieService.ResponseDTO;

import in.bm.MovieService.ENTITY.MovieStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class MovieInfoDTO {

    private String movieCode;
    private String movieName;
    private String movieAvatar;
    private String certificate;
    private String language;
    private String duration;

    private String synopsis;
    private Map<String, String> castAndCrew;
    private List<String> posters;
    private List<String> movieType;
    private List<MovieReviewDto> reviews;

    private MovieStatus status;
}
