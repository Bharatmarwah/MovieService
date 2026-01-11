package in.bm.MovieService.CONTROLLER;

import in.bm.MovieService.RequestDTO.ScreenRequestDTO;
import in.bm.MovieService.ResponseDTO.ScreenResponseDTO;
import in.bm.MovieService.ResponseDTO.ScreenStatusDTO;
import in.bm.MovieService.SERVICE.ScreenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/screens")
@RequiredArgsConstructor
public class ScreenController {

    private final ScreenService screenService;

    @PostMapping
    public ResponseEntity<ScreenResponseDTO> addScreen(@Valid @RequestBody ScreenRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(screenService.createScreen(dto));
    }

    @PutMapping("/{screenId}")
    public ResponseEntity<ScreenResponseDTO> updateScreen(@Valid @RequestBody ScreenRequestDTO requestDTO, @PathVariable Long screenId) {
        return ResponseEntity.status(HttpStatus.OK).body(screenService.updateScreenDetails(requestDTO, screenId));
    }

    @PatchMapping("/{screenId}/deactivate")
    public ResponseEntity<ScreenStatusDTO> deactivateScreen(@PathVariable Long screenId){
        return ResponseEntity.status(HttpStatus.OK).body(screenService.deactivateScreen(screenId));
    }

    @PatchMapping("/{screenId}/activate")
    public ResponseEntity<ScreenStatusDTO> activateScreen(@PathVariable Long screenId){
        return ResponseEntity.status(HttpStatus.OK).body(screenService.activateScreen(screenId));
    }

    @PatchMapping("/{screenId}/retire")
    public ResponseEntity<ScreenStatusDTO> retireScreen(@PathVariable Long screenId){
        return ResponseEntity.status(HttpStatus.OK).body(screenService.retireScreen(screenId));
    }


}