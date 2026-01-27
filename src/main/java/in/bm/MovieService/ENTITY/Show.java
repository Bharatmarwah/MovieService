package in.bm.MovieService.ENTITY;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Entity
@Table(
        name = "shows",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"screen_id", "show_date", "show_time"}
        )
)
@Getter
@Setter
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long showId;

    @Column(nullable = false)
    private LocalDate showDate;

    @Column(nullable = false)
    private LocalTime showTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @ManyToOne
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;

    @ManyToOne
    @JoinColumn(
            name = "movie_code",
            referencedColumnName = "movieCode",
            nullable = false
    )
    private Movie movie;

    private String meridiem;

    @OneToMany(mappedBy = "show", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<ShowSeat> showSeats;

    @PrePersist// runs before inserting
    @PreUpdate // runs before every update
    private void syncDayOfWeek() {
        this.dayOfWeek = this.showDate.getDayOfWeek();
        this.meridiem = this.showTime
                .format(DateTimeFormatter.ofPattern("a")).toUpperCase(Locale.ROOT);
    }


}
