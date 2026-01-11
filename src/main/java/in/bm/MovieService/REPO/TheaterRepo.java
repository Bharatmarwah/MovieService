package in.bm.MovieService.REPO;

import in.bm.MovieService.ENTITY.Theater;
import in.bm.MovieService.ENTITY.TheaterStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;

public interface TheaterRepo extends JpaRepository<Theater, String> {
    Page<Theater> findAllByStatus(TheaterStatus status, PageRequest pageRequest);


    @Query("""
                SELECT t FROM Theater t WHERE t.city = :city
            """)
    Page<Theater> filter(
            @Param("city") String city,
            @Param("movieCode") String movieCode,
            @Param("time") LocalTime time,
            @Param("seatPrice") Double seatPrice,
            @Param("status") TheaterStatus status,
            Pageable pageable
    );


}
