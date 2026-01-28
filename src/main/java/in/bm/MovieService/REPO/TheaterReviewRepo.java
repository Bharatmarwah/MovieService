package in.bm.MovieService.REPO;


import in.bm.MovieService.ENTITY.TheaterReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TheaterReviewRepo extends JpaRepository<TheaterReview,Long> {

    @Transactional(readOnly = true)
    @Query("SELECT tr from TheaterReview tr WHERE tr.theater.theatreCode= :theaterCode")
    List<TheaterReview> findAllReviewsPerTheater(@Param("theaterCode") String theaterCode);
}
