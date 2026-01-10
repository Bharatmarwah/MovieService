package in.bm.MovieService.REPO;

import in.bm.MovieService.ENTITY.Screen;
import in.bm.MovieService.ENTITY.SeatCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SeatCategoryRepo extends JpaRepository<SeatCategory,Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM SeatCategory sc where sc.screen =:screen")
    void deleteByScreen(@Param("screen") Screen screen);
}
