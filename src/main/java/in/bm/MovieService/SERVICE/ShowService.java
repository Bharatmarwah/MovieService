    package in.bm.MovieService.SERVICE;

    import in.bm.MovieService.ENTITY.*;
    import in.bm.MovieService.EXCEPTION.*;
    import in.bm.MovieService.REPO.MovieRepo;
    import in.bm.MovieService.REPO.ScreenRepo;
    import in.bm.MovieService.REPO.ShowRepo;
    import in.bm.MovieService.REPO.ShowSeatRepo;
    import in.bm.MovieService.RequestDTO.ShowRequestDTO;
    import in.bm.MovieService.ResponseDTO.*;
    import jakarta.validation.Valid;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.cache.annotation.CacheEvict;
    import org.springframework.cache.annotation.Cacheable;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.time.LocalDate;
    import java.util.List;

    @Service
    @RequiredArgsConstructor
    @Slf4j
    public class ShowService {

        private final ShowRepo showRepo;
        private final ScreenRepo screenRepo;
        private final MovieRepo movieRepo;
        private final ShowSeatRepo showSeatRepo;

        // adding show with dto {screenId , movieCode , time and date}
        @CacheEvict(cacheNames = "shows", allEntries = true)
        @Transactional
        public ShowResponseDTO addShow(ShowRequestDTO dto) {

            Movie movie = movieRepo.findById(dto.getMovieCode())
                    .orElseThrow(() -> new MovieNotFoundException("Movie not found"));

            if (movie.getStatus() != MovieStatus.ACTIVE) {
                throw new MovieInactiveException("Movie is inactive");
            }

            Screen screen = screenRepo.findById(dto.getScreenId())
                    .orElseThrow(() -> new ScreenNotFoundException("Screen not found"));

            if (screen.getTheater().getStatus() != TheaterStatus.ACTIVE) {
                throw new TheaterInactiveException("Theater inactive");
            }

            Show show = new Show();
            show.setShowDate(dto.getShowDate());
            show.setShowTime(dto.getShowTime());
            show.setMovie(movie);
            show.setScreen(screen);

            Show savedShow = showRepo.save(show);


            // from the screen id we are fetching seats as per screen and adding the seats in the showSeat one by one
            List<ShowSeat> showSeats =
                    screen.getSeats().stream()
                            .filter(seat -> seat.getLifeCycle() == SeatLifecycle.ACTIVE)
                            .map(seat -> {
                                ShowSeat ss = new ShowSeat();
                                ss.setShow(savedShow);
                                ss.setSeat(seat);
                                ss.setSeatStatus(SeatStatus.AVAILABLE);

                                ss.setShowSeatId(
                                        Long.parseLong(savedShow.getShowId() + "00" + seat.getSeatId())
                                );
                                return ss;
                            })
                            .toList();

            showSeatRepo.saveAll(showSeats);

            return ShowResponseDTO.builder()
                    .showId(savedShow.getShowId())
                    .showDate(savedShow.getShowDate())
                    .showTime(savedShow.getShowTime())
                    .screenId(screen.getScreenId())
                    .movieCode(movie.getMovieCode())
                    .dayOfWeek(savedShow.getDayOfWeek())
                    .meridiem(savedShow.getMeridiem())
                    .build();
        }


        // we are updating the show but not the showSeats because seat status or etc screen responsibility
        @CacheEvict(cacheNames = "shows",allEntries = true)
        @Transactional
        public ShowResponseDTO updateShow(@Valid ShowRequestDTO dto, Long showId) {

            Show show = showRepo.findById(showId)
                    .orElseThrow(() -> new ShowNotFoundException("Show not found"));

            Movie movie = movieRepo.findById(dto.getMovieCode())
                    .orElseThrow(() -> new MovieNotFoundException("Movie not found"));

            if (movie.getStatus() != MovieStatus.ACTIVE) {
                throw new MovieInactiveException("Movie inactive");
            }

            Screen screen = screenRepo.findById(dto.getScreenId())
                    .orElseThrow(() -> new ScreenNotFoundException("Screen not found"));

            show.setMovie(movie);
            show.setScreen(screen);
            show.setShowDate(dto.getShowDate());
            show.setShowTime(dto.getShowTime());

            Show updated = showRepo.save(show);

            return ShowResponseDTO.builder()
                    .showId(updated.getShowId())
                    .showDate(updated.getShowDate())
                    .showTime(updated.getShowTime())
                    .screenId(updated.getScreen().getScreenId())
                    .movieCode(updated.getMovie().getMovieCode())
                    .dayOfWeek(updated.getDayOfWeek())
                    .meridiem(updated.getMeridiem())
                    .build();
        }


        // deleting the show
        @CacheEvict(cacheNames = "shows", allEntries = true)
        @Transactional
        public void deleteShow(Long showId) {
            Show show = showRepo.findById(showId)
                    .orElseThrow(() -> new ShowNotFoundException("Show not found"));
            showRepo.delete(show); // showSeats removed via orphanRemoval
        }


        // getting all the show by page and size using paginating
        @Cacheable(
                cacheNames = "shows",
                key = "'PAGE:' + #page + ':' + #size"
        )
        @Transactional(readOnly = true)
        public ShowPageResponseDTO getAllShow(int page, int size) {

            PageRequest pageRequest = PageRequest.of(page, size);
            Page<Show> showPage =
                    showRepo.findALlShowWithActiveMovies(pageRequest, MovieStatus.ACTIVE);

            List<ShowResponseDTO> shows =
                    showPage.getContent().stream()
                            .map(show -> ShowResponseDTO.builder()
                                    .showId(show.getShowId())
                                    .screenId(show.getScreen().getScreenId())
                                    .showTime(show.getShowTime())
                                    .showDate(show.getShowDate())
                                    .dayOfWeek(show.getDayOfWeek())
                                    .meridiem(show.getMeridiem())
                                    .movieCode(show.getMovie().getMovieCode())
                                    .build())
                            .toList();

            return ShowPageResponseDTO.builder()
                    .showResponses(shows)
                    .page(showPage.getNumber())
                    .size(showPage.getSize())
                    .totalElements(showPage.getTotalElements())
                    .totalPages(showPage.getTotalPages())
                    .hasNext(showPage.hasNext())
                    .hasPrevious(showPage.hasPrevious())
                    .build();
        }

        // getting the show by id
        @Cacheable(cacheNames = "shows",key = "#showId")
        public ShowResponseDTO getShowById(Long showId) {

            Show show = showRepo.findById(showId)
                    .orElseThrow(() -> new ShowNotFoundException("Show not found"));

            if (show.getMovie().getStatus() != MovieStatus.ACTIVE) {
                throw new MovieInactiveException("Movie inactive");
            }

            return ShowResponseDTO.builder()
                    .showId(show.getShowId())
                    .screenId(show.getScreen().getScreenId())
                    .showTime(show.getShowTime())
                    .showDate(show.getShowDate())
                    .dayOfWeek(show.getDayOfWeek())
                    .meridiem(show.getMeridiem())
                    .movieCode(show.getMovie().getMovieCode())
                    .build();
        }


        // getting shows by movieCode and city
        @Cacheable(
                cacheNames = "shows",
                key = "'MOVIE:' + #movieCode +':'+ #city")
        public List<ShowDateTimeResponseDTO> getShowsByMovieAndCity(String movieCode, String city) {

            return showRepo.findShowsByMovieCode(movieCode, MovieStatus.ACTIVE, city)
                    .stream()
                    .map(show -> ShowDateTimeResponseDTO.builder()
                            .showId(show.getShowId())
                            .showDate(show.getShowDate())
                            .showTime(show.getShowTime())
                            .dayOfWeek(show.getDayOfWeek())
                            .meridiem(show.getMeridiem())
                            .build())
                    .toList();
        }


        //getting showTime in theater columns as time like PVR nsp
        //                                                 11:00 PM
        @Cacheable(
                cacheNames = "shows",
                key = "'THEATER:' + #theatreCode + ':' + #date"
        )
        public List<ShowTimeResponseDTO> getShowsForTheater(
                String movieCode, String theatreCode, LocalDate date) {

            return showRepo.findShowsTime(movieCode, theatreCode, date)
                    .stream()
                    .map(show -> ShowTimeResponseDTO.builder()
                            .showId(show.getShowId())
                            .startTime(show.getShowTime())
                            .build())
                    .toList();
        }

        // theater filter will return the showId from which seats will be fetched
        @Transactional(readOnly = true)
        public ShowSeatsResponse seatsByShowId(Long showId) {

            Show show = showRepo.findById(showId)
                    .orElseThrow(() -> new ShowNotFoundException("Show not found"));

            List<ShowSeat> showSeats = showSeatRepo.findByShow_ShowId(showId);

            if (showSeats.isEmpty()) {
                throw new SeatNotFoundException("No seats for this show");
            }

            List<SeatsResponseDTO> seats =
                    showSeats.stream()
                            .map(ss -> {
                                Seat seat = ss.getSeat();
                                return SeatsResponseDTO.builder()
                                        .showSeatId(ss.getShowSeatId())
                                        .seatNumber(seat.getSeatNumber())
                                        .seatType(seat.getSeatCategory().getSeatType())
                                        .view(seat.getViewType())
                                        .feature(seat.getSeatFeatures())
                                        .price(seat.getSeatCategory().getPrice())
                                        .status(ss.getSeatStatus())
                                        .build();
                            })
                            .toList();

            return ShowSeatsResponse.builder()
                    .showId(show.getShowId())
                    .screenName(show.getScreen().getScreenName())
                    .movieCode(show.getMovie().getMovieCode())
                    .theaterCode(show.getScreen().getTheater().getTheatreCode())
                    .startTime(show.getShowTime())
                    .seats(seats)
                    .build();
        }
    }
