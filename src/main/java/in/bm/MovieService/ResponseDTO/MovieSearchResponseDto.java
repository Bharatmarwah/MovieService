package in.bm.MovieService.ResponseDTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MovieSearchResponseDto {

    private String movieCode;
    private String movieName;
    private String movieAvatar;
    private String language;
    private String certificate;

}
