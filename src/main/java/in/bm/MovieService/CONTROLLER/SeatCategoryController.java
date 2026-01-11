package in.bm.MovieService.CONTROLLER;

import in.bm.MovieService.RequestDTO.SeatCategoryRequestDTO;
import in.bm.MovieService.ResponseDTO.SeatCategoryPageResponseDTO;
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

    @PatchMapping("/{seatCategoryId}")
    public ResponseEntity<SeatCategoryResponseDTO> updateSeatCategory(@Valid @RequestBody SeatCategoryRequestDTO dto, @PathVariable Long seatCategoryId) {
        return ResponseEntity.status(HttpStatus.OK).body(seatCategoryService.updateSeatCategory(dto,seatCategoryId));
    }

    @DeleteMapping("/{seatCategoryId}")
    public ResponseEntity<Void> deleteSeatCategory(@PathVariable Long seatCategoryId){
        seatCategoryService.deleteSeatCategory(seatCategoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{seatCategoryId}")
    public ResponseEntity<SeatCategoryResponseDTO> getSeatCategoryById(@PathVariable Long seatCategoryId){
        return ResponseEntity.status(HttpStatus.OK).body(seatCategoryService.getSeatCategoryById(seatCategoryId));
    }

    @GetMapping
    public ResponseEntity<SeatCategoryPageResponseDTO> getAllSeatCategories(@RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.status(HttpStatus.OK).body(seatCategoryService.getAllSeatCategories(page,size));

    }
}