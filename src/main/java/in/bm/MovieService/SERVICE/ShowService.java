package in.bm.MovieService.SERVICE;

import in.bm.MovieService.ENTITY.*;
import in.bm.MovieService.EXCEPTION.*;
import in.bm.MovieService.REPO.MovieRepo;
import in.bm.MovieService.REPO.ScreenRepo;
import in.bm.MovieService.REPO.ShowRepo;
import in.bm.MovieService.RequestDTO.ShowRequestDTO;
import in.bm.MovieService.ResponseDTO.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
@Slf4j
public class ShowService {

    private final ShowRepo showRepo;
    private final ScreenRepo screenRepo;
    private final MovieRepo movieRepo;

    @Transactional
    public ShowResponseDTO addShow(ShowRequestDTO dto) {

        log.info(
                "Add show request received | movieCode={} screenId={} showDate={} showTime={}",
                dto.getMovieCode(),
                dto.getScreenId(),
                dto.getShowDate(),
                dto.getShowTime()
        );

        Movie movie = movieRepo
                .findById(dto.getMovieCode())
                .orElseThrow(() -> {
                    log.warn("Movie not found | movieCode={}", dto.getMovieCode());
                    return new MovieNotFoundException("Movie not found");
                });

        if (movie.getStatus() != MovieStatus.ACTIVE) {
            log.warn(
                    "Inactive movie attempted for show creation | movieCode={} status={}",
                    movie.getMovieCode(),
                    movie.getStatus()
            );
            throw new MovieInactiveException("Movie is no longer active");
        }

        Screen screen = screenRepo
                .findById(dto.getScreenId())
                .orElseThrow(() -> {
                    log.warn("Screen not found | screenId={}", dto.getScreenId());
                    return new ScreenNotFoundException("Screen not found");
                });

        if (screen.getTheater().getStatus() != TheaterStatus.ACTIVE) {
            log.warn(
                    "Inactive theater attempted for show creation | screenId={} theaterId={} status={}",
                    screen.getScreenId(),
                    screen.getTheater().getTheatreCode(),
                    screen.getTheater().getStatus()
            );
            throw new TheaterInactiveException(
                    "Theater is no longer active with screenId: " + dto.getScreenId()
            );
        }

        Show show = new Show();
        show.setShowDate(dto.getShowDate());
        show.setShowTime(dto.getShowTime());
        show.setMovie(movie);
        show.setScreen(screen);

        Show savedShow = showRepo.save(show);

        log.info(
                "Show created successfully | showId={} movieCode={} screenId={} showDate={} showTime={}",
                savedShow.getShowId(),
                movie.getMovieCode(),
                screen.getScreenId(),
                savedShow.getShowDate(),
                savedShow.getShowTime()
        );

        return ShowResponseDTO.builder()
                .showId(savedShow.getShowId())
                .showTime(savedShow.getShowTime())
                .showDate(savedShow.getShowDate())
                .screenId(savedShow.getScreen().getScreenId())
                .movieCode(savedShow.getMovie().getMovieCode())
                .dayOfWeek(savedShow.getDayOfWeek())
                .meridiem(savedShow.getMeridiem())
                .build();
    }

    @Transactional
    public ShowResponseDTO updateShow(@Valid ShowRequestDTO requestDTO, Long showId) {
        Show show = showRepo.findById(showId)
                .orElseThrow(() ->
                        new ShowNotFoundException("Show not found"));

        Movie movie = movieRepo.
                findById(requestDTO.getMovieCode()).
                orElseThrow(() ->
                        new MovieNotFoundException("Movie not found"));

        if (movie.getStatus() != MovieStatus.ACTIVE) {
            throw new MovieInactiveException("Movie is not active");
        }

        Screen screen = screenRepo.
                findById(requestDTO.getScreenId()).
                orElseThrow(() ->
                        new ScreenNotFoundException("Screen not found"));

        show.setMovie(movie);
        show.setScreen(screen);
        show.setShowTime(requestDTO.getShowTime());
        show.setShowDate(requestDTO.getShowDate());

        Show updateShow = showRepo.save(show);


        return ShowResponseDTO.builder().
                movieCode(updateShow.getMovie().getMovieCode()).
                meridiem(updateShow.getMeridiem()).
                dayOfWeek(updateShow.getDayOfWeek()).
                showDate(updateShow.getShowDate()).
                screenId(updateShow.getScreen().getScreenId()).
                showTime(updateShow.getShowTime()).
                showId(updateShow.getShowId()).
                build();
    }

