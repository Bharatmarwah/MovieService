package in.bm.MovieService.CONTROLLER;

import in.bm.MovieService.RequestDTO.SeatCategoryRequestDTO;
import in.bm.MovieService.ResponseDTO.SeatCategoryResponseDTO;
import in.bm.MovieService.SERVICE.SeatCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/seat-category")
@RequiredArgsConstructor
public class SeatCategoryController {

    private final SeatCategoryService seatCategoryService;

    @PostMapping
    public ResponseEntity<SeatCategoryResponseDTO> addSeatCategory(@Valid @RequestBody SeatCategoryRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seatCategoryService.addSeatCategory(dto));
    }

}
