package in.bm.MovieService.REPO;

import in.bm.MovieService.ENTITY.Screen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreenRepo extends JpaRepository<Screen, Long> {
}
