package in.bm.MovieService.REPO;

import in.bm.MovieService.ENTITY.Theater;
import in.bm.MovieService.ENTITY.TheaterStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepo extends JpaRepository<Theater, String> {
    Page<Theater> findAllByStatus(TheaterStatus status, PageRequest pageRequest);

}
