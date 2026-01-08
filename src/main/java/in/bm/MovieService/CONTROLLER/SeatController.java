package in.bm.MovieService.CONTROLLER;

import in.bm.MovieService.RequestDTO.SeatRequestDTO;
import in.bm.MovieService.ResponseDTO.SeatBulkResponseDTO;
import in.bm.MovieService.SERVICE.SeatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/seats")
@RequiredArgsConstructor
public class SeatController{

    private final SeatService seatService;

    @PostMapping
    public ResponseEntity<SeatBulkResponseDTO> addSeats(@Valid @RequestBody SeatRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(seatService.addSeats(dto));
    }


}