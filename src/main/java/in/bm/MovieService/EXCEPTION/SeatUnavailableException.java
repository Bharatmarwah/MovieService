package in.bm.MovieService.EXCEPTION;

public class SeatUnavailableException extends RuntimeException{
    public SeatUnavailableException(String message) {
        super(message);
    }
}
