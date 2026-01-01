    package in.bm.MovieService.SERVICE;

    import in.bm.MovieService.ENTITY.*;
    import in.bm.MovieService.EXCEPTION.ScreenNotFoundException;
    import in.bm.MovieService.EXCEPTION.SeatCategoryNotFoundException;
    import in.bm.MovieService.REPO.ScreenRepo;
    import in.bm.MovieService.REPO.SeatCategoryRepo;
    import in.bm.MovieService.REPO.SeatRepo;
    import in.bm.MovieService.RequestDTO.SeatRequestDTO;
    import in.bm.MovieService.ResponseDTO.SeatBulkResponseDTO;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.stereotype.Service;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Locale;

    @Service
    @RequiredArgsConstructor
    @Slf4j
    public class SeatService {

        private final SeatRepo seatRepo;
        private final ScreenRepo screenRepo;
        private final SeatCategoryRepo seatCategoryRepo;

        public SeatBulkResponseDTO addSeats(SeatRequestDTO dto) {

            log.info(
                    "Add seats request | screenId={} seatCategoryId={} rows {}-{} seatsPerRow={}",
                    dto.getScreenId(),
                    dto.getSeatCategoryId(),
                    dto.getRowStart(),
                    dto.getRowEnd(),
                    dto.getSeatsPerRow()
            );

            Screen screen = screenRepo.findById(dto.getScreenId())
                    .orElseThrow(() ->
                            new ScreenNotFoundException("Screen not found"));

            SeatCategory seatCategory = seatCategoryRepo.findById(dto.getSeatCategoryId())
                    .orElseThrow(() ->
                            new SeatCategoryNotFoundException("Seat category not found"));

            List<Seat> seats = new ArrayList<>();
            List<String> seatNumbers = new ArrayList<>();

            if (dto.getRowStart().toUpperCase(Locale.ROOT).charAt(0) > dto.getRowEnd().toUpperCase(Locale.ROOT).charAt(0)){
                throw new IllegalStateException("rowStart must be <= rowEnd");
            }

            char startRow = dto.getRowStart().toUpperCase().charAt(0);
            char endRow = dto.getRowEnd().toUpperCase().charAt(0);

            // ASCII numbers

            for (char row = startRow; row <= endRow; row++) {
                for (int i = 1; i <= dto.getSeatsPerRow(); i++) {

                    String seatNumber = row + String.valueOf(i);

                    Seat seat = new Seat();
                    seat.setSeatNumber(seatNumber);
                    seat.setScreen(screen);
                    seat.setSeatCategory(seatCategory);
                    seat.setLifeCycle(SeatLifecycle.ACTIVE);
                    seat.setSeatFeatures(SeatFeature.NONE);
                    seat.setViewType(ViewType.NORMAL);

                    seats.add(seat);
                    seatNumbers.add(seatNumber);
                }
            }

            seatRepo.saveAll(seats);

            log.info(
                    "Seats created successfully | screenId={} totalSeats={}",
                    screen.getScreenId(),
                    seats.size()
            );

            return SeatBulkResponseDTO.builder()
                    .screenId(screen.getScreenId())
                    .seatCategoryId(seatCategory.getId())
                    .totalSeatsCreated(seats.size())
                    .seatNumbers(seatNumbers)
                    .build();
        }
    }
