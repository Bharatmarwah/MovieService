package in.bm.MovieService.ENTITY;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "theater_details")
public class TheaterDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String roadOrMall;
    private String cityPinCode;
    private String country;

    @ElementCollection
    @CollectionTable(name = "services_and_amenities",joinColumns = @JoinColumn(name = "theater_id"))
    @Column(name = "details")
    List<String> servicesAndAmenities;

    @OneToOne
    @JoinColumn(name = "theater_id",nullable = false)
    private Theater theater;



}
