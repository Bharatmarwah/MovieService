package in.bm.MovieService.SERVICE;

import in.bm.MovieService.ENTITY.*;
import in.bm.MovieService.EXCEPTION.*;
import in.bm.MovieService.REPO.MovieRepo;
import in.bm.MovieService.REPO.ScreenRepo;
import in.bm.MovieService.REPO.ShowRepo;
import in.bm.MovieService.RequestDTO.ShowRequestDTO;
import in.bm.MovieService.ResponseDTO.BookingResponseDTO;
import in.bm.MovieService.ResponseDTO.ShowResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
@Slf4j
public class ShowService {

    private final ShowRepo showRepo;
    private final ScreenRepo screenRepo;
    private final MovieRepo movieRepo;

    public ShowResponseDTO addShow( ShowRequestDTO dto) {

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

    public BookingResponseDTO previewBooking(in.bm.MovieService.RequestDTO.BookingRequestDTO requestDTO) {
        Show show = showRepo.findById(requestDTO.getShowId())
                .orElseThrow(()
                        -> new ShowNotFoundException("show is not found"));

        Set<String> requestSeats = new HashSet<>(requestDTO.getSeatNumbers());

        List<Seat> matchedSeats = show.getScreen()
                .getSeats()
                .stream()
                .filter(seat->requestSeats.contains(seat.getSeatNumber()))
                .toList();

        if (matchedSeats.size()!=requestSeats.size()){
            throw new InvalidSeatException("Invalid seat number(s)");
        }

        if (matchedSeats.stream().anyMatch(
                seat -> seat.getLifeCycle() != SeatLifecycle.ACTIVE)) {
            throw new SeatUnavailableException("One or more seats are unavailable");
        }

        double baseAmount = matchedSeats.stream()
                .mapToDouble(seat -> seat.getSeatCategory().getPrice())
                .sum();

       List<String> seatNumbers = matchedSeats.stream().map(Seat::getSeatNumber).toList();

        return BookingResponseDTO
                .builder()
                .showId(show.getShowId())
                .movieCode(show.getMovie().getMovieCode())
                .theaterCode(show.getScreen().getTheater().getTheatreCode())
                .seatNumbers(seatNumbers).baseAmount(baseAmount).build();

    }
}
