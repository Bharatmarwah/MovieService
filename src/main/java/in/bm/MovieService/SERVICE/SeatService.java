package in.bm.MovieService.SERVICE;

import in.bm.MovieService.ENTITY.*;
import in.bm.MovieService.EXCEPTION.*;
import in.bm.MovieService.REPO.ScreenRepo;
import in.bm.MovieService.REPO.SeatCategoryRepo;
import in.bm.MovieService.REPO.SeatRepo;
import in.bm.MovieService.RequestDTO.SeatCategoriesRequestDTO;
import in.bm.MovieService.RequestDTO.AddSeatRequestDTO;
import in.bm.MovieService.RequestDTO.SeatRequestDTO;
import in.bm.MovieService.ResponseDTO.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeatService {

    private final SeatRepo seatRepo;
    private final ScreenRepo screenRepo;
    private final SeatCategoryRepo seatCategoryRepo;

    public SeatBulkResponseDTO addSeats(AddSeatRequestDTO dto) {

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

        if (dto.getRowStart().toUpperCase(Locale.ROOT).charAt(0) > dto.getRowEnd().toUpperCase(Locale.ROOT).charAt(0)) {
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

        List<Seat> saved = seatRepo.saveAll(seats);

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
                .seatFeature(saved.stream().map(Seat::getSeatFeatures).toList())
                .seatLifecycle(saved.stream().map(Seat::getLifeCycle).toList())
                .build();
    }

    public SeatCategoriesResponseDTO updateSeatCategories(Long seatsId, @Valid SeatCategoriesRequestDTO requestDTO) {
        Seat seat = seatRepo.
                findById(seatsId).
                orElseThrow(() ->
                        new SeatNotFoundException("Seat not found"));


        seat.setViewType(requestDTO.getViewType());
        seat.setSeatFeatures(requestDTO.getSeatFeature());

        Seat updatedSeat = seatRepo.save(seat);

        return SeatCategoriesResponseDTO.
                builder().
                seatId(updatedSeat.getSeatId()).
                seatFeature(updatedSeat.getSeatFeatures()).
                seatNumber(updatedSeat.getSeatNumber()).
                viewType(updatedSeat.getViewType()).
                build();
    }

    public SeatPageResponseDTO getAllSeats(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Seat> seatPage = seatRepo.findAll(pageRequest);

        List<SeatResponseDTO> seats = seatPage.
                getContent().
                stream().
                map(seat -> SeatResponseDTO.builder().
                        screenId(seat.getSeatId()).
                        seatCategoryId(seat.getSeatCategory().getId()).
                        seatFeature(seat.getSeatFeatures()).
                        seatId(seat.getSeatId()).
                        seatNumber(seat.getSeatNumber()).
                        seatLifecycle(seat.getLifeCycle()).
                        viewType(seat.getViewType()).
                        build()).toList();

        return SeatPageResponseDTO.
                builder().
                seatResponses(seats).
                hasNext(seatPage.hasNext()).
                hasPrevious(seatPage.hasPrevious()).
                page(seatPage.getTotalPages()).
                size(seatPage.getSize()).
                totalElements(seatPage.getTotalElements()).
                totalPages(seatPage.getTotalPages()).
                build();
    }

    public SeatResponseDTO getSeatById(Long seatId) {
        Seat seat = seatRepo.
                findById(seatId).
                orElseThrow(() ->
                        new SeatNotFoundException("Seat not found"));

        return SeatResponseDTO.builder().
                screenId(seat.getSeatId()).
                seatCategoryId(seat.getSeatCategory().getId()).
                seatFeature(seat.getSeatFeatures()).
                seatId(seat.getSeatId()).
                seatNumber(seat.getSeatNumber()).
                seatLifecycle(seat.getLifeCycle()).
                viewType(seat.getViewType()).
                build();
    }

    public SeatLifeCycleResponseDTO deactivateSeat(Long seatId) {
        Seat seat = seatRepo.
                findById(seatId).
                orElseThrow(() ->
                        new SeatNotFoundException("Seat not found"));


        if(seat.getLifeCycle() == SeatLifecycle.INACTIVE){
            throw new IllegalStateException("Seat is already Inactive");
        }

        seat.setLifeCycle(SeatLifecycle.INACTIVE);
        Seat updatedSeat = seatRepo.save(seat);


        return SeatLifeCycleResponseDTO
                .builder()
                .seatNumber(updatedSeat.getSeatNumber())
                .message("Seat is no longer active")
                .build();
    }

    public SeatLifeCycleResponseDTO activateSeat(Long seatId) {
        Seat seat = seatRepo.
                findById(seatId).
                orElseThrow(() ->
                        new SeatNotFoundException("Seat not found"));

        if (seat.getLifeCycle() == SeatLifecycle.ACTIVE){
            throw new IllegalStateException("Seat is already Active");
        }

        seat.setLifeCycle(SeatLifecycle.ACTIVE);
        Seat updatedSeat = seatRepo.save(seat);

        return SeatLifeCycleResponseDTO
                .builder()
                .seatNumber(updatedSeat.getSeatNumber())
                .message("Seat is now active")
                .build();
    }

    public SeatResponseDTO updateSeat(Long seatId, @Valid SeatRequestDTO dto) {

        Seat seat = seatRepo.findById(seatId)
                .orElseThrow(() -> new SeatNotFoundException("Seat not found"));

        boolean isSameSeatNumber =
                Objects.equals(seat.getSeatNumber(), dto.getSeatNumber());

        if (!isSameSeatNumber){
            throw new SeatUnavailableException("Seat already exists with that seat number");
        }

        if (seat.getLifeCycle() == SeatLifecycle.INACTIVE) {
            throw new SeatInActiveException("Seat is no longer active");
        }

        if (dto.getScreenId() != null) {
            Screen screen = screenRepo.findById(dto.getScreenId())
                    .orElseThrow(() -> new ScreenNotFoundException("Screen not found"));
            seat.setScreen(screen);
        }

        if (dto.getSeatCategoryId() != null) {
            SeatCategory category = seatCategoryRepo.findById(dto.getSeatCategoryId())
                    .orElseThrow(() -> new SeatCategoryNotFoundException("Seat category not found"));
            seat.setSeatCategory(category);
        }

        if (dto.getSeatNumber() != null) {
            seat.setSeatNumber(dto.getSeatNumber());
        }

        Seat updatedSeat = seatRepo.save(seat);

        return SeatResponseDTO.builder()
                .seatId(updatedSeat.getSeatId())
                .screenId(updatedSeat.getScreen().getScreenId())
                .seatCategoryId(updatedSeat.getSeatCategory().getId())
                .seatNumber(updatedSeat.getSeatNumber())
                .seatFeature(updatedSeat.getSeatFeatures())
                .viewType(updatedSeat.getViewType())
                .seatLifecycle(updatedSeat.getLifeCycle())
                .build();
    }

    public void deleteSeat(Long seatId) {
        Seat seat = seatRepo.findById(seatId).orElseThrow(()->new SeatNotFoundException("Seat not found"));
        if (seat.getLifeCycle() != SeatLifecycle.INACTIVE){
            throw new SeatActiveException("Active seat cant be removed");
        }
        seatRepo.delete(seat);
    }
}
