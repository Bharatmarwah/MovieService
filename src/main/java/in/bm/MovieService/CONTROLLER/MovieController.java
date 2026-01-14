package in.bm.MovieService.CONTROLLER;


import in.bm.MovieService.RequestDTO.MovieReviewRequestDTO;
import in.bm.MovieService.ResponseDTO.*;
import in.bm.MovieService.SERVICE.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    //movies?page=0&size=10
    @GetMapping
    public ResponseEntity<MoviePageResponseDTO> getMovies
    (@RequestParam(defaultValue = "0") int page,
     @RequestParam(defaultValue = "10") int size) {
        MoviePageResponseDTO response = movieService.getMovies(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //{movieCode}/details
    @GetMapping("/{movieCode}/details")
    public ResponseEntity<MovieDetailsResponseDTO> getMovieDetails(@PathVariable String movieCode) {
        MovieDetailsResponseDTO response = movieService.getMovieDetails(movieCode);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<MoviePageResponseDTO> searchMovie(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        MoviePageResponseDTO response = movieService.searchMovie(q, page, size);
        return ResponseEntity.ok(response);
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

}