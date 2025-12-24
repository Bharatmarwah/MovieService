    package in.bm.MovieService.ENTITY;

    import com.fasterxml.jackson.annotation.JsonIgnore;
    import jakarta.persistence.*;
    import lombok.*;

    import java.util.List;
    import java.util.Map;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    @Table(name="movies_details")
    public class MovieDetails {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;
        @OneToMany(mappedBy ="movieDetails",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
        private List<MovieReview> movieReviews;
        @Lob
        private String synopsis;

        @ElementCollection
        @CollectionTable(name = "Movie_types",joinColumns =@JoinColumn(name = "movies_details_id",referencedColumnName = "id"))
        @Column(name = "type",nullable = false)
        private List<String> movieType;

        @ElementCollection
        @CollectionTable(name = "cast_and_crew",joinColumns = @JoinColumn(name = "movies_details_id",referencedColumnName = "id"))
        @MapKeyColumn(name = "name")
        @Column(name = "image_url")
        private Map<String,String> castAndCrew;

        @ElementCollection
        @CollectionTable(name = "posters",joinColumns = @JoinColumn(name = "movies_details_id",referencedColumnName = "id"))
        @Column(name = "poster_image_url")
        private List<String> posters;

        private double avgRating;
        private int totalReviews;

        @JsonIgnore
        @OneToOne
        @JoinColumn(name="movie_code",referencedColumnName = "movieCode",unique = true)
        private Movie movie;

    }
