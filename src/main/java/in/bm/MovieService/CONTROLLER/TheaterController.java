package in.bm.MovieService.CONTROLLER;

import in.bm.MovieService.RequestDTO.TheaterReviewRequestDTO;
import in.bm.MovieService.ResponseDTO.*;
import in.bm.MovieService.SERVICE.TheaterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/theaters")
@RequiredArgsConstructor
public class TheaterController {

    private final TheaterService theaterService;


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

    //theaters/reviews/{theaterCode}
    @GetMapping("/reviews/{theaterCode}")
    public ResponseEntity<List<TheaterReviewResponseDTO>> getAllReviews(@PathVariable String theaterCode) {
        return ResponseEntity.ok(theaterService.getAllReviews(theaterCode));
    }

    @GetMapping("/filters")
    public ResponseEntity<TheaterFilterPageResponseDTO> filters(
            @RequestParam(required = true) String movieCode,
            @RequestParam(required = true) String city,

            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate date,

            @RequestParam(required = false) Double seatPrice,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,

            @RequestParam(required = true) double latitude,
            @RequestParam(required = true) double longitude
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(theaterService.searchFilter(
                        movieCode, city, date, seatPrice, page, size, latitude, longitude
                ));
    }

    //theaters/{theaterCode}/reviews
    @PostMapping("/{theaterCode}/reviews")
    public ResponseEntity<TheaterReviewResponseDTO> addReview(
            @PathVariable String theaterCode,
            @Valid @RequestBody TheaterReviewRequestDTO theaterReviewRequestDTO,
            @RequestHeader("x-user-id") String userId) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(theaterService.addReview(theaterCode, theaterReviewRequestDTO, userId));
    }

    //theaters/reviews/{reviewsId}
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<TheaterReviewResponseDTO> editReview(
            @PathVariable long reviewId,
            @Valid @RequestBody TheaterReviewRequestDTO dto,
            @RequestHeader("x-user-id") String userId) {

        return ResponseEntity.ok(
                theaterService.editReview(reviewId, dto, userId));
    }


    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReviewByUser(
            @RequestHeader("x-user-id") String userId,
            @PathVariable Long reviewId) {

        theaterService.deleteReviewByUser(userId, reviewId);
        return ResponseEntity.noContent().build();
    }

}
