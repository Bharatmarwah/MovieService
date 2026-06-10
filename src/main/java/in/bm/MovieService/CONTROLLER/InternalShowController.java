package in.bm.MovieService.CONTROLLER;

import in.bm.MovieService.RequestDTO.InternalShowRequestDTO;
import in.bm.MovieService.ResponseDTO.InternalShowResponse;
import in.bm.MovieService.SERVICE.InternalShowBookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/shows")
@RequiredArgsConstructor
public class InternalShowController {

    private final InternalShowBookingService showBookingService;

    @PostMapping("/validate")
    @ResponseStatus(HttpStatus.OK)
    public InternalShowResponse validateShowForBooking(
            @Valid @RequestBody InternalShowRequestDTO showRequestDTO) {

        return showBookingService.validateShowForBooking(showRequestDTO);
    }
}

