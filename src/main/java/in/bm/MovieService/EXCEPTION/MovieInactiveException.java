package in.bm.MovieService.EXCEPTION;

public class MovieInactiveException extends RuntimeException{
    public MovieInactiveException(String message){
        super(message);
    }
}
