package in.bm.MovieService.CONTROLLER;


import in.bm.MovieService.RequestDTO.BookingRequestDTO;
import in.bm.MovieService.RequestDTO.ShowRequestDTO;
import in.bm.MovieService.ResponseDTO.BookingResponseDTO;
import in.bm.MovieService.ResponseDTO.ShowResponseDTO;
import in.bm.MovieService.SERVICE.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/shows")
@RequiredArgsConstructor
public class ShowController{

    private final ShowService showService;

    @PostMapping
    public ResponseEntity<ShowResponseDTO> addShow
            (@Valid @RequestBody ShowRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(showService.addShow(dto));
    }

    // webClient api
    @PostMapping("/booking")
    public ResponseEntity<BookingResponseDTO> previewBooking(@Valid @RequestBody BookingRequestDTO requestDTO){
        return ResponseEntity.status(HttpStatus.OK).body(showService.previewBooking(requestDTO));
    }



}