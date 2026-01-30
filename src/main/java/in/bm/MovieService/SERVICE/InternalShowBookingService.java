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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InternalShowBookingService {

    private final MovieRepo movieRepo;
    private final ShowRepo showRepo;
    private final ShowSeatRepo showSeatRepo;

    @Transactional
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
                showSeatRepo.findByIdsForUpdate(request.getShowSeatIds());

        if (showSeats.size() != request.getShowSeatIds().size()) {
            throw new SeatNotFoundException("Invalid seat ids");
        }

        double baseAmount = 0;

        for (ShowSeat seat : showSeats) {

            if (!seat.getShow().getShowId().equals(show.getShowId())) {
                throw new InvalidSeatException("Seat does not belong to show");
            }

            if (seat.getSeatStatus() != SeatStatus.AVAILABLE) {
                throw new SeatUnavailableException(
                        "Seat already booked or blocked: " + seat.getShowSeatId());
            }

            if (seat.getSeat().getLifeCycle() != SeatLifecycle.ACTIVE) {
                throw new SeatUnavailableException("Seat inactive");
            }

            baseAmount += seat.getSeat()
                    .getSeatCategory()
                    .getPrice();
        }

        final double CONVENIENCE_RATE = 0.08;
        final double MAX_CONVENIENCE_FEE = 50;
        final double GST_RATE = 0.18;

        double convenienceFee =
                Math.min(baseAmount * CONVENIENCE_RATE, MAX_CONVENIENCE_FEE);

        double gstAmount =
                (baseAmount + convenienceFee) * GST_RATE;

        double totalAmount =
                baseAmount + convenienceFee + gstAmount;

        return InternalShowResponse.builder()
                .movieCode(movie.getMovieCode())
                .showId(show.getShowId())
                .showSeatsIds(
                        showSeats.stream()
                                .map(ShowSeat::getShowSeatId)
                                .toList()
                )
                .baseAmount(baseAmount)
                .convenienceFee(convenienceFee)
                .gstAmount(gstAmount)
                .totalAmount(totalAmount)
                .build();
    }


}
