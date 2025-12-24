package in.bm.MovieService.CONTROLLER;

import in.bm.MovieService.RequestDTO.ScreenRequestDTO;
import in.bm.MovieService.ResponseDTO.ScreenResponseDTO;
import in.bm.MovieService.SERVICE.ScreenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/screens")
@RequiredArgsConstructor
public class ScreenController{

    private final ScreenService screenService;

    @PostMapping
    public ResponseEntity<ScreenResponseDTO> addScreen(@Valid @RequestBody ScreenRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(screenService.addScreen(dto));
    }


}