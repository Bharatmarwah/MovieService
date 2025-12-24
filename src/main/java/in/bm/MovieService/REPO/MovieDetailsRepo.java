package in.bm.MovieService.REPO;

import in.bm.MovieService.ENTITY.MovieDetails;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MovieDetailsRepo extends JpaRepository<MovieDetails,Long> {
    List<MovieDetails> findByMovieType(String movieType);
}
