package in.bm.MovieService.CONTROLLER;


import in.bm.MovieService.ResponseDTO.ShowDateTimeResponseDTO;
import in.bm.MovieService.ResponseDTO.ShowSeatsResponse;
import in.bm.MovieService.SERVICE.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/shows")
@RequiredArgsConstructor
public class ShowController {

    private final ShowService showService;

    @GetMapping("/by-movie/{movieCode}")
    public ResponseEntity<List<ShowDateTimeResponseDTO>> getShowsByMovieCode(@PathVariable String movieCode, @RequestParam String city){
        return ResponseEntity.status(HttpStatus.OK).body(showService.getShowsByMovieAndCity(movieCode, city));
    }

    // /{showId}/seats (show id is being fetched by the theater filter)
    @GetMapping("{showId}/seats")
    public ResponseEntity<ShowSeatsResponse> seatsByShowId(@PathVariable Long showId){
        return ResponseEntity.status(HttpStatus.OK).body(showService.seatsByShowId(showId));
    }

}