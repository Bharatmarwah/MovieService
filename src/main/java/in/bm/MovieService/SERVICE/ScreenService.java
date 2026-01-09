package in.bm.MovieService.SERVICE;


import in.bm.MovieService.ENTITY.Screen;
import in.bm.MovieService.ENTITY.Theater;
import in.bm.MovieService.ENTITY.TheaterStatus;
import in.bm.MovieService.EXCEPTION.ScreenNotFoundException;
import in.bm.MovieService.EXCEPTION.TheaterInactiveException;
import in.bm.MovieService.EXCEPTION.TheaterNotFoundException;
import in.bm.MovieService.REPO.ScreenRepo;
import in.bm.MovieService.REPO.TheaterRepo;
import in.bm.MovieService.RequestDTO.ScreenRequestDTO;
import in.bm.MovieService.ResponseDTO.ScreenResponseDTO;
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


    public ScreenResponseDTO addScreen(ScreenRequestDTO dto) {
        log.info("Add Screen request | screenName={}", dto.getScreenName());

        Screen screen = new Screen();
        screen.setScreenName(dto.getScreenName());

        Theater theater = theaterRepo.findById(dto.getTheaterCode())
                .orElseThrow(() -> {
                    log.warn("Add screen failed | theater not found theaterCode={}",
                            dto.getTheaterCode());
                    return new TheaterNotFoundException("Theater not found");
                });

        if (theater.getStatus() != TheaterStatus.ACTIVE) {
            log.warn("Add screen rejected | theater inactive theaterCode={} status={}",
                    theater.getTheatreCode(), theater.getStatus());
            throw new TheaterInactiveException("Theater is not active");
        }
        screen.setTheater(theater);

        Screen savedScreen = screenRepo.save(screen);
        log.info("Screen added Successfully with id={}", savedScreen.getScreenId());

        return ScreenResponseDTO
                .builder()
                .screenId(savedScreen.getScreenId())
                .screenName(savedScreen.getScreenName())
                .theaterCode(savedScreen.getTheater().getTheatreCode())
                .build();
    }


    @Transactional
    public ScreenResponseDTO updateScreen(@Valid ScreenRequestDTO requestDTO, Long screenId) {

        Screen screen = screenRepo.findById(screenId)
                .orElseThrow(() ->
                        new ScreenNotFoundException("Screen not found"));

        screen.getTheater().setTheatreCode(requestDTO.getTheaterCode());
        screen.setScreenName(requestDTO.getScreenName());

        Screen updatedScreen = screenRepo.save(screen);

        return ScreenResponseDTO
                .builder()
                .screenId(updatedScreen.getScreenId())
                .theaterCode(updatedScreen.getTheater().getTheatreCode())
                .screenName(updatedScreen.getScreenName())
                .build();
    }

    @Transactional
    public void deleteScreen(Long screenId) {
        Screen screen = screenRepo
                .findById(screenId)
                .orElseThrow(() ->
                        new ScreenNotFoundException("Screen not found"));
        screenRepo.delete(screen);
    }
}