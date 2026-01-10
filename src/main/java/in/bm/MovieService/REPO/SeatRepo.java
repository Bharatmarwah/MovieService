package in.bm.MovieService.REPO;

import in.bm.MovieService.ENTITY.Screen;
import in.bm.MovieService.ENTITY.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SeatRepo extends JpaRepository<Seat,Long> {

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Seat s WHERE s.seatCategory.screen = :screen")
    int deleteByScreen(@Param("screen") Screen screen);
}
