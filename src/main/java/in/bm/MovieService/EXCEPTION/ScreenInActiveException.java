package in.bm.MovieService.EXCEPTION;

public class ScreenInActiveException extends RuntimeException {
    public ScreenInActiveException(String message) {
        super(message);
    }
}
