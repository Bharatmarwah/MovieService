package in.bm.MovieService.SERVICE;

import in.bm.MovieService.ENTITY.Screen;
import in.bm.MovieService.ENTITY.SeatCategory;
import in.bm.MovieService.ENTITY.TheaterStatus;
import in.bm.MovieService.EXCEPTION.ScreenNotFoundException;
import in.bm.MovieService.EXCEPTION.TheaterInactiveException;
import in.bm.MovieService.REPO.ScreenRepo;
import in.bm.MovieService.REPO.SeatCategoryRepo;
import in.bm.MovieService.RequestDTO.SeatCategoryRequestDTO;
import in.bm.MovieService.ResponseDTO.SeatCategoryResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeatCategoryService {

    private final SeatCategoryRepo seatCategoryRepo;
    private final ScreenRepo screenRepo;

    public SeatCategoryResponseDTO addSeatCategory(SeatCategoryRequestDTO dto) {

        log.info("Add seat category request | seatType={} price={} screenId={}",
                dto.getSeatType(), dto.getPrize(), dto.getScreenId());

        Screen screen = screenRepo.findById(dto.getScreenId())
                .orElseThrow(() -> {
                    log.warn("Add seat category failed | screen not found screenId={}",
                            dto.getScreenId());
                    return new ScreenNotFoundException("Screen not found");
                });

        if (screen.getTheater().getStatus() != TheaterStatus.ACTIVE) {
            log.warn("Add seat category rejected | theater inactive theaterCode={} status={}",
                    screen.getTheater().getTheatreCode(),
                    screen.getTheater().getStatus());
            throw new TheaterInactiveException("Theater is no longer active");
        }

        SeatCategory seatCategory = new SeatCategory();
        seatCategory.setSeatType(dto.getSeatType());
        seatCategory.setPrice(dto.getPrize());
        seatCategory.setScreen(screen);

        SeatCategory savedSeatCategory = seatCategoryRepo.save(seatCategory);

        log.info("Seat category added successfully | seatCategoryId={} screenId={} seatType={}",
                savedSeatCategory.getId(),
                screen.getScreenId(),
                savedSeatCategory.getSeatType());

        return SeatCategoryResponseDTO.builder()
                .seatCategoryId(savedSeatCategory.getId())
                .seatType(savedSeatCategory.getSeatType())
                .prize(savedSeatCategory.getPrice())
                .screenId(savedSeatCategory.getScreen().getScreenId())
                .build();
    }
}
