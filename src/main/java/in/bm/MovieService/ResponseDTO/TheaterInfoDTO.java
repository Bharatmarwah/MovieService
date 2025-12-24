package in.bm.MovieService.ResponseDTO;

import in.bm.MovieService.ENTITY.TheaterStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TheaterInfoDTO {

    private String theaterCode;
    private String brand;
    private String branchName;
    private String city;
    private String area;

    private Double latitude;
    private Double longitude;

    private String roadOrMall;
    private String cityPinCode;
    private String country;

    private Boolean allowsCancellation;

    private TheaterStatus status;

    private List<String> servicesAndAmenities;
}
