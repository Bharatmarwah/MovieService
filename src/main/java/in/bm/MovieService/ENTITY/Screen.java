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
@Table(name = "screens")
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long screenId;
    private String screenName;

    @ManyToOne
    @JoinColumn(name = "theater_code",nullable = false)
    private Theater theater;

    @OneToMany(mappedBy = "screen",cascade = CascadeType.PERSIST)// persist meaning only insertion is allowed
    private List<Seat> seats;

    @OneToMany(mappedBy = "screen",cascade = CascadeType.PERSIST)
    private List<Show> shows;

    @OneToMany(mappedBy = "screen", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<SeatCategory> seatCategories;

}
