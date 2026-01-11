package in.bm.MovieService.REPO;


import in.bm.MovieService.ENTITY.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepo extends JpaRepository<Seat,Long> {

}
