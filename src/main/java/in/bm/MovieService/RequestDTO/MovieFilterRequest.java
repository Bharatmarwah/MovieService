package in.bm.MovieService.RequestDTO;

import lombok.Data;

import java.util.List;

@Data
public class MovieFilterRequest {

    private String movieName;
    private String certificate;
    private String language;
    private List<String> movieType;
    private List<String> castOrCrewsNames;
    private String status;
    private String sort;
}