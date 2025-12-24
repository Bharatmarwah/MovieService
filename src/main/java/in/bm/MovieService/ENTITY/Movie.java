package in.bm.MovieService.ENTITY;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name ="movies",
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"movie_name","language","certificate"}))
public class Movie {

    @Id
    @Column(unique = true)
    private String movieCode;
    private String movieName;
    private String duration;
    private String movieAvatar;
    private String certificate;
    private String language;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovieStatus status = MovieStatus.PENDING;

    @OneToOne(mappedBy = "movie",cascade = CascadeType.ALL, orphanRemoval = true)
    private MovieDetails movieDetails;

    @OneToMany(mappedBy = "movie",cascade = CascadeType.PERSIST)
    private List<Show> shows;

}
