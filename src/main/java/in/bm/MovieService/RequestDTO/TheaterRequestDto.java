package in.bm.MovieService.RequestDTO;

import in.bm.MovieService.ENTITY.TheaterStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.util.List;

@Data
public class TheaterRequestDto {

    @NotBlank
    private String brand;

    @NotBlank
    private String branchName;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotNull
    private Boolean allowsCancellation;

    @NotBlank
    private String roadOrMall;

    @NotBlank
    private String cityPinCode;

    @NotBlank
    private String country;

    @NotBlank
    private String city;

    @NotBlank
    private String area;


    private TheaterStatus status;

    @NotEmpty
    private List<@NotBlank String> servicesAndAmenities;
}
