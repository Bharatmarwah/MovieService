package in.bm.MovieService.CONTROLLER;

import in.bm.MovieService.SERVICE.SeatService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/seats")
@RequiredArgsConstructor
public class SeatController{

    private final SeatService seatService;

// todo GET /shows?movieCode=MV123&theaterCode=T01

    // todo GET /shows/{showId}/seats

}