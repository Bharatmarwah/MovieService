package in.bm.MovieService.CONTROLLER;

import in.bm.MovieService.ENTITY.TheaterStatus;
import in.bm.MovieService.RequestDTO.TheaterRequestDto;
import in.bm.MovieService.RequestDTO.TheaterReviewRequestDTO;
import in.bm.MovieService.ResponseDTO.*;
import in.bm.MovieService.SERVICE.TheaterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;


@RestController
@RequestMapping("/theaters")
@RequiredArgsConstructor
public class TheaterController {

    private final TheaterService theaterService;


    @PostMapping
    public ResponseEntity<TheaterInfoDTO> addTheater(
            @Valid @RequestBody TheaterRequestDto theaterRequestDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(theaterService.addTheater(theaterRequestDto));
    }

    //theaters/{theaterCode}/reviews
    @PostMapping("/{theaterCode}/reviews")
    public ResponseEntity<TheaterReviewResponseDTO> addReview(
            @PathVariable String theaterCode,
            @Valid @RequestBody TheaterReviewRequestDTO theaterReviewRequestDTO) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(theaterService.addReview(theaterCode, theaterReviewRequestDTO));
    }

    // theaters/{theaterCode}
    @GetMapping("/{theaterCode}")
    public ResponseEntity<TheaterResponseDTO> getTheaterById(
            @PathVariable String theaterCode,
            @RequestParam double latitude,
            @RequestParam double longitude) {

        return ResponseEntity.ok(
                theaterService.getTheaterById(theaterCode, latitude, longitude));
    }

    //theaters/{theaterCode}/details
    @GetMapping("/{theaterCode}/details")
    public ResponseEntity<TheaterDetailsResponseDTO> getTheaterDetailsById(
            @PathVariable String theaterCode) {

        return ResponseEntity.ok(
                theaterService.getTheaterDetailsById(theaterCode));
    }

    //theaters/{theaterCode}
    @PutMapping("/{theaterCode}")
    public ResponseEntity<TheaterInfoDTO> updateTheater(
            @PathVariable String theaterCode,
            @Valid @RequestBody TheaterRequestDto dto) {

        return ResponseEntity.ok(
                theaterService.updateTheater(theaterCode, dto));
    }

    //theaters/reviews/{reviewsId}
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<TheaterReviewResponseDTO> editReview(
            @PathVariable long reviewId,
            @Valid @RequestBody TheaterReviewRequestDTO dto) {

        return ResponseEntity.ok(
                theaterService.editReview(reviewId, dto));
    }

    //theaters/reviews
    @GetMapping("/reviews")
    public ResponseEntity<List<TheaterReviewResponseDTO>> getAllReviews() {
        return ResponseEntity.ok(theaterService.getAllReviews());
    }

    // ADMIN CONTROLLER APIS

    //theaters/reviews/{reviewsId}
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable long reviewId) {
        theaterService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    //theaters/{theaterCode}/deactivate
    @PatchMapping("/{theaterCode}/deactivate")
    public ResponseEntity<TheaterStatusDTO> deactivate(
            @PathVariable String theaterCode) {

        return ResponseEntity.ok(theaterService.deactivate(theaterCode));
    }

    //theaters/{theaterCode}/activate
    @PatchMapping("/{theaterCode}/activate")
    public ResponseEntity<TheaterStatusDTO> activate(
            @PathVariable String theaterCode) {

        return ResponseEntity.ok(theaterService.activate(theaterCode));
    }

    @GetMapping
    public ResponseEntity<TheaterPageResponseDTO> getTheatersByStatus(
            @RequestParam TheaterStatus status,
            @RequestParam int page,
            @RequestParam int size) {

        return ResponseEntity.ok(
                theaterService.getTheaterByStatus(status, page, size));
    }

    @GetMapping("/filters")
    public ResponseEntity<TheaterFilterPageResponseDTO> filters(
            @RequestParam String movieCode,
            @RequestParam(required = false) String city,

            @RequestParam(required = false)
            @DateTimeFormat(pattern = "HH:mm")
            LocalTime time,

            @RequestParam(required = false) Double seatPrice,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(theaterService.searchFilter(
                        movieCode, city, time, seatPrice, page, size
                ));
    }

}
