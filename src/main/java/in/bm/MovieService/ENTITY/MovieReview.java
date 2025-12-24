package in.bm.MovieService.ENTITY;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reviews")
public class MovieReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private double rating;
    @Column(length = 2000)
    private String comment;
    private Instant createdAt;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "movie_details_id")
    private MovieDetails movieDetails;

}
