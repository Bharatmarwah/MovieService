package in.bm.MovieService.ENTITY;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "seat_categories")
@Getter @Setter
@NoArgsConstructor@AllArgsConstructor
public class SeatCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SeatType seatType;

    private double price;

    @ManyToOne
    @JoinColumn(name = "screen_id",nullable = false)
    private Screen screen;
}