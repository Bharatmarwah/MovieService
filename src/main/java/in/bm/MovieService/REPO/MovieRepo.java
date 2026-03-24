package in.bm.MovieService.REPO;

import in.bm.MovieService.ENTITY.Movie;
import in.bm.MovieService.ENTITY.MovieStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MovieRepo extends JpaRepository<Movie, String> {

    @Query("""
    SELECT DISTINCT m
    FROM Movie m
    JOIN m.movieDetails md
    LEFT JOIN md.movieType mt
    WHERE m.status = :status
      AND (
            LOWER(m.movieName) LIKE LOWER(CONCAT(:q, '%'))
         OR LOWER(mt) LIKE LOWER(CONCAT(:q, '%'))
         OR LOWER(m.language) LIKE LOWER(CONCAT(:q, '%'))
      )
""")
    Page<Movie> searchAcrossFields(
            @Param("status") MovieStatus status,
            @Param("q") String q,
            Pageable pageable
    );

    Page<Movie> findByStatus(MovieStatus movieStatus, Pageable pageable);

    @Query(value = "SELECT * FROM movies WHERE movie_name LIKE %:name% LIMIT 3", nativeQuery = true)
    List<Movie> findMovieForAgentByName(String name);
}

