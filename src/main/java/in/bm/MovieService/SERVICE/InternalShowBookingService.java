package in.bm.MovieService.SERVICE;

import in.bm.MovieService.ENTITY.*;
import in.bm.MovieService.EXCEPTION.*;
import in.bm.MovieService.REPO.MovieRepo;
import in.bm.MovieService.REPO.ShowRepo;
import in.bm.MovieService.REPO.ShowSeatRepo;
import in.bm.MovieService.RequestDTO.InternalShowRequestDTO;
import in.bm.MovieService.ResponseDTO.InternalShowResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InternalShowBookingService {

    private final MovieRepo movieRepo;
    private final ShowRepo showRepo;
    private final ShowSeatRepo showSeatRepo;

    public InternalShowResponse validateShowForBooking(
            @Valid InternalShowRequestDTO request) {


        Show show = showRepo.findById(request.getShowId())
                .orElseThrow(() -> new ShowNotFoundException("Show not found"));


        Movie movie = movieRepo.findById(show.getMovie().getMovieCode())
                .orElseThrow(() -> new MovieNotFoundException("Movie not found"));

        if (movie.getStatus() != MovieStatus.ACTIVE) {
            throw new MovieInactiveException("Movie is not active");
        }

        List<ShowSeat> showSeats =
                showSeatRepo.findAllById(request.getShowSeatIds());

        if (showSeats.size() != request.getShowSeatIds().size()) {
            throw new SeatNotFoundException("Invalid seat id(s)");
        }

        double totalAmount = 0;

        for (ShowSeat seat : showSeats) {
            if (!seat.getShow().getShowId().equals(show.getShowId())) {
                throw new InvalidSeatException("Seat does not belong to show");
            }

            if (seat.getSeat().getLifeCycle() != SeatLifecycle.ACTIVE) {
                throw new SeatUnavailableException("Seat inactive");
            }

            totalAmount += seat.getSeat()
                    .getSeatCategory()
                    .getPrice();
        }

        // 5️⃣ Build response
        return InternalShowResponse.builder()
                .movieCode(movie.getMovieCode())
                .showId(show.getShowId())
                .showSeatsIds(
                        showSeats.stream()
                                .map(ShowSeat::getShowSeatId)
                                .toList()
                )
                .totalAmount(totalAmount)
                .build();
    }
    // implements taxes in total amount
    // checking showSeats availability
    // seat active

}
