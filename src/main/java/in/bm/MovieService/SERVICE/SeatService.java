package in.bm.MovieService.SERVICE;

import in.bm.MovieService.ENTITY.*;
import in.bm.MovieService.EXCEPTION.*;
import in.bm.MovieService.REPO.ScreenRepo;
import in.bm.MovieService.REPO.SeatCategoryRepo;
import in.bm.MovieService.REPO.SeatRepo;
import in.bm.MovieService.REPO.ShowSeatRepo;
import in.bm.MovieService.RequestDTO.*;
import in.bm.MovieService.ResponseDTO.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ShowSeatRepo showSeatRepo;


    @Transactional
    public SeatBulkResponseDTO addSeats(@org.jetbrains.annotations.NotNull AddSeatRequestDTO dto) {

        log.info(
                "Add seats request | screenId={} seatCategoryId={} rows {}-{} seatsPerRow={}",
                dto.getScreenId(),
                dto.getSeatCategoryId(),
                dto.getRowStart(),
                dto.getRowEnd(),
                dto.getSeatsPerRow()
        );

        Screen screen = screenRepo.findById(dto.getScreenId())
                .orElseThrow(() -> new ScreenNotFoundException("Screen not found"));

        SeatCategory seatCategory = seatCategoryRepo.findById(dto.getSeatCategoryId())
                .orElseThrow(() -> new SeatCategoryNotFoundException("Seat category not found"));

        if (dto.getRowStart().toUpperCase(Locale.ROOT).charAt(0)
                > dto.getRowEnd().toUpperCase(Locale.ROOT).charAt(0)) {
            throw new IllegalStateException("rowStart must be <= rowEnd");
        }

        char startRow = dto.getRowStart().toUpperCase().charAt(0);
        char endRow = dto.getRowEnd().toUpperCase().charAt(0);

        List<Seat> seats = new ArrayList<>();
        List<String> seatNumbers = new ArrayList<>();

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

        List<Seat> savedSeats = seatRepo.saveAll(seats);

        log.info(
                "Seats created successfully | screenId={} totalSeats={}",
                screen.getScreenId(),
                savedSeats.size()
        );

        return SeatBulkResponseDTO.builder()
                .screenId(screen.getScreenId())
                .seatCategoryId(seatCategory.getId())
                .totalSeatsCreated(savedSeats.size())
                .seatNumbers(seatNumbers)
                .seatFeature(savedSeats.stream().map(Seat::getSeatFeatures).toList())
                .seatLifecycle(savedSeats.stream().map(Seat::getLifeCycle).toList())
                .build();
    }

    @Transactional
    public SeatCategoriesResponseDTO updateSeatCategories(Long seatId,
                                                          @Valid SeatCategoriesRequestDTO dto) {

        log.info("Update seat categories request | seatId={}", seatId);

        Seat seat = seatRepo.findById(seatId)
                .orElseThrow(() -> new SeatNotFoundException("Seat not found"));

        seat.setViewType(dto.getViewType());
        seat.setSeatFeatures(dto.getSeatFeature());

        Seat updatedSeat = seatRepo.save(seat);

        log.info(
                "Seat categories updated | seatId={} feature={} viewType={}",
                updatedSeat.getSeatId(),
                updatedSeat.getSeatFeatures(),
                updatedSeat.getViewType()
        );

        return SeatCategoriesResponseDTO.builder()
                .seatId(updatedSeat.getSeatId())
                .seatNumber(updatedSeat.getSeatNumber())
                .seatFeature(updatedSeat.getSeatFeatures())
                .viewType(updatedSeat.getViewType())
                .build();
    }

    @Transactional(readOnly = true)
    public SeatPageResponseDTO getAllSeats(int page, int size) {

        log.info("Fetch all seats | page={} size={}", page, size);

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Seat> seatPage = seatRepo.findAll(pageRequest);

        List<SeatResponseDTO> seats = seatPage.getContent()
                .stream()
                .map(seat -> SeatResponseDTO.builder()
                        .seatId(seat.getSeatId())
                        .seatNumber(seat.getSeatNumber())
                        .screenId(seat.getScreen().getScreenId())
                        .seatCategoryId(seat.getSeatCategory().getId())
                        .seatFeature(seat.getSeatFeatures())
                        .seatLifecycle(seat.getLifeCycle())
                        .viewType(seat.getViewType())
                        .build())
                .toList();

        log.info(
                "Seats fetched | page={} size={} totalElements={}",
                seatPage.getNumber(),
                seatPage.getSize(),
                seatPage.getTotalElements()
        );

        return SeatPageResponseDTO.builder()
                .seatResponses(seats)
                .page(seatPage.getNumber())
                .size(seatPage.getSize())
                .totalElements(seatPage.getTotalElements())
                .totalPages(seatPage.getTotalPages())
                .hasNext(seatPage.hasNext())
                .hasPrevious(seatPage.hasPrevious())
                .build();
    }

    @Transactional(readOnly = true)
    public SeatResponseDTO getSeatById(Long seatId) {

        log.info("Fetch seat by id | seatId={}", seatId);

        Seat seat = seatRepo.findById(seatId)
                .orElseThrow(() -> new SeatNotFoundException("Seat not found"));

        log.info(
                "Seat fetched | seatId={} seatNumber={} screenId={}",
                seat.getSeatId(),
                seat.getSeatNumber(),
                seat.getScreen().getScreenId()
        );

        return SeatResponseDTO.builder()
                .seatId(seat.getSeatId())
                .seatNumber(seat.getSeatNumber())
                .screenId(seat.getScreen().getScreenId())
                .seatCategoryId(seat.getSeatCategory().getId())
                .seatFeature(seat.getSeatFeatures())
                .seatLifecycle(seat.getLifeCycle())
                .viewType(seat.getViewType())
                .build();
    }


    @Transactional
    public SeatLifeCycleResponseDTO deactivateSeat(Long seatId) {

        log.info("Deactivate seat request | seatId={}", seatId);

        Seat seat = seatRepo.findById(seatId)
                .orElseThrow(() -> new SeatNotFoundException("Seat not found"));

        if (seat.getLifeCycle() == SeatLifecycle.INACTIVE) {
            log.warn("Seat already inactive | seatId={}", seatId);
            throw new IllegalStateException("Seat is already inactive");
        }

        boolean usedInAnyShow =
                showSeatRepo.existsBySeat_SeatId(seatId);

        if (usedInAnyShow) {
            throw new IllegalStateException(
                    "Seat cannot be deactivated because it is used in existing shows"
            );
        }

        seat.setLifeCycle(SeatLifecycle.INACTIVE);

        Seat updatedSeat = seatRepo.save(seat);

        log.info(
                "Seat deactivated | seatId={} seatNumber={}",
                updatedSeat.getSeatId(),
                updatedSeat.getSeatNumber()
        );

        return SeatLifeCycleResponseDTO.builder()
                .seatNumber(updatedSeat.getSeatNumber())
                .message("Seat is no longer active")
                .build();
    }

    @Transactional
    public SeatLifeCycleResponseDTO activateSeat(Long seatId) {

        log.info("Activate seat request | seatId={}", seatId);

        Seat seat = seatRepo.findById(seatId)
                .orElseThrow(() -> new SeatNotFoundException("Seat not found"));

        if (seat.getLifeCycle() == SeatLifecycle.ACTIVE) {
            log.warn("Seat already active | seatId={}", seatId);
            throw new IllegalStateException("Seat is already active");
        }

        seat.setLifeCycle(SeatLifecycle.ACTIVE);
        Seat updatedSeat = seatRepo.save(seat);

        log.info(
                "Seat activated | seatId={} seatNumber={}",
                updatedSeat.getSeatId(),
                updatedSeat.getSeatNumber()
        );

        return SeatLifeCycleResponseDTO.builder()
                .seatNumber(updatedSeat.getSeatNumber())
                .message("Seat is now active")
                .build();
    }


    @Transactional
    public SeatResponseDTO updateSeat(Long seatId, @Valid SeatRequestDTO dto) {

        log.info(
                "Update seat request | seatId={} screenId={} seatCategoryId={}",
                seatId,
                dto.getScreenId(),
                dto.getSeatCategoryId()
        );

        Seat seat = seatRepo.findById(seatId)
                .orElseThrow(() -> new SeatNotFoundException("Seat not found"));

        if (seat.getLifeCycle() == SeatLifecycle.INACTIVE) {
            throw new SeatInActiveException("Inactive seat cannot be updated");
        }

        if (dto.getSeatNumber() != null &&
                !dto.getSeatNumber().equals(seat.getSeatNumber())) {

            log.warn(
                    "Seat number change attempt blocked | seatId={} requestedSeatNumber={}",
                    seatId,
                    dto.getSeatNumber()
            );

            throw new UnsupportedOperationException("Seat number cannot be changed");
        }

        if (dto.getScreenId() != null &&
                !dto.getScreenId().equals(seat.getScreen().getScreenId())) {

            Screen screen = screenRepo.findById(dto.getScreenId())
                    .orElseThrow(() -> new ScreenNotFoundException("Screen not found"));
            seat.setScreen(screen);
        }

        if (dto.getSeatCategoryId() != null &&
                !dto.getSeatCategoryId().equals(seat.getSeatCategory().getId())) {

            SeatCategory category = seatCategoryRepo.findById(dto.getSeatCategoryId())
                    .orElseThrow(() -> new SeatCategoryNotFoundException("Seat category not found"));
            seat.setSeatCategory(category);
        }

        Seat updatedSeat = seatRepo.save(seat);

        log.info(
                "Seat updated | seatId={} screenId={} seatCategoryId={}",
                updatedSeat.getSeatId(),
                updatedSeat.getScreen().getScreenId(),
                updatedSeat.getSeatCategory().getId()
        );

        return SeatResponseDTO.builder()
                .seatId(updatedSeat.getSeatId())
                .seatNumber(updatedSeat.getSeatNumber())
                .screenId(updatedSeat.getScreen().getScreenId())
                .seatCategoryId(updatedSeat.getSeatCategory().getId())
                .seatFeature(updatedSeat.getSeatFeatures())
                .viewType(updatedSeat.getViewType())
                .seatLifecycle(updatedSeat.getLifeCycle())
                .build();
    }

    @Transactional
    public void deleteSeat(Long seatId) {

        log.info("Delete seat request | seatId={}", seatId);

        Seat seat = seatRepo.findById(seatId)
                .orElseThrow(() -> new SeatNotFoundException("Seat not found"));

        if (seat.getLifeCycle() != SeatLifecycle.INACTIVE) {
            log.warn("Delete blocked for active seat | seatId={}", seatId);
            throw new SeatActiveException("Active seat cannot be removed");
        }

        boolean usedInAnyShow =
                showSeatRepo.existsBySeat_SeatId(seatId);

        if (usedInAnyShow) {
            throw new IllegalStateException(
                    "Seat cannot be deactivated because it is used in existing shows"
            );
        }

        seatRepo.delete(seat);

        log.info("Seat deleted | seatId={}", seatId);
    }
}
