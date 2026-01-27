package in.bm.MovieService.REPO;

import in.bm.MovieService.ENTITY.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShowSeatRepo extends JpaRepository<ShowSeat,Long> {

    List<ShowSeat> findByShow_ShowId(Long showId);

    boolean existsBySeat_SeatId(Long seatId);
}
