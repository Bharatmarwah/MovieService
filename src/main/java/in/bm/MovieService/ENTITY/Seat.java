package in.bm.MovieService.ENTITY;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "seats",uniqueConstraints = @UniqueConstraint(columnNames = {"screen_id","seat_number"}))
@Getter
@Setter
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;

    @Column(nullable = false)
    private String seatNumber;

    @Enumerated(EnumType.STRING)
    private SeatLifecycle lifeCycle = SeatLifecycle.ACTIVE;

    @Enumerated(EnumType.STRING)
    private ViewType viewType = ViewType.NORMAL;

    @Enumerated(EnumType.STRING)
    @Column(name = "feature")
    private SeatFeature seatFeatures;

    @ManyToOne
    @JoinColumn(name = "seat_category_id", nullable = false)
    private SeatCategory seatCategory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus status = SeatStatus.AVAILABLE;

    @ManyToOne
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;


}
