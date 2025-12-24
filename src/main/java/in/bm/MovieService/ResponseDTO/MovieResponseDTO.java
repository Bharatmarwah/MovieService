package in.bm.MovieService.ResponseDTO;


import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieResponseDTO {

    private String movieCode;
    private String movieName;
    private String movieAvatar;
    private String certificate;
    private String language;

}
