package in.bm.MovieService.CONTROLLER;

import in.bm.MovieService.RequestDTO.SeatCategoriesRequestDTO;
import in.bm.MovieService.RequestDTO.AddSeatRequestDTO;
import in.bm.MovieService.RequestDTO.SeatRequestDTO;
import in.bm.MovieService.ResponseDTO.*;
import in.bm.MovieService.SERVICE.SeatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/seats")
@RequiredArgsConstructor
public class SeatController{

    private final SeatService seatService;

    @PostMapping
    public ResponseEntity<SeatBulkResponseDTO> addSeats(@Valid @RequestBody AddSeatRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(seatService.addSeats(dto));
    }

    @PutMapping("/{seatId}")
    public ResponseEntity<SeatResponseDTO> updateSeat(@Valid @RequestBody SeatRequestDTO requestDTO,@PathVariable Long seatId){
        return ResponseEntity.status(HttpStatus.OK).body(seatService.updateSeat(seatId, requestDTO));
    }

    @PatchMapping("/{seatId}")
    public ResponseEntity<SeatCategoriesResponseDTO> updateSeatCategories(@Valid @RequestBody SeatCategoriesRequestDTO requestDTO , @PathVariable Long seatsId){
        return ResponseEntity.status(HttpStatus.OK).body(seatService.updateSeatCategories(seatsId,requestDTO));
    }

    @PatchMapping("/{seatId}/deactivate")
    public ResponseEntity<SeatLifeCycleResponseDTO> deactivateSeat(@PathVariable Long seatId){
        return ResponseEntity.status(HttpStatus.OK).body(seatService.deactivateSeat(seatId));
    }

    @PatchMapping("/{seatId}/activate")
    public ResponseEntity<SeatLifeCycleResponseDTO> activateSeat(@PathVariable Long seatId){
        return ResponseEntity.status(HttpStatus.OK).body(seatService.activateSeat(seatId));
    }

    @GetMapping("/{seatId}")
    public ResponseEntity<SeatResponseDTO> getSeatsById(@PathVariable Long seatId){
        return ResponseEntity.status(HttpStatus.OK).body(seatService.getSeatById(seatId));
    }

    @GetMapping
    public ResponseEntity<SeatPageResponseDTO> getAllSeats(@RequestParam(defaultValue = "0") int page , @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.status(HttpStatus.OK).body(seatService.getAllSeats(page,size));
    }

    @DeleteMapping("/{seatId}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long seatId){
        seatService.deleteSeat(seatId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}