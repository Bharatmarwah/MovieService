package in.bm.MovieService.REPO;

import in.bm.MovieService.ENTITY.MovieStatus;
import in.bm.MovieService.ENTITY.Show;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface ShowRepo extends JpaRepository<Show, Long> {


    @Transactional(readOnly = true)
    @Query("SELECT sh FROM Show sh WHERE sh.movie.status =:status")
    Page<Show> findALlShowWithActiveMovies(PageRequest pageRequest, @Param("status") MovieStatus movieStatus);

    @Transactional(readOnly = true)
    @Query("""
                SELECT sh
                FROM Show sh
                JOIN sh.movie m
                WHERE m.movieCode = :movieCode
                  AND m.status = :movieStatus
            """)
    List<Show> findShowsByMovieCode(
            @Param("movieCode") String movieCode,
            @Param("movieStatus") MovieStatus movieStatus
    );

    @Transactional(readOnly = true)
    @Query("SELECT sh FROM Show sh WHERE sh.movie.movieCode=:movieCode AND sh.screen.theater.theatreCode=:theaterCode AND sh.showDate=:date")
    List<Show> findShowsTime(@Param("movieCode")
                             String movieCode,
                             @Param("theaterCode") String theatreCode,
                             @Param("date") LocalDate date);
}
