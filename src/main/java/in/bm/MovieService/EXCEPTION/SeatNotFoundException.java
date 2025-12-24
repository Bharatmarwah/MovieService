package in.bm.MovieService.EXCEPTION;



public class SeatNotFoundException extends RuntimeException{
    public SeatNotFoundException(String message){
        super(message);
    }
}
