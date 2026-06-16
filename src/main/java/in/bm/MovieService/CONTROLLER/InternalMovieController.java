package in.bm.MovieService.controller;

import in.bm.MovieService.RequestDTO.MovieFilterRequest;
import in.bm.MovieService.ResponseDTO.MovieDataResponse;
import in.bm.MovieService.SERVICE.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/movies")
public class InternalMovieController {

    private final MovieService movieService;

    @GetMapping("/filter")
    public List<MovieDataResponse> filterMovies(@ModelAttribute MovieFilterRequest movieFilterRequest
                                                ) {
        return movieService.fetchMoviesData(movieFilterRequest);
    }
}