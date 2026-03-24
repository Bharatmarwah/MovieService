package in.bm.MovieService.ResponseDTO;

import lombok.Builder;

import java.util.List;

@Builder
public class MovieSearchWrapperResponse {
    private List<MovieSearchResponseDto> movies;
    private String message;
}