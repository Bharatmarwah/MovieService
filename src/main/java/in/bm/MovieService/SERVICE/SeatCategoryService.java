package in.bm.MovieService.SERVICE;

import in.bm.MovieService.ENTITY.Screen;
import in.bm.MovieService.ENTITY.SeatCategory;
import in.bm.MovieService.ENTITY.TheaterStatus;
import in.bm.MovieService.EXCEPTION.ScreenNotFoundException;
import in.bm.MovieService.EXCEPTION.SeatCategoryNotFoundException;
import in.bm.MovieService.EXCEPTION.TheaterInactiveException;
import in.bm.MovieService.REPO.ScreenRepo;
import in.bm.MovieService.REPO.SeatCategoryRepo;
import in.bm.MovieService.RequestDTO.SeatCategoryRequestDTO;
import in.bm.MovieService.ResponseDTO.SeatCategoryPageResponseDTO;
import in.bm.MovieService.ResponseDTO.SeatCategoryResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeatCategoryService {

    private final SeatCategoryRepo seatCategoryRepo;
    private final ScreenRepo screenRepo;


    @CacheEvict(cacheNames = "seatCategories",allEntries = true)
    @Transactional
    public SeatCategoryResponseDTO addSeatCategory(@Valid SeatCategoryRequestDTO dto) {

        log.info(
                "Add seat category request | seatType={} price={} screenId={}",
                dto.getSeatType(),
                dto.getPrize(),
                dto.getScreenId()
        );

        Screen screen = screenRepo.findById(dto.getScreenId())
                .orElseThrow(() -> {
                    log.warn("Add seat category failed | screen not found screenId={}", dto.getScreenId());
                    return new ScreenNotFoundException("Screen not found");
                });

        if (screen.getTheater().getStatus() != TheaterStatus.ACTIVE) {
            log.warn(
                    "Add seat category blocked | theater inactive theaterCode={} status={}",
                    screen.getTheater().getTheatreCode(),
                    screen.getTheater().getStatus()
            );
            throw new TheaterInactiveException("Theater is not active");
        }

        SeatCategory seatCategory = new SeatCategory();
        seatCategory.setSeatType(dto.getSeatType());
        seatCategory.setPrice(dto.getPrize());
        seatCategory.setScreen(screen);

        SeatCategory saved = seatCategoryRepo.save(seatCategory);

        log.info(
                "Seat category created | seatCategoryId={} screenId={} seatType={}",
                saved.getId(),
                screen.getScreenId(),
                saved.getSeatType()
        );

        return SeatCategoryResponseDTO.builder()
                .seatCategoryId(saved.getId())
                .screenId(saved.getScreen().getScreenId())
                .seatType(saved.getSeatType())
                .prize(saved.getPrice())
                .build();
    }

    @CacheEvict(cacheNames = "seatCategories",allEntries = true)
    @Transactional
    public SeatCategoryResponseDTO updateSeatCategory(
            @Valid SeatCategoryRequestDTO dto,
            Long seatCategoryId
    ) {

        log.info("Update seat category request | seatCategoryId={}", seatCategoryId);

        SeatCategory seatCategory = seatCategoryRepo.findById(seatCategoryId)
                .orElseThrow(() -> {
                    log.warn("Update failed | seat category not found seatCategoryId={}", seatCategoryId);
                    return new SeatCategoryNotFoundException("Seat category not found");
                });

        Screen screen = screenRepo.findById(dto.getScreenId())
                .orElseThrow(() -> {
                    log.warn("Update failed | screen not found screenId={}", dto.getScreenId());
                    return new ScreenNotFoundException("Screen not found");
                });

        if (screen.getTheater().getStatus() != TheaterStatus.ACTIVE) {
            log.warn(
                    "Update blocked | theater inactive theaterCode={} status={}",
                    screen.getTheater().getTheatreCode(),
                    screen.getTheater().getStatus()
            );
            throw new TheaterInactiveException("Theater is not active");
        }

        seatCategory.setScreen(screen);
        seatCategory.setPrice(dto.getPrize());
        seatCategory.setSeatType(dto.getSeatType());

        SeatCategory updated = seatCategoryRepo.save(seatCategory);

        log.info(
                "Seat category updated | seatCategoryId={} screenId={} seatType={}",
                updated.getId(),
                updated.getScreen().getScreenId(),
                updated.getSeatType()
        );

        return SeatCategoryResponseDTO.builder()
                .seatCategoryId(updated.getId())
                .screenId(updated.getScreen().getScreenId())
                .seatType(updated.getSeatType())
                .prize(updated.getPrice())
                .build();
    }

    @CacheEvict(cacheNames = "seatCategories",allEntries = true)
    @Transactional
    public void deleteSeatCategory(Long seatCategoryId) {

        log.info("Delete seat category request | seatCategoryId={}", seatCategoryId);

        SeatCategory seatCategory = seatCategoryRepo.findById(seatCategoryId)
                .orElseThrow(() -> {
                    log.warn("Delete failed | seat category not found seatCategoryId={}", seatCategoryId);
                    return new SeatCategoryNotFoundException("Seat category not found");
                });

        seatCategoryRepo.delete(seatCategory);

        log.info("Seat category deleted | seatCategoryId={}", seatCategoryId);
    }

    @Cacheable(cacheNames = "seatCategories",key = "#seatCategoryId")
    @Transactional(readOnly = true)
    public SeatCategoryResponseDTO getSeatCategoryById(Long seatCategoryId) {

        log.info("Fetch seat category by id | seatCategoryId={}", seatCategoryId);

        SeatCategory seatCategory = seatCategoryRepo.findById(seatCategoryId)
                .orElseThrow(() -> {
                    log.warn("Fetch failed | seat category not found seatCategoryId={}", seatCategoryId);
                    return new SeatCategoryNotFoundException("Seat category not found");
                });

        log.info(
                "Seat category fetched | seatCategoryId={} screenId={} seatType={}",
                seatCategory.getId(),
                seatCategory.getScreen().getScreenId(),
                seatCategory.getSeatType()
        );

        return SeatCategoryResponseDTO.builder()
                .seatCategoryId(seatCategory.getId())
                .screenId(seatCategory.getScreen().getScreenId())
                .seatType(seatCategory.getSeatType())
                .prize(seatCategory.getPrice())
                .build();
    }

    @Cacheable(
            cacheNames = "seatCategories",
            key = "'PAGE:' + #page + ':' + #size")
    @Transactional(readOnly = true)
    public SeatCategoryPageResponseDTO getAllSeatCategories(int page, int size) {

        log.info("Fetch all seat categories | page={} size={}", page, size);

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<SeatCategory> pageResult = seatCategoryRepo.findAll(pageRequest);

        List<SeatCategoryResponseDTO> categories = pageResult.getContent()
                .stream()
                .map(sc -> SeatCategoryResponseDTO.builder()
                        .seatCategoryId(sc.getId())
                        .screenId(sc.getScreen().getScreenId())
                        .seatType(sc.getSeatType())
                        .prize(sc.getPrice())
                        .build())
                .toList();

        log.info(
                "Seat categories fetched | page={} size={} totalElements={}",
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements()
        );

        return SeatCategoryPageResponseDTO.builder()
                .seatCategories(categories)
                .page(pageResult.getNumber())
                .size(pageResult.getSize())
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .hasNext(pageResult.hasNext())
                .hasPrevious(pageResult.hasPrevious())
                .build();
    }
}
