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
@Table(name = "theater_reviews")
public class TheaterReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    @Column(length = 2000)
    private String comment;
    private double rating;
    private Instant createdAt;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "theater_id")
    private Theater theater;


}
