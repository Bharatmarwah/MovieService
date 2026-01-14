package in.bm.MovieService.CONTROLLER;


import in.bm.MovieService.RequestDTO.BookingRequestDTO;
import in.bm.MovieService.RequestDTO.ShowRequestDTO;
import in.bm.MovieService.ResponseDTO.BookingResponseDTO;
import in.bm.MovieService.ResponseDTO.ShowDateTimeResponseDTO;
import in.bm.MovieService.ResponseDTO.ShowPageResponseDTO;
import in.bm.MovieService.ResponseDTO.ShowResponseDTO;
import in.bm.MovieService.SERVICE.ShowService;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<ShowDateTimeResponseDTO>> getShowsByMovieCode(@PathVariable String movieCode){
        return ResponseEntity.status(HttpStatus.OK).body(showService.getShowsByMovieCode(movieCode));
    }

    // webClient api
    @PostMapping("/booking")
    public ResponseEntity<BookingResponseDTO> previewBooking(@Valid @RequestBody BookingRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(showService.previewBooking(requestDTO));
    }


}