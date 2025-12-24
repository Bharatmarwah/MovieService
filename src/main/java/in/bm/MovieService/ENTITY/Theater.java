package in.bm.MovieService.ENTITY;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "theaters",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"brand", "branch_name", "area", "city"}
                )
        }
)
public class Theater {

    @Id
    private String theatreCode;

    @Column(nullable = false,length = 100)
    private String brand;

    @Column(name = "branch_name", nullable = false,length = 100)
    private String branchName;

    @Column(nullable = false,length = 100)
    private String area;

    @Column(nullable = false,length = 100)
    private String city;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    private boolean allowsCancellation = true;

    @Enumerated(EnumType.STRING)
    private TheaterStatus status = TheaterStatus.PENDING;

    private double avgRating;
    private int totalRatings;

    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL)
    private List<TheaterReview> theaterReviews;

    @OneToOne(mappedBy = "theater", cascade = CascadeType.ALL)
    private TheaterDetails theaterDetails;

    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Screen> screens;
}
