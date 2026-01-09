package in.bm.MovieService.CONTROLLER;

import in.bm.MovieService.ENTITY.SeatCategory;
import in.bm.MovieService.RequestDTO.SeatCategoryRequestDTO;
import in.bm.MovieService.ResponseDTO.SeatCategoryResponseDTO;
import in.bm.MovieService.SERVICE.SeatCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/seat-category")
@RequiredArgsConstructor
public class SeatCategoryController {

    private final SeatCategoryService seatCategoryService;

    @PostMapping
    public ResponseEntity<SeatCategoryResponseDTO> addSeatCategory(@Valid @RequestBody SeatCategoryRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seatCategoryService.addSeatCategory(dto));
    }

    @PutMapping("/{seatCategoryId}")
    public ResponseEntity<SeatCategoryResponseDTO> updateSeatCategory(@Valid @RequestBody SeatCategoryRequestDTO dto, @PathVariable Long seatCategory) {
        return ResponseEntity.status(HttpStatus.OK).body(seatCategoryService.updateSeatCategory(dto,seatCategory));
    }

    @DeleteMapping("/{seatCategoryId}")
    public ResponseEntity<Void> deleteSeatCategory(@PathVariable Long seatCategoryId){
        seatCategoryService.deleteSeatCategory(seatCategoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}