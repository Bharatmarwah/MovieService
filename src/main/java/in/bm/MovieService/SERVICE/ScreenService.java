package in.bm.MovieService.SERVICE;

import in.bm.MovieService.ENTITY.Screen;
import in.bm.MovieService.ENTITY.ScreenLifeCycle;
import in.bm.MovieService.ENTITY.Theater;
import in.bm.MovieService.ENTITY.TheaterStatus;
import in.bm.MovieService.EXCEPTION.*;
import in.bm.MovieService.REPO.ScreenRepo;
import in.bm.MovieService.REPO.ShowRepo;
import in.bm.MovieService.REPO.TheaterRepo;
import in.bm.MovieService.RequestDTO.ScreenRequestDTO;
import in.bm.MovieService.ResponseDTO.ScreenResponseDTO;
import in.bm.MovieService.ResponseDTO.ScreenStatusDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScreenService {

    private final ScreenRepo screenRepo;
    private final TheaterRepo theaterRepo;
    private final ShowRepo showRepo;



    @Transactional
    public ScreenResponseDTO createScreen(@Valid ScreenRequestDTO dto) {

        log.info("Create screen request | screenName={} theaterCode={}",
                dto.getScreenName(), dto.getTheaterCode());

        Theater theater = theaterRepo.findById(dto.getTheaterCode())
                .orElseThrow(() -> {
                    log.warn("Create screen failed | theater not found theaterCode={}",
                            dto.getTheaterCode());
                    return new TheaterNotFoundException("Theater not found");
                });

        if (theater.getStatus() != TheaterStatus.ACTIVE) {
            log.warn("Create screen blocked | theater inactive theaterCode={} status={}",
                    theater.getTheatreCode(), theater.getStatus());
            throw new TheaterInactiveException("Theater is not active");
        }

        Screen screen = new Screen();
        screen.setScreenName(dto.getScreenName());
        screen.setTheater(theater);
        screen.setScreenLifeCycle(ScreenLifeCycle.INACTIVE);

        Screen saved = screenRepo.save(screen);

        log.info("Screen created | screenId={} screenName={}",
                saved.getScreenId(), saved.getScreenName());

        return ScreenResponseDTO.builder()
                .screenId(saved.getScreenId())
                .screenName(saved.getScreenName())
                .theaterCode(saved.getTheater().getTheatreCode())
                .screenLifecycle(saved.getScreenLifeCycle())
                .build();
    }


    @Transactional
    public ScreenResponseDTO updateScreenDetails(
            @Valid ScreenRequestDTO dto,
            Long screenId
    ) {

        log.info("Update screen request | screenId={} newName={} newTheaterCode={}",
                screenId, dto.getScreenName(), dto.getTheaterCode());

        Screen screen = screenRepo.findById(screenId)
                .orElseThrow(() -> {
                    log.warn("Update failed | screen not found screenId={}", screenId);
                    return new ScreenNotFoundException("Screen not found");
                });

        if (screen.getScreenLifeCycle() != ScreenLifeCycle.ACTIVE) {
            log.warn("Update blocked | screen inactive screenId={} state={}",
                    screenId, screen.getScreenLifeCycle());
            throw new ScreenInActiveException("Screen must be active to update");
        }

        Theater theater = theaterRepo.findById(dto.getTheaterCode())
                .orElseThrow(() -> {
                    log.warn("Update failed | theater not found theaterCode={}",
                            dto.getTheaterCode());
                    return new TheaterNotFoundException("Theater not found");
                });

        if (theater.getStatus() != TheaterStatus.ACTIVE) {
            log.warn("Update blocked | theater inactive theaterCode={} status={}",
                    theater.getTheatreCode(), theater.getStatus());
            throw new TheaterInactiveException("Theater is not active");
        }

        screen.setScreenName(dto.getScreenName());
        screen.setTheater(theater);

        Screen updated = screenRepo.save(screen);

        log.info("Screen updated | screenId={} screenName={} theaterCode={}",
                updated.getScreenId(),
                updated.getScreenName(),
                updated.getTheater().getTheatreCode());

        return ScreenResponseDTO.builder()
                .screenId(updated.getScreenId())
                .screenName(updated.getScreenName())
                .theaterCode(updated.getTheater().getTheatreCode())
                .screenLifecycle(updated.getScreenLifeCycle())
                .build();
    }


    @Transactional
    public ScreenStatusDTO activateScreen(Long screenId) {

        log.info("Activate screen request | screenId={}", screenId);

        Screen screen = screenRepo.findById(screenId)
                .orElseThrow(() -> {
                    log.warn("Activate failed | screen not found screenId={}", screenId);
                    return new ScreenNotFoundException("Screen not found");
                });

        if (screen.getScreenLifeCycle() == ScreenLifeCycle.ACTIVE) {
            log.warn("Activate blocked | screen already active screenId={}", screenId);
            throw new IllegalStateException("Screen already active");
        }

        screen.setScreenLifeCycle(ScreenLifeCycle.ACTIVE);
        screenRepo.save(screen);

        log.info("Screen activated | screenId={}", screenId);

        return ScreenStatusDTO.builder()
                .screenId(screenId)
                .message("Screen is now active")
                .build();
    }

    @Transactional
    public ScreenStatusDTO deactivateScreen(Long screenId) {

        log.info("Deactivate screen request | screenId={}", screenId);

        Screen screen = screenRepo.findById(screenId)
                .orElseThrow(() -> {
                    log.warn("Deactivate failed | screen not found screenId={}", screenId);
                    return new ScreenNotFoundException("Screen not found");
                });

        if (screen.getScreenLifeCycle() == ScreenLifeCycle.INACTIVE) {
            log.warn("Deactivate blocked | screen already inactive screenId={}", screenId);
            throw new ScreenInActiveException("Screen already inactive");
        }

        boolean hasShows = showRepo.existsByScreen_ScreenId(screenId);

        if (hasShows) {
            throw new IllegalStateException(
                    "Screen cannot be deactivated because shows exist on it"
            );
        }


        screen.setScreenLifeCycle(ScreenLifeCycle.INACTIVE);
        screenRepo.save(screen);

        log.info("Screen deactivated | screenId={}", screenId);

        return ScreenStatusDTO.builder()
                .screenId(screenId)
                .message("Screen is no longer active")
                .build();
    }


    @Transactional
    public ScreenStatusDTO retireScreen(Long screenId) {

        log.info("Retire screen request | screenId={}", screenId);

        Screen screen = screenRepo.findById(screenId)
                .orElseThrow(() -> {
                    log.warn("Retire failed | screen not found screenId={}", screenId);
                    return new ScreenNotFoundException("Screen not found");
                });

        if (screen.getScreenLifeCycle() != ScreenLifeCycle.INACTIVE) {
            log.warn("Retire blocked | screen not inactive screenId={} state={}",
                    screenId, screen.getScreenLifeCycle());
            throw new ScreenInActiveException("Screen must be inactive before retiring");
        }

        boolean hasShows = showRepo.existsByScreen_ScreenId(screenId);

        if (hasShows) {
            throw new IllegalStateException(
                    "Screen cannot be retired because shows exist on it"
            );
        }


        screen.setScreenLifeCycle(ScreenLifeCycle.RETIRED);
        screenRepo.save(screen);

        log.info("Screen retired | screenId={}", screenId);

        return ScreenStatusDTO.builder()
                .screenId(screenId)
                .message("Screen is now retired")
                .build();
    }
}
