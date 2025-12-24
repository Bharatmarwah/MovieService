package in.bm.MovieService.EXCEPTION;

import in.bm.MovieService.ENTITY.Screen;

public class ScreenNotFoundException extends RuntimeException{
    public ScreenNotFoundException(String message){
        super(message);
    }
}
