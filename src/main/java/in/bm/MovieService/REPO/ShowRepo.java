package in.bm.MovieService.REPO;

import in.bm.MovieService.ENTITY.Show;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowRepo extends JpaRepository<Show,Long> {
}
