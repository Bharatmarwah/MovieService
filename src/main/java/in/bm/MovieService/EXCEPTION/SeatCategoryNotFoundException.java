package in.bm.MovieService.EXCEPTION;

public class SeatCategoryNotFoundException extends RuntimeException{
    public SeatCategoryNotFoundException(String message){
        super(message);
    }
}
