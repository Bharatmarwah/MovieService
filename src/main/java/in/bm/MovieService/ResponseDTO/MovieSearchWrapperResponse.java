package in.bm.MovieService.ResponseDTO;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MovieSearchWrapperResponse {
    private List<MovieSearchResponseDto> movies;
    private String message;
}