    @Transactional
    public void deleteShow(Long showId) {
        Show show = showRepo.
                findById(showId).
                orElseThrow(() ->
                        new ShowNotFoundException("Show not found"));

        showRepo.delete(show);
    }

    public ShowPageResponseDTO getAllShow(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Show> showPage = showRepo.findALlShowWithActiveMovies(pageRequest, MovieStatus.ACTIVE);

        List<ShowResponseDTO> shows = showPage.getContent().stream().map(show -> ShowResponseDTO.builder().
                        showId(show.getShowId()).
                        screenId(show.getScreen().getScreenId()).
                        showTime(show.getShowTime()).
                        showDate(show.getShowDate()).
                        dayOfWeek(show.getDayOfWeek()).
                        meridiem(show.getMeridiem()).
                        movieCode(show.getMovie().getMovieCode())
                        .build())
                .toList();

        return ShowPageResponseDTO.
                builder().
                showResponses(shows).
                hasNext(showPage.hasNext()).
                hasPrevious(showPage.hasPrevious()).
                page(showPage.getTotalPages()).
                size(showPage.getSize()).
                totalElements(showPage.getTotalElements()).
                totalPages(showPage.getTotalPages())
                .build();
    }

    public ShowResponseDTO getShowById(Long showId) {
        Show show = showRepo.
                findById(showId).
                orElseThrow(() ->
                        new ShowNotFoundException("Show not found"));
        if (show.getMovie().getStatus() != MovieStatus.ACTIVE) {
            throw new MovieInactiveException("Movie is no longer active");
        }
        return ShowResponseDTO.builder()
                .showId(show.getShowId()).
                screenId(show.getScreen().getScreenId()).
                showTime(show.getShowTime()).
                showDate(show.getShowDate()).
                dayOfWeek(show.getDayOfWeek()).
                meridiem(show.getMeridiem()).
                movieCode(show.getMovie().getMovieCode()).
                build();
    }

    public List<ShowDateTimeResponseDTO> getShowsByMovieCode(String movieCode) {
        List<Show> shows = showRepo.findShowsByMovieCode(movieCode, MovieStatus.ACTIVE);
        return shows.
                stream().
                map(show ->
                        ShowDateTimeResponseDTO
                                .builder().
                                showId(show.getShowId()).
                                showTime(show.getShowTime()).
                                showDate(show.getShowDate()).
                                meridiem(show.getMeridiem()).
                                dayOfWeek(show.getDayOfWeek()).
                                build()).toList();
    }

    public List<ShowTimeResponseDTO> getShowsForTheater(String movieCode, String theatreCode, LocalDate date) {
        List<Show> shows = showRepo.findShowsTime(movieCode,theatreCode,date);
        return shows
                .stream()
                .map(show->ShowTimeResponseDTO
                        .builder()
                        .showId(show.getShowId())
                        .startTime(show.getShowTime())
                        .build())
                .toList();
    }

    @Transactional(readOnly = true)
    public ShowSeatsResponse seatsByShowId(Long showId) {

        Show show = showRepo.findById(showId)
                .orElseThrow(() ->
                        new ShowNotFoundException("Show not found"));

        Screen screen = show.getScreen();
        if (screen == null) {
            throw new ScreenNotFoundException("Screen not found");
        }

        if (screen.getScreenLifeCycle() != ScreenLifeCycle.ACTIVE) {
            throw new ScreenInActiveException("Screen is inactive");
        }

        List<Seat> seatList = screen.getSeats();
        if (seatList == null || seatList.isEmpty()) {
            throw new SeatNotFoundException("No seats configured for screen");
        }

        List<SeatsResponseDTO> seats =
                seatList.stream()
                        .filter(seat -> seat.getLifeCycle() == SeatLifecycle.ACTIVE)
                        .map(seat -> SeatsResponseDTO.builder()
                                .seatId(seat.getSeatId())
                                .seatNumber(seat.getSeatNumber())
                                .seatType(seat.getSeatCategory().getSeatType())
                                .view(seat.getViewType())
                                .price(seat.getSeatCategory().getPrice())
                                .feature(seat.getSeatFeatures())
                                .status(seat.getStatus())
                                .build()
                        )
                        .toList();

        return ShowSeatsResponse.builder()
                .showId(show.getShowId())
                .screenName(screen.getScreenName())
                .movieCode(show.getMovie().getMovieCode())
                .theaterCode(screen.getTheater().getTheatreCode())
                .startTime(show.getShowTime())
                .seats(seats)
                .build();
    }

}
