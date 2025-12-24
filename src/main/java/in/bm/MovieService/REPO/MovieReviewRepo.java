package in.bm.MovieService.REPO;

import in.bm.MovieService.ENTITY.MovieReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieReviewRepo extends JpaRepository<MovieReview,Long> {
}
