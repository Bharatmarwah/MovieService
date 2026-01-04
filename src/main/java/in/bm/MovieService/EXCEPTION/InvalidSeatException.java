package in.bm.MovieService.EXCEPTION;

public class InvalidSeatException extends RuntimeException {
    public InvalidSeatException(String message) {
        super(message);
    }
}
