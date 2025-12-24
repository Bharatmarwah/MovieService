package in.bm.MovieService.EXCEPTION;

public class TheaterNotFoundException extends RuntimeException{
    public TheaterNotFoundException(String message){
        super(message);
    }
}
