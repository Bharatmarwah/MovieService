package in.bm.MovieService.ResponseDTO;

import lombok.*;

import java.util.List;


@Getter
@Builder
public class TheaterDetailsResponseDTO {

    private long theaterDetailsId;
    private String brand;
    private String branchName;
    private String area;
    private String city;
    private String roadOrMall;
    private String cityPinCode;
    private String country;
    private List<String> servicesAndAmenities;



}
