package in.bm.MovieService.RequestDTO;

import in.bm.MovieService.ENTITY.TheaterStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

    @NotNull
    private TheaterStatus status;

    @NotEmpty
    private List<@NotBlank String> servicesAndAmenities;
}
