package in.bm.MovieService.CONTROLLER;

import in.bm.MovieService.RequestDTO.MovieRequestDTO;
import in.bm.MovieService.RequestDTO.MovieReviewRequestDTO;
import in.bm.MovieService.ResponseDTO.*;
import in.bm.MovieService.SERVICE.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    //movies?page=0&size=10
    @GetMapping
    public ResponseEntity<MoviePageResponseDTO> getMovies
    (@RequestParam int page,
     @RequestParam int size) {
        MoviePageResponseDTO response = movieService.getMovies(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //{movieCode}/details
    @GetMapping("/{movieCode}/details")
    public ResponseEntity<MovieDetailsResponseDTO> getMovieDetails(@PathVariable String movieCode) {
        MovieDetailsResponseDTO response = movieService.getMovieDetails(movieCode);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //movies/add
    @PostMapping
    public ResponseEntity<MovieInfoDTO> addMovies(@Valid @RequestBody MovieRequestDTO movieRequestDTO) {
        MovieInfoDTO response = movieService.addMovie(movieRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //localhost:8080/movies/search?q={}&page=0&size=10
    @GetMapping("/search")
    public ResponseEntity<MoviePageResponseDTO> searchMovie(@RequestParam String q,
                                                            @RequestParam int page,
                                                            @RequestParam int size) {

        MoviePageResponseDTO response = movieService.searchMovie(q, page, size);

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    //movies/{movieCode}
    @GetMapping("/{movieCode}")
    public ResponseEntity<MovieResponseDTO> getMovieId(@PathVariable String movieCode) {
        MovieResponseDTO response = movieService.getMovieById(movieCode);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Only called when booking status=COMPLETED & showEndTIme< now
    @PostMapping("/{movieCode}/reviews")
    public ResponseEntity<MovieReviewDto> addReview(@PathVariable String movieCode, @Valid @RequestBody MovieReviewRequestDTO movieReviewRequestDTO) {
        MovieReviewDto response = movieService.addReview(movieCode, movieReviewRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{movieCode}")
    public ResponseEntity<MovieInfoDTO> updateMovie(@PathVariable String movieCode, @Valid @RequestBody MovieRequestDTO movieRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(movieService.updateMovie(movieCode, movieRequestDTO));
    }

    @PatchMapping("/{movieCode}/deactivate")
    public ResponseEntity<MovieStatusDTO> deactivate(@PathVariable String movieCode) {
        return ResponseEntity.status(HttpStatus.OK).body(movieService.deactivate(movieCode));
    }

    @PatchMapping("/{movieCode}/activate")
    public ResponseEntity<MovieStatusDTO> activate(@PathVariable String movieCode) {
        return ResponseEntity.status(HttpStatus.OK).body(movieService.activate(movieCode));
    }

    //filter

}
