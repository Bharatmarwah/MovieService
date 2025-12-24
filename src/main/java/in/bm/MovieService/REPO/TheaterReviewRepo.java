package in.bm.MovieService.REPO;


import in.bm.MovieService.ENTITY.TheaterReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterReviewRepo extends JpaRepository<TheaterReview,Long> {
}
