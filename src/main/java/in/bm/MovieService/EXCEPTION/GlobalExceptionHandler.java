package in.bm.MovieService.EXCEPTION;


import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<?> handleDatabaseErrors(DataAccessException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Database error occurred",
                "message", ex.getMostSpecificCause().getMessage(),
                "timestamp", LocalDateTime.now()
        ));
    }


    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<?> handleMovieNullPointerErrors(MovieNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "error", "Movie not Found",
                "message", ex.getMessage(),
                "timestamp", LocalDateTime.now()
        ));
    }

    @ExceptionHandler(TheaterNotFoundException.class)
    public ResponseEntity<?> handleTheaterNullPointerErrors(TheaterNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "error", "Theater not Found",
                "message", ex.getMessage(),
                "timestamp", LocalDateTime.now()
        ));
    }

    @ExceptionHandler(ShowNotFoundException.class)
    public ResponseEntity<?> handleShowTimingsNullPointerErrors(ShowNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "error", "Show timing not Found",
                "message", ex.getMessage(),
                "timestamp", LocalDateTime.now()
        ));
    }

    @ExceptionHandler(SeatNotFoundException.class)
    public ResponseEntity<?> handleSeatsNullPointerErrors(SeatNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "error", "Seat not Found",
                "message", ex.getMessage(),
                "timestamp", LocalDateTime.now()
        ));
    }

    @ExceptionHandler(ScreenNotFoundException.class)
    public ResponseEntity<?> handleScreensNullPointerErrors(ScreenNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "error", "Screen not Found",
                "message", ex.getMessage(),
                "timestamp", LocalDateTime.now()
        ));
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<?> handleReviewNullPointerErrors(ReviewNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "error", "Review not Found",
                "message", ex.getMessage(),
                "timestamp", LocalDateTime.now()
        ));
    }

    @ExceptionHandler(SeatCategoryNotFoundException.class)
    public ResponseEntity<?> handleSeatCategoryNullPointerErrors(SeatCategoryNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "error", "SeatCategory not Found",
                "message", ex.getMessage(),
                "timestamp", LocalDateTime.now()
        ));
    }

    @ExceptionHandler(TheaterInactiveException.class)
    public ResponseEntity<?> handleTheaterInactive(TheaterInactiveException ex) {
        return ResponseEntity.status(HttpStatus.GONE).body(
                Map.of(
                        "error", "THEATER_INACTIVE",
                        "message", ex.getMessage(),
                        "timestamp", LocalDateTime.now()
                )
        );
    }

    @ExceptionHandler(MovieInactiveException.class)
    public ResponseEntity<?> handleMovieInactive(MovieInactiveException ex) {
        return ResponseEntity.status(HttpStatus.GONE).body(
                Map.of(
                        "error", "MOVIE_INACTIVE",
                        "message", ex.getMessage(),
                        "timestamp", LocalDateTime.now()
                )
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleConflict(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                Map.of(
                        "error", "STATE_CONFLICT",
                        "message", ex.getMessage(),
                        "timestamp", LocalDateTime.now()
                )
        );
    }


    @ExceptionHandler(InvalidSeatException.class)
    public ResponseEntity<?> handleInvalidSeat(InvalidSeatException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of(
                        "error", "INVALID_SEAT",
                        "message", ex.getMessage(),
                        "timestamp", LocalDateTime.now()
                )
        );
    }


    @ExceptionHandler(SeatUnavailableException.class)
    public ResponseEntity<?> handleUnavailableSeat(SeatUnavailableException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                Map.of(
                        "error", "SEAT_UNAVAILABLE",
                        "message", ex.getMessage(),
                        "timestamp", LocalDateTime.now()
                )
        );
    }

    @ExceptionHandler(SeatInActiveException.class)
    public ResponseEntity<?> handleSeatNotActive(SeatInActiveException ex) {
        return ResponseEntity.status(HttpStatus.GONE).body(
                Map.of(
                        "error", "SEAT_INACTIVE",
                        "message", ex.getMessage(),
                        "timestamp", LocalDateTime.now()
                )
        );
    }

    @ExceptionHandler(SeatActiveException.class)
    public ResponseEntity<?> handleSeatActive(SeatActiveException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                Map.of(
                        "error", "SEAT_ACTIVE",
                        "message", ex.getMessage(),
                        "timestamp", LocalDateTime.now()
                )
        );

    }
    @ExceptionHandler(ScreenInActiveException.class)
    public ResponseEntity<?> handleScreenInActive(ScreenInActiveException ex) {
        return ResponseEntity.status(HttpStatus.GONE).body(
                Map.of(
                        "error", "SCREEN_INACTIVE",
                        "message", ex.getMessage(),
                        "timestamp", LocalDateTime.now()
                )
        );

    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<?> handleUnauthorized(UnauthorizedActionException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                Map.of(
                        "error", "FORBIDDEN",
                        "message", ex.getMessage(),
                        "timestamp", LocalDateTime.now()
                )
        );
    }


}
