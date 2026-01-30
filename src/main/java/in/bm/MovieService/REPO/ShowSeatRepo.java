package in.bm.MovieService.REPO;

import in.bm.MovieService.ENTITY.ShowSeat;
import jakarta.persistence.LockModeType;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShowSeatRepo extends JpaRepository<ShowSeat,Long> {

    List<ShowSeat> findByShow_ShowId(Long showId);

    boolean existsBySeat_SeatId(Long seatId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ss FROM ShowSeat ss WHERE ss.showSeatId IN :ids")
    List<ShowSeat> findByIdsForUpdate(@Param ("ids")@NotNull List<Long> ids);
}
