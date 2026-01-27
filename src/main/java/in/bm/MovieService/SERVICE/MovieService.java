package in.bm.MovieService.SERVICE;

import in.bm.MovieService.ENTITY.*;
import in.bm.MovieService.EXCEPTION.*;
import in.bm.MovieService.REPO.MovieDetailsRepo;
import in.bm.MovieService.REPO.MovieRepo;
import in.bm.MovieService.REPO.MovieReviewRepo;
import in.bm.MovieService.RequestDTO.MovieRequestDTO;
import in.bm.MovieService.RequestDTO.MovieReviewRequestDTO;
import in.bm.MovieService.ResponseDTO.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {

    private final MovieRepo movieRepo;
    private final MovieDetailsRepo movieDetailsRepo;
    private final MovieReviewRepo movieReviewRepo;

    @Cacheable(
            cacheNames = "movies",
            key = "'PAGE:' + #page + ':' + #size"
    )
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public MoviePageResponseDTO getMovies(int page, int size) {

        log.info("Fetch movies request | page={} size={}", page, size);

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Movie> moviePage =
                movieRepo.findByStatus(MovieStatus.ACTIVE, pageRequest);

        log.info("Movies fetched | count={}", moviePage.getNumberOfElements());

        List<MovieResponseDTO> movies = moviePage.getContent()
                .stream()
                .map(movie -> MovieResponseDTO.builder()
                        .movieCode(movie.getMovieCode())
                        .movieName(movie.getMovieName())
                        .movieAvatar(movie.getMovieAvatar())
                        .certificate(movie.getCertificate())
                        .language(movie.getLanguage())
                        .build())
                .toList();

        return MoviePageResponseDTO.builder()
                .movies(movies)
                .page(moviePage.getNumber())
                .size(moviePage.getSize())
                .totalElements(moviePage.getTotalElements())
                .totalPages(moviePage.getTotalPages())
                .hasNext(moviePage.hasNext())
                .hasPrevious(moviePage.hasPrevious())
                .build();
    }


    @Cacheable(cacheNames = "movies",key = "#movieCode")
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public MovieDetailsResponseDTO getMovieDetails(String movieCode) {

        log.info("Fetch movie details request | movieCode={}", movieCode);

        Movie movie = movieRepo.findById(movieCode)
                .orElseThrow(() -> {
                    log.warn("Fetch rejected | movie not found movieCode={}", movieCode);
                    return new MovieNotFoundException("Movie not found by id: " + movieCode);
                });

        if (movie.getStatus() != MovieStatus.ACTIVE) {
            log.warn("Fetch rejected | movie inactive movieCode={} status={}",
                    movieCode, movie.getStatus());
            throw new MovieInactiveException("Movie is not active");
        }

        MovieDetails movieDetails = movie.getMovieDetails();

        log.info("Movie details fetched | movieCode={}", movieCode);

        Map<String, String> castAndCrew = movieDetails.getCastAndCrew();
        List<String> posters = movieDetails.getPosters();

        List<MovieReviewDto> reviews = movieDetails.getMovieReviews()
                .stream()
                .map(review -> MovieReviewDto.builder()
                        .id(review.getId())
                        .username(review.getUsername())
                        .rating(review.getRating())
                        .comment(review.getComment())
                        .createdAt(review.getCreatedAt())
                        .build())
                .toList();

        return MovieDetailsResponseDTO.builder()
                .id(movieDetails.getId())
                .castAndCrew(castAndCrew)
                .poster(posters)
                .reviews(reviews)
                .avgRating(movieDetails.getAvgRating())
                .totalReviews(movieDetails.getTotalReviews())
                .movieType(movieDetails.getMovieType())
                .synopsis(movieDetails.getSynopsis())
                .build();
    }


    @Transactional
    public MovieInfoDTO addMovie(MovieRequestDTO movieRequestDTO) {

        log.info("Add movie request | name={} language={}",
                movieRequestDTO.getMovieName(),
                movieRequestDTO.getLanguage());

        Movie movie = new Movie();
        movie.setMovieCode(generateCode());
        movie.setMovieName(movieRequestDTO.getMovieName());
        movie.setDuration(movieRequestDTO.getDuration());
        movie.setMovieAvatar(movieRequestDTO.getMovieAvatar());
        movie.setCertificate(movieRequestDTO.getCertificate());
        movie.setLanguage(movieRequestDTO.getLanguage());
        movie.setStatus(MovieStatus.PENDING);

        Movie savedMovie = movieRepo.save(movie);

        MovieDetails movieDetails = new MovieDetails();
        movieDetails.setMovie(savedMovie);
        movieDetails.setCastAndCrew(movieRequestDTO.getMovieDetailsRequestDTO().getCastAndCrew());
        movieDetails.setSynopsis(movieRequestDTO.getMovieDetailsRequestDTO().getSynopsis());
        movieDetails.setMovieType(movieRequestDTO.getMovieDetailsRequestDTO().getMovieType());
        movieDetails.setPosters(movieRequestDTO.getMovieDetailsRequestDTO().getPosters());

        MovieDetails savedDetails = movieDetailsRepo.save(movieDetails);
        savedMovie.setMovieDetails(savedDetails);

        log.info("Movie created | movieCode={} status={}",
                savedMovie.getMovieCode(), savedMovie.getStatus());

        return MovieInfoDTO.builder()
                .movieCode(savedMovie.getMovieCode())
                .movieName(savedMovie.getMovieName())
                .movieAvatar(savedMovie.getMovieAvatar())
                .language(savedMovie.getLanguage())
                .duration(savedMovie.getDuration())
                .certificate(savedMovie.getCertificate())
                .castAndCrew(savedDetails.getCastAndCrew())
                .posters(savedDetails.getPosters())
                .synopsis(savedDetails.getSynopsis())
                .movieType(savedDetails.getMovieType())
                .reviews(List.of())
                .status(savedMovie.getStatus())
                .build();
    }

    @Cacheable(cacheNames = "movies",key = "#movieCode")
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public MovieResponseDTO getMovieById(String movieCode) {

        log.info("Fetch movie request | movieCode={}", movieCode);

        Movie movie = movieRepo.findById(movieCode)
                .orElseThrow(() -> {
                    log.warn("Fetch rejected | movie not found movieCode={}", movieCode);
                    return new MovieNotFoundException("Movie not found with id: " + movieCode);
                });

        if (movie.getStatus() != MovieStatus.ACTIVE) {
            throw new MovieInactiveException("Movie is no longer active");
        }

        log.info("Movie fetched | movieCode={}", movieCode);

        return MovieResponseDTO.builder()
                .movieCode(movie.getMovieCode())
                .movieName(movie.getMovieName())
                .movieAvatar(movie.getMovieAvatar())
                .language(movie.getLanguage())
                .certificate(movie.getCertificate())
                .build();
    }


    @org.springframework.transaction.annotation.Transactional
    public MovieReviewDto addReview(String movieCode, MovieReviewRequestDTO dto, String userId) {

        log.info("Add review request | movieCode={} user={}",
                movieCode, dto.getUsername());

        Movie movie = movieRepo.findById(movieCode)
                .orElseThrow(() -> {
                    log.warn("Review rejected | movie not found movieCode={}", movieCode);
                    return new MovieNotFoundException("Movie not found");
                });

        if (movie.getStatus() != MovieStatus.ACTIVE) {
            log.warn("Review rejected | movie inactive movieCode={}", movieCode);
            throw new MovieInactiveException("Movie is not active");
        }

        MovieReview review = new MovieReview();
        review.setUserId(userId);
        review.setUsername(dto.getUsername());
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setCreatedAt(Instant.now());
        review.setMovieDetails(movie.getMovieDetails());

        MovieReview savedReview = movieReviewRepo.save(review);
        updateMovieRating(movie.getMovieDetails());

        log.info("Review added | reviewId={} movieCode={}",
                savedReview.getId(), movieCode);

        return MovieReviewDto.builder()
                .id(savedReview.getId())
                .username(savedReview.getUsername())
                .rating(savedReview.getRating())
                .comment(savedReview.getComment())
                .createdAt(savedReview.getCreatedAt())
                .build();
    }

    @CacheEvict(cacheNames = "movies",allEntries = true)
    @org.springframework.transaction.annotation.Transactional
    public MovieStatusDTO activate(String movieCode) {

        log.info("Activate movie request | movieCode={}", movieCode);

        Movie movie = movieRepo.findById(movieCode)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found"));

        if (movie.getStatus() == MovieStatus.ACTIVE) {
            log.warn("Activation rejected | already active movieCode={}", movieCode);
            throw new IllegalStateException("Movie is already active");
        }

        movie.setStatus(MovieStatus.ACTIVE);
        movieRepo.save(movie);

        log.info("Movie activated | movieCode={}", movieCode);

        return MovieStatusDTO.builder()
                .movieCode(movieCode)
                .message("Movie activated successfully")
                .build();
    }

    @Caching(
            put = @CachePut(cacheNames = "movies", key = "#result.movieCode()"),
            evict = @CacheEvict(cacheNames = "movies", allEntries = true)
    )
    @org.springframework.transaction.annotation.Transactional
    public MovieInfoDTO updateMovie(String movieCode, MovieRequestDTO movieRequestDTO) {

        log.info("Update movie request | movieCode={}", movieCode);

        Movie movie = movieRepo.findById(movieCode)
                .orElseThrow(() -> {
                    log.warn("Update rejected | movie not found movieCode={}", movieCode);
                    return new MovieNotFoundException("Movie not found with id: " + movieCode);
                });

        if (movie.getStatus() != MovieStatus.ACTIVE) {
            log.warn("Update rejected | movie inactive movieCode={} status={}",
                    movieCode, movie.getStatus());
            throw new MovieInactiveException("Movie is not active");
        }

        movie.setMovieName(movieRequestDTO.getMovieName());
        movie.setMovieAvatar(movieRequestDTO.getMovieAvatar());
        movie.setCertificate(movieRequestDTO.getCertificate());
        movie.setLanguage(movieRequestDTO.getLanguage());
        movie.setDuration(movieRequestDTO.getDuration());

        MovieDetails details = movie.getMovieDetails();
        if (details == null) {
            details = new MovieDetails();
            details.setMovie(movie);
        }

        details.setCastAndCrew(movieRequestDTO.getMovieDetailsRequestDTO().getCastAndCrew());
        details.setSynopsis(movieRequestDTO.getMovieDetailsRequestDTO().getSynopsis());
        details.setMovieType(movieRequestDTO.getMovieDetailsRequestDTO().getMovieType());
        details.setPosters(movieRequestDTO.getMovieDetailsRequestDTO().getPosters());

        movieDetailsRepo.save(details);

        log.info("Movie updated successfully | movieCode={}", movieCode);

        return MovieInfoDTO.builder()
                .movieCode(movie.getMovieCode())
                .movieName(movie.getMovieName())
                .movieAvatar(movie.getMovieAvatar())
                .language(movie.getLanguage())
                .duration(movie.getDuration())
                .certificate(movie.getCertificate())
                .castAndCrew(details.getCastAndCrew())
                .posters(details.getPosters())
                .synopsis(details.getSynopsis())
                .movieType(details.getMovieType())
                .reviews(List.of())
                .status(movie.getStatus())
                .build();
    }

    @CacheEvict(cacheNames = "movies",allEntries = true)
    @org.springframework.transaction.annotation.Transactional
    public MovieStatusDTO deactivate(String movieCode) {

        log.info("Deactivate movie request | movieCode={}", movieCode);

        Movie movie = movieRepo.findById(movieCode)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found"));

        if (movie.getStatus() == MovieStatus.INACTIVE) {
            log.warn("Deactivation rejected | already inactive movieCode={}", movieCode);
            throw new IllegalStateException("Movie is already inactive");
        }

        movie.setStatus(MovieStatus.INACTIVE);
        movieRepo.save(movie);

        log.info("Movie deactivated | movieCode={}", movieCode);

        return MovieStatusDTO.builder()
                .movieCode(movieCode)
                .message("Movie is no longer active")
                .build();
    }



    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public MoviePageResponseDTO searchMovie(String q, int page, int size) {

        log.info("Search movie request | query='{}' page={} size={}", q, page, size);

        Page<Movie> moviePage =
                movieRepo.searchAcrossFields(MovieStatus.ACTIVE, q, PageRequest.of(page, size));

        log.info("Search result | count={}", moviePage.getNumberOfElements());

        List<MovieResponseDTO> movies = moviePage.getContent()
                .stream()
                .map(movie -> MovieResponseDTO.builder()
                        .movieCode(movie.getMovieCode())
                        .movieName(movie.getMovieName())
                        .movieAvatar(movie.getMovieAvatar())
                        .certificate(movie.getCertificate())
                        .language(movie.getLanguage())
                        .build())
                .collect(Collectors.toList());

        return MoviePageResponseDTO.builder()
                .movies(movies)
                .page(moviePage.getNumber())
                .size(moviePage.getSize())
                .totalElements(moviePage.getTotalElements())
                .totalPages(moviePage.getTotalPages())
                .hasNext(moviePage.hasNext())
                .hasPrevious(moviePage.hasPrevious())
                .build();
    }

    private String generateCode() {
        return "MV" + UUID.randomUUID().toString().substring(0, 8);
    }

    @org.springframework.transaction.annotation.Transactional
    private void updateMovieRating(MovieDetails details) {

        double avgRating = details.getMovieReviews()
                .stream()
                .mapToDouble(MovieReview::getRating)
                .average()
                .orElse(0.0);


        details.setAvgRating(avgRating);
        details.setTotalReviews(details.getMovieReviews().size());
        movieDetailsRepo.save(details);

        log.info("Movie rating updated | avgRating={} totalReviews={}",
                avgRating, details.getTotalReviews());
    }

    @org.springframework.transaction.annotation.Transactional
    public void deleteReviewUser(String userId, Long reviewId) {
        MovieReview review = movieReviewRepo.findById(reviewId).orElseThrow(()->new ReviewNotFoundException("Review not found"));
        if(!review.getUserId().equals(userId)){
            throw new UnauthorizedActionException("You cannot modify this review");
        }
        movieReviewRepo.delete(review);
    }

    public MovieReviewDto editReview(long reviewId, @Valid MovieReviewRequestDTO dto, String userId) {
        log.info("Edit review request | reviewId={}", reviewId);

        MovieReview review = movieReviewRepo.findById(reviewId)
                .orElseThrow(() -> {
                    log.warn("Edit rejected | review not found reviewId={}", reviewId);
                    return new ReviewNotFoundException("Review not found");
                });

        if (review.getMovieDetails().getMovie().getStatus() != MovieStatus.ACTIVE) {
            log.warn("Edit rejected | Movie inactive reviewId={}", reviewId);
            throw new MovieInactiveException("Movie is not active");
        }

        if (!review.getUserId().equals(userId)){
            throw new UnauthorizedActionException("You cannot modify this review");
        }

        review.setUsername(dto.getUsername());
        review.setComment(dto.getComment());
        review.setRating(dto.getRating());

        MovieReview savedReview = movieReviewRepo.save(review);

        log.info("Review updated | reviewId={}", reviewId);

        return MovieReviewDto.builder()
                .id(savedReview.getId())
                .username(savedReview.getUsername())
                .rating(savedReview.getRating())
                .comment(savedReview.getComment())
                .createdAt(savedReview.getCreatedAt())
                .build();

    }
}
