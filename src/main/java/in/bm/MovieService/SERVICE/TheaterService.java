package in.bm.MovieService.SERVICE;

import in.bm.MovieService.ENTITY.*;
import in.bm.MovieService.EXCEPTION.ReviewNotFoundException;
import in.bm.MovieService.EXCEPTION.TheaterInactiveException;
import in.bm.MovieService.EXCEPTION.TheaterNotFoundException;
import in.bm.MovieService.EXCEPTION.UnauthorizedActionException;
import in.bm.MovieService.REPO.TheaterRepo;
import in.bm.MovieService.REPO.TheaterReviewRepo;
import in.bm.MovieService.RequestDTO.TheaterRequestDto;
import in.bm.MovieService.RequestDTO.TheaterReviewRequestDTO;
import in.bm.MovieService.ResponseDTO.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TheaterService {

    private static final int EARTH_RADIUS_KM = 6371;
    private final TheaterRepo theaterRepo;
    private final TheaterReviewRepo theaterReviewRepo;
    private final ShowService showService;

    @Transactional
    public TheaterInfoDTO addTheater(TheaterRequestDto dto) {

        log.info("Add theater request | brand={} branchName={}",
                dto.getBrand(), dto.getBranchName());

        Theater theater = new Theater();
        theater.setTheatreCode(generateCode());
        theater.setBrand(dto.getBrand());
        theater.setBranchName(dto.getBranchName());
        theater.setCity(dto.getCity());
        theater.setArea(dto.getArea());
        theater.setLatitude(dto.getLatitude());
        theater.setLongitude(dto.getLongitude());
        theater.setAllowsCancellation(dto.getAllowsCancellation());
        theater.setStatus(TheaterStatus.PENDING);

        TheaterDetails details = new TheaterDetails();
        details.setRoadOrMall(dto.getRoadOrMall());
        details.setCityPinCode(dto.getCityPinCode());
        details.setCountry(dto.getCountry());
        details.setServicesAndAmenities(dto.getServicesAndAmenities());
        details.setTheater(theater);
        theater.setTheaterDetails(details);

        Theater saved = theaterRepo.save(theater);

        log.info("Theater created | theaterCode={} status={}",
                saved.getTheatreCode(), saved.getStatus());

        return mapToInfoDTO(saved);
    }

    @Transactional
    public TheaterInfoDTO updateTheater(String theaterCode, TheaterRequestDto dto) {

        log.info("Update theater request | theaterCode={}", theaterCode);

        Theater theater = theaterRepo.findById(theaterCode)
                .orElseThrow(() -> {
                    log.warn("Update rejected | theater not found theaterCode={}", theaterCode);
                    return new TheaterNotFoundException("Theater not found");
                });

        if (theater.getStatus() != TheaterStatus.ACTIVE) {
            log.warn("Update rejected | theater inactive theaterCode={} status={}",
                    theaterCode, theater.getStatus());
            throw new TheaterInactiveException("Theater is not active");
        }

        theater.setBrand(dto.getBrand());
        theater.setBranchName(dto.getBranchName());
        theater.setCity(dto.getCity());
        theater.setArea(dto.getArea());
        theater.setLatitude(dto.getLatitude());
        theater.setLongitude(dto.getLongitude());
        theater.setAllowsCancellation(dto.getAllowsCancellation());

        TheaterDetails details = theater.getTheaterDetails();
        details.setRoadOrMall(dto.getRoadOrMall());
        details.setCityPinCode(dto.getCityPinCode());
        details.setCountry(dto.getCountry());
        details.setServicesAndAmenities(dto.getServicesAndAmenities());

        log.info("Theater updated successfully | theaterCode={}", theaterCode);

        return mapToInfoDTO(theater);
    }

    @Transactional
    public TheaterStatusDTO activate(String theaterCode) {

        log.info("Activate theater request | theaterCode={}", theaterCode);

        Theater theater = theaterRepo.findById(theaterCode)
                .orElseThrow(() -> {
                    log.warn("Activation failed | theater not found theaterCode={}", theaterCode);
                    return new TheaterNotFoundException("Theater not found");
                });

        if (theater.getStatus() == TheaterStatus.ACTIVE) {
            log.warn("Activation rejected | already active theaterCode={}", theaterCode);
            throw new IllegalStateException("Theater is already active");
        }

        theater.setStatus(TheaterStatus.ACTIVE);
        theaterRepo.save(theater);

        log.info("Theater activated | theaterCode={}", theaterCode);

        return TheaterStatusDTO.builder()
                .theaterCode(theaterCode)
                .message("Theater activated successfully")
                .build();
    }

    @Transactional
    public TheaterStatusDTO deactivate(String theaterCode) {

        log.info("Deactivate theater request | theaterCode={}", theaterCode);

        Theater theater = theaterRepo.findById(theaterCode)
                .orElseThrow(() -> {
                    log.warn("Deactivation failed | theater not found theaterCode={}", theaterCode);
                    return new TheaterNotFoundException("Theater not found");
                });

        if (theater.getStatus() == TheaterStatus.INACTIVE) {
            log.warn("Deactivation rejected | already inactive theaterCode={}", theaterCode);
            throw new IllegalStateException("Theater is already inactive");
        }


        theater.setStatus(TheaterStatus.INACTIVE);
        theaterRepo.save(theater);

        log.info("Theater deactivated | theaterCode={}", theaterCode);

        return TheaterStatusDTO.builder()
                .theaterCode(theaterCode)
                .message("Theater is no longer active")
                .build();
    }

    @Transactional
    public TheaterDetailsResponseDTO getTheaterDetailsById(String theaterCode) {

        log.info("Fetch theater details request | theaterCode={}", theaterCode);

        Theater theater = theaterRepo.findById(theaterCode)
                .orElseThrow(() -> {
                    log.warn("Fetch rejected | theater not found theaterCode={}", theaterCode);
                    return new TheaterNotFoundException("Theater not found");
                });

        if (theater.getStatus() != TheaterStatus.ACTIVE) {
            log.warn("Fetch rejected | theater inactive theaterCode={}", theaterCode);
            throw new TheaterInactiveException("Theater is not active");
        }

        log.info("Theater details fetched | theaterCode={}", theaterCode);

        TheaterDetails details = theater.getTheaterDetails();

        return TheaterDetailsResponseDTO.builder()
                .theaterDetailsId(details.getId())
                .brand(theater.getBrand())
                .branchName(theater.getBranchName())
                .area(theater.getArea())
                .city(theater.getCity())
                .cityPinCode(details.getCityPinCode())
                .country(details.getCountry())
                .roadOrMall(details.getRoadOrMall())
                .servicesAndAmenities(details.getServicesAndAmenities())
                .build();
    }

    @Transactional
    public TheaterReviewResponseDTO addReview(String theaterCode, TheaterReviewRequestDTO dto, String userId) {

        log.info("Add review request | theaterCode={} user={}",
                theaterCode, dto.getUsername());

        Theater theater = theaterRepo.findById(theaterCode)
                .orElseThrow(() -> {
                    log.warn("Review rejected | theater not found theaterCode={}", theaterCode);
                    return new TheaterNotFoundException("Theater not found");
                });

        if (theater.getStatus() != TheaterStatus.ACTIVE) {
            log.warn("Review rejected | theater inactive theaterCode={}", theaterCode);
            throw new TheaterInactiveException("Theater is not active");
        }

        TheaterReview review = new TheaterReview();
        review.setUserId(userId);
        review.setUsername(dto.getUsername());
        review.setComment(dto.getComment());
        review.setRating(dto.getRating());
        review.setCreatedAt(Instant.now());
        review.setTheater(theater);

        theaterReviewRepo.save(review);
        recalculateRating(theater.getTheatreCode());

        log.info("Review added | reviewId={} theaterCode={}",
                review.getId(), theaterCode);

        return TheaterReviewResponseDTO.builder()
                .id(review.getId())
                .username(review.getUsername())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }

    @Transactional
    public TheaterResponseDTO getTheaterById(String theaterCode, double latitude, double longitude) {

        log.info("Fetch theater request | theaterCode={}", theaterCode);

        Theater theater = theaterRepo.findById(theaterCode)
                .orElseThrow(() -> {
                    log.warn("Fetch rejected | theater not found theaterCode={}", theaterCode);
                    return new TheaterNotFoundException("Theater not found");
                });

        if (theater.getStatus() != TheaterStatus.ACTIVE) {
            log.warn("Fetch rejected | theater inactive theaterCode={}", theaterCode);
            throw new TheaterInactiveException("Theater is not active");
        }

        double distance = distanceKm(
                latitude, longitude,
                theater.getLatitude(), theater.getLongitude()
        );

        log.info("Theater fetched | theaterCode={} distanceKm={}", theaterCode, distance);

        return TheaterResponseDTO.builder()
                .theaterCode(theater.getTheatreCode())
                .brand(theater.getBrand())
                .branchName(theater.getBranchName())
                .city(theater.getCity())
                .distanceKm(distance)
                .allowsCancellation(theater.isAllowsCancellation())
                .avgRating(theater.getAvgRating())
                .build();
    }

    @Transactional
    public TheaterReviewResponseDTO editReview(long reviewId, TheaterReviewRequestDTO dto,String userId) {

        log.info("Edit review request | reviewId={}", reviewId);

        TheaterReview review = theaterReviewRepo.findById(reviewId)
                .orElseThrow(() -> {
                    log.warn("Edit rejected | review not found reviewId={}", reviewId);
                    return new ReviewNotFoundException("Review not found");
                });

        if (review.getTheater().getStatus() != TheaterStatus.ACTIVE) {
            log.warn("Edit rejected | theater inactive reviewId={}", reviewId);
            throw new TheaterInactiveException("Theater is not active");
        }

        if (!review.getUserId().equals(userId)){
            throw new UnauthorizedActionException("Invalid user");
        }

        review.setUsername(dto.getUsername());
        review.setComment(dto.getComment());
        review.setRating(dto.getRating());

        TheaterReview saved = theaterReviewRepo.save(review);

        log.info("Review updated | reviewId={}", reviewId);

        return TheaterReviewResponseDTO.builder()
                .id(saved.getId())
                .comment(saved.getComment())
                .username(saved.getUsername())
                .rating(saved.getRating())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    public void deleteReview(long reviewID) {

        log.warn("Delete review request | reviewId={}", reviewID);

        TheaterReview review = theaterReviewRepo.findById(reviewID)
                .orElseThrow(() -> {
                    log.warn("Delete rejected | review not found reviewId={}", reviewID);
                    return new ReviewNotFoundException("Review not found");
                });

        theaterReviewRepo.delete(review);

        log.warn("Review deleted | reviewId={}", reviewID);
    }

    @Transactional
    public List<TheaterReviewResponseDTO> getAllReviews(String theaterCode) {

        log.info("Fetch all reviews request");

        List<TheaterReviewResponseDTO> reviews =
                theaterReviewRepo.findAllReviewsPerTheater(theaterCode)
                        .stream()
                        .map(r -> TheaterReviewResponseDTO.builder()
                                .id(r.getId())
                                .username(r.getUsername())
                                .comment(r.getComment())
                                .rating(r.getRating())
                                .createdAt(r.getCreatedAt())
                                .build())
                        .toList();

        log.info("All reviews fetched | count={}", reviews.size());

        return reviews;
    }

    private void recalculateRating(String theaterCode) {

        log.debug("Recalculate rating | theaterCode={}", theaterCode);

        Theater theater = theaterRepo.findById(theaterCode)
                .orElseThrow(() -> new TheaterNotFoundException("Theater not found"));

        double avg = theater.getTheaterReviews()
                .stream()
                .mapToDouble(TheaterReview::getRating)
                .average()
                .orElse(0.0);

        theater.setAvgRating(avg);
        theater.setTotalRatings(theater.getTheaterReviews().size());

        log.info("Rating updated | theaterCode={} avgRating={} totalRatings={}",
                theaterCode, avg, theater.getTotalRatings());
    }

    private TheaterInfoDTO mapToInfoDTO(Theater theater) {
        return TheaterInfoDTO.builder()
                .theaterCode(theater.getTheatreCode())
                .brand(theater.getBrand())
                .branchName(theater.getBranchName())
                .city(theater.getCity())
                .area(theater.getArea())
                .latitude(theater.getLatitude())
                .longitude(theater.getLongitude())
                .roadOrMall(theater.getTheaterDetails().getRoadOrMall())
                .cityPinCode(theater.getTheaterDetails().getCityPinCode())
                .country(theater.getTheaterDetails().getCountry())
                .allowsCancellation(theater.isAllowsCancellation())
                .servicesAndAmenities(theater.getTheaterDetails().getServicesAndAmenities())
                .status(theater.getStatus())
                .build();
    }

    private String generateCode() {
        return "TH" + UUID.randomUUID().toString().substring(0, 8);
    }

    private static double distanceKm(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        return Math.round(EARTH_RADIUS_KM * 2 *
                Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)) * 10.0) / 10.0;
    }

    @Transactional
    public TheaterPageResponseDTO getTheaterByStatus(TheaterStatus status, int page, int size) {

        log.info("Fetch theaters by status | status={} page={} size={}",
                status, page, size);

        Page<Theater> theaterPage =
                theaterRepo.findAllByStatus(status, PageRequest.of(page, size));

        log.info("Theaters fetched | status={} count={}",
                status, theaterPage.getNumberOfElements());

        List<TheaterInfoDTO> theaters = theaterPage.getContent()
                .stream()
                .map(this::mapToInfoDTO)
                .toList();

        return TheaterPageResponseDTO.builder()
                .theaters(theaters)
                .page(theaterPage.getNumber())
                .size(theaterPage.getSize())
                .totalElements(theaterPage.getTotalElements())
                .totalPages(theaterPage.getTotalPages())
                .hasNext(theaterPage.hasNext())
                .hasPrevious(theaterPage.hasPrevious())
                .build();
    }

    @Transactional
    public TheaterFilterPageResponseDTO searchFilter(
            String movieCode,
            String city,
            LocalDate date,
            Double seatPrice,
            int page,
            int size,
            double userLat,
            double userLon
    ) {

        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Theater> theaterPage =
                theaterRepo.filter(
                        city,
                        movieCode,
                        date,
                        seatPrice,
                        TheaterStatus.ACTIVE,
                        ScreenLifeCycle.ACTIVE,
                        pageRequest
                );

        List<TheaterResponseDTO> theaters =
                theaterPage.getContent()
                        .stream()
                        .map(theater -> {

                            double distanceKm = distanceKm(
                                    userLat,
                                    userLon,
                                    theater.getLatitude(),
                                    theater.getLongitude()
                            );

                          List<ShowTimeResponseDTO> shows =  showService
                                  .getShowsForTheater(movieCode
                                          ,theater.getTheatreCode()
                                          ,date);
                            return TheaterResponseDTO.builder()
                                    .theaterCode(theater.getTheatreCode())
                                    .brand(theater.getBrand())
                                    .branchName(theater.getBranchName())
                                    .city(theater.getCity())
                                    .avgRating(theater.getAvgRating())
                                    .allowsCancellation(theater.isAllowsCancellation())
                                    .distanceKm(distanceKm)
                                    .showTimeResponse(shows)
                                    .build();
                        })
                        .toList();

        return TheaterFilterPageResponseDTO.builder()
                .theaters(theaters)
                .page(theaterPage.getNumber())
                .size(theaterPage.getSize())
                .totalElements(theaterPage.getTotalElements())
                .totalPages(theaterPage.getTotalPages())
                .hasNext(theaterPage.hasNext())
                .hasPrevious(theaterPage.hasPrevious())
                .build();
    }

    @Transactional
    public void deleteReviewByUser(String userId, Long reviewId) {
        TheaterReview review = theaterReviewRepo.findById(reviewId).orElseThrow(()->new ReviewNotFoundException("Review not found"));
        if(!review.getUserId().equals(userId)){
            throw new UnauthorizedActionException("Invalid user");
        }
        theaterReviewRepo.delete(review);
    }
}
