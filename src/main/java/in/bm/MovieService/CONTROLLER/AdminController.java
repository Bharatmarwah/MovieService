package in.bm.MovieService.CONTROLLER;

import in.bm.MovieService.ENTITY.TheaterStatus;
import in.bm.MovieService.RequestDTO.*;
import in.bm.MovieService.ResponseDTO.*;
import in.bm.MovieService.SERVICE.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MovieService movieService;
    private final ShowService showService;
    private final ScreenService screenService;
    private final TheaterService theaterService;
    private final SeatCategoryService seatCategoryService;
    private final SeatService seatService;

    // ================= MOVIES =================

    @PostMapping("/movies")
    public ResponseEntity<MovieInfoDTO> addMovies(
            @Valid @RequestBody MovieRequestDTO movieRequestDTO) {

        MovieInfoDTO response = movieService.addMovie(movieRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/movies/{movieCode}")
    public ResponseEntity<MovieInfoDTO> updateMovie(
            @PathVariable String movieCode,
            @Valid @RequestBody MovieRequestDTO movieRequestDTO) {

        return ResponseEntity.ok(
                movieService.updateMovie(movieCode, movieRequestDTO));
    }

    @PatchMapping("/movies/{movieCode}/deactivate")
    public ResponseEntity<MovieStatusDTO> deactivate(
            @PathVariable String movieCode) {

        return ResponseEntity.ok(movieService.deactivate(movieCode));
    }

    @PatchMapping("/movies/{movieCode}/activate")
    public ResponseEntity<MovieStatusDTO> activate(
            @PathVariable String movieCode) {

        return ResponseEntity.ok(movieService.activate(movieCode));
    }

    // ================= SCREENS =================

    @PostMapping("/screens")
    public ResponseEntity<ScreenResponseDTO> addScreen(
            @Valid @RequestBody ScreenRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(screenService.createScreen(dto));
    }

    @PutMapping("/screens/{screenId}")
    public ResponseEntity<ScreenResponseDTO> updateScreen(
            @Valid @RequestBody ScreenRequestDTO requestDTO,
            @PathVariable Long screenId) {

        return ResponseEntity.ok(
                screenService.updateScreenDetails(requestDTO, screenId));
    }

    @PatchMapping("/screens/{screenId}/deactivate")
    public ResponseEntity<ScreenStatusDTO> deactivateScreen(
            @PathVariable Long screenId) {

        return ResponseEntity.ok(
                screenService.deactivateScreen(screenId));
    }

    @PatchMapping("/screens/{screenId}/activate")
    public ResponseEntity<ScreenStatusDTO> activateScreen(
            @PathVariable Long screenId) {

        return ResponseEntity.ok(
                screenService.activateScreen(screenId));
    }

    @PatchMapping("/screens/{screenId}/retire")
    public ResponseEntity<ScreenStatusDTO> retireScreen(
            @PathVariable Long screenId) {

        return ResponseEntity.ok(
                screenService.retireScreen(screenId));
    }

    // ================= SEAT CATEGORIES =================

    @PostMapping("/seat-categories")
    public ResponseEntity<SeatCategoryResponseDTO> addSeatCategory(
            @Valid @RequestBody SeatCategoryRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(seatCategoryService.addSeatCategory(dto));
    }

    @PatchMapping("/seat-categories/{seatCategoryId}")
    public ResponseEntity<SeatCategoryResponseDTO> updateSeatCategory(
            @Valid @RequestBody SeatCategoryRequestDTO dto,
            @PathVariable Long seatCategoryId) {

        return ResponseEntity.ok(
                seatCategoryService.updateSeatCategory(dto, seatCategoryId));
    }

    @DeleteMapping("/seat-categories/{seatCategoryId}")
    public ResponseEntity<Void> deleteSeatCategory(
            @PathVariable Long seatCategoryId) {

        seatCategoryService.deleteSeatCategory(seatCategoryId);
        return ResponseEntity.noContent().build();
    }

    // ================= SEATS =================

    @PostMapping("/seats")
    public ResponseEntity<SeatBulkResponseDTO> addSeats(
            @Valid @RequestBody AddSeatRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(seatService.addSeats(dto));
    }

    @PutMapping("/seats/{seatId}")
    public ResponseEntity<SeatResponseDTO> updateSeat(
            @Valid @RequestBody SeatRequestDTO requestDTO,
            @PathVariable Long seatId) {

        return ResponseEntity.ok(
                seatService.updateSeat(seatId, requestDTO));
    }

    @PatchMapping("/seats/{seatId}/categories")
    public ResponseEntity<SeatCategoriesResponseDTO> updateSeatCategories(
            @Valid @RequestBody SeatCategoriesRequestDTO requestDTO,
            @PathVariable Long seatId) {

        return ResponseEntity.ok(
                seatService.updateSeatCategories(seatId, requestDTO));
    }

    @PatchMapping("/seats/{seatId}/deactivate")
    public ResponseEntity<SeatLifeCycleResponseDTO> deactivateSeat(
            @PathVariable Long seatId) {

        return ResponseEntity.ok(
                seatService.deactivateSeat(seatId));
    }

    @PatchMapping("/seats/{seatId}/activate")
    public ResponseEntity<SeatLifeCycleResponseDTO> activateSeat(
            @PathVariable Long seatId) {

        return ResponseEntity.ok(
                seatService.activateSeat(seatId));
    }

    @DeleteMapping("/seats/{seatId}")
    public ResponseEntity<Void> deleteSeat(
            @PathVariable Long seatId) {

        seatService.deleteSeat(seatId);
        return ResponseEntity.noContent().build();
    }

    // ================= SHOWS =================

    @PostMapping("/shows")
    public ResponseEntity<ShowResponseDTO> addShow(
            @Valid @RequestBody ShowRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(showService.addShow(dto));
    }

    @PutMapping("/shows/{showId}")
    public ResponseEntity<ShowResponseDTO> updateShow(
            @Valid @RequestBody ShowRequestDTO requestDTO,
            @PathVariable Long showId) {

        return ResponseEntity.ok(
                showService.updateShow(requestDTO, showId));
    }

    @DeleteMapping("/shows/{showId}")
    public ResponseEntity<Void> deleteShow(
            @PathVariable Long showId) {

        showService.deleteShow(showId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/shows")
    public ResponseEntity<ShowPageResponseDTO> getAllShows(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                showService.getAllShow(page, size));
    }

    @GetMapping("/shows/{showId}")
    public ResponseEntity<ShowResponseDTO> getShowById(
            @PathVariable Long showId) {

        return ResponseEntity.ok(
                showService.getShowById(showId));
    }

    // ================= THEATERS =================

    @PostMapping("/theaters")
    public ResponseEntity<TheaterInfoDTO> addTheater(
            @Valid @RequestBody TheaterRequestDto theaterRequestDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(theaterService.addTheater(theaterRequestDto));
    }

    @PutMapping("/theaters/{theaterCode}")
    public ResponseEntity<TheaterInfoDTO> updateTheater(
            @PathVariable String theaterCode,
            @Valid @RequestBody TheaterRequestDto dto) {

        return ResponseEntity.ok(
                theaterService.updateTheater(theaterCode, dto));
    }

    //theaters/reviews/{reviewsId}
    @DeleteMapping("/theater-reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable long reviewId) {

        theaterService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
    // todo delete only user review by user

    //theaters/{theaterCode}/deactivate
    @PatchMapping("/theaters/{theaterCode}/deactivate")
    public ResponseEntity<TheaterStatusDTO> deactivateTheater(
            @PathVariable String theaterCode) {

        return ResponseEntity.ok(
                theaterService.deactivate(theaterCode));
    }

    //theaters/{theaterCode}/activate
    @PatchMapping("/theaters/{theaterCode}/activate")
    public ResponseEntity<TheaterStatusDTO> activateTheater(
            @PathVariable String theaterCode) {

        return ResponseEntity.ok(
                theaterService.activate(theaterCode));
    }

    @GetMapping("/theaters")
    public ResponseEntity<TheaterPageResponseDTO> getTheatersByStatus(
            @RequestParam TheaterStatus status,
            @RequestParam int page,
            @RequestParam int size) {

        return ResponseEntity.ok(
                theaterService.getTheaterByStatus(status, page, size));
    }

    // ================= SEAT CATEGORIES =================

    @GetMapping("/seat-categories/{seatCategoryId}")
    public ResponseEntity<SeatCategoryResponseDTO> getSeatCategoryById(
            @PathVariable Long seatCategoryId) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(seatCategoryService.getSeatCategoryById(seatCategoryId));
    }

    @GetMapping("/seat-categories")
    public ResponseEntity<SeatCategoryPageResponseDTO> getAllSeatCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(seatCategoryService.getAllSeatCategories(page, size));
    }
    // ================= SEATS (ADMIN) =================

    @GetMapping("/seats/{seatId}")
    public ResponseEntity<SeatResponseDTO> getSeatById(
            @PathVariable Long seatId) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(seatService.getSeatById(seatId));
    }

    @GetMapping("/seats")
    public ResponseEntity<SeatPageResponseDTO> getAllSeats(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(seatService.getAllSeats(page, size));
    }


}
