package in.bm.MovieService.REPO;

import in.bm.MovieService.ENTITY.ScreenLifeCycle;
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
            SELECT DISTINCT t
            FROM Theater t
            JOIN t.screens s
            JOIN s.shows sh
            JOIN sh.movie m
            JOIN s.seatCategories sc
            WHERE t.city = :city
            AND (m.movieCode = :movieCode)
            AND (:time IS NULL OR sh.showTime >= :time)
            AND (:seatPrice IS NULL OR sc.price <= :seatPrice)
            AND (:status IS NULL OR t.status = :status)
            AND (:lifeCycle IS NULL OR s.screenLifeCycle =:lifeCycle)
            """)

    Page<Theater> filter(
            @Param("city") String city,
            @Param("movieCode") String movieCode,
            @Param("time") LocalTime time,
            @Param("seatPrice") Double seatPrice,
            @Param("status") TheaterStatus status,
            @Param("lifeCycle") ScreenLifeCycle lifeCycle,
            Pageable pageable
    );


}
