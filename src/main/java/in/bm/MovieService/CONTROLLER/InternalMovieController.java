package in.bm.MovieService.CONTROLLER;

import in.bm.MovieService.RequestDTO.MovieFilterRequest;
import in.bm.MovieService.ResponseDTO.MovieDataResponse;
import in.bm.MovieService.SERVICE.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/movies")
public class InternalMovieController {

    private final MovieService movieService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MovieDataResponse> getMovies(){
        return movieService.fetchAllMoviesData();
    }

    @GetMapping("/{movieCode}")
    @ResponseStatus(HttpStatus.OK)
    public MovieDataResponse getMovieByCode(@PathVariable String movieCode) {
        return movieService.fetchMovieDataByCode(movieCode);
    }

    @GetMapping("/filter")
    @ResponseStatus(HttpStatus.OK)
    public List<MovieDataResponse> filterMovies(@ModelAttribute MovieFilterRequest movieFilterRequest
    ) {
        return movieService.fetchMoviesData(movieFilterRequest);
    }
}