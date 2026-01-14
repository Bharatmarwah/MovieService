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

    @PostMapping
    public ResponseEntity<ShowResponseDTO> addShow
            (@Valid @RequestBody ShowRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(showService.addShow(dto));
    }

    @PutMapping("/{showId}")
    public ResponseEntity<ShowResponseDTO> updateShow(@Valid @RequestBody ShowRequestDTO requestDTO, @PathVariable Long showId) {
        return ResponseEntity.status(HttpStatus.OK).body(showService.updateShow(requestDTO, showId));
    }

    @DeleteMapping("/{showId}")
    public ResponseEntity<Void> deleteShow(@PathVariable Long showId) {
        showService.deleteShow(showId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/by-movie/{movieCode}")
    public ResponseEntity<List<ShowDateTimeResponseDTO>> getShowsByMovieCode(@PathVariable String movieCode){
        return ResponseEntity.status(HttpStatus.OK).body(showService.getShowsByMovieCode(movieCode));
    }


// ADMIN APIS
    @GetMapping
    public ResponseEntity<ShowPageResponseDTO> getAllShows(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.status(HttpStatus.OK).body(showService.getAllShow(page,size));
    }

    @GetMapping("/{showId}")
    public ResponseEntity<ShowResponseDTO> getShowById(@PathVariable Long showId){
        return ResponseEntity.status(HttpStatus.OK).body(showService.getShowById(showId));
    }


    // webClient api
    @PostMapping("/booking")
    public ResponseEntity<BookingResponseDTO> previewBooking(@Valid @RequestBody BookingRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(showService.previewBooking(requestDTO));
    }


}