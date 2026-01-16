package in.bm.MovieService.ResponseDTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class TheaterResponseDTO {

    private String theaterCode;
    private String brand;
    private String branchName;
    private String city;
    private double distanceKm;
    private boolean allowsCancellation;
    private double avgRating;
    private List<ShowTimeResponseDTO> showTimeResponse;



}
