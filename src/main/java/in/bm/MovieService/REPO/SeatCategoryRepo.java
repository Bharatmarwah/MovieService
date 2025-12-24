package in.bm.MovieService.REPO;

import in.bm.MovieService.ENTITY.SeatCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatCategoryRepo extends JpaRepository<SeatCategory,Long> {
}
