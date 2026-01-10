package in.bm.MovieService.REPO;

import in.bm.MovieService.ENTITY.MovieStatus;
import in.bm.MovieService.ENTITY.Show;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ShowRepo extends JpaRepository<Show,Long> {


    @Transactional(readOnly = true)
    @Query("SELECT sh FROM Show sh WHERE sh.movie.status =:status")
    Page<Show> findALlShowWithActiveMovies(PageRequest pageRequest, @Param("status") MovieStatus movieStatus);
}
