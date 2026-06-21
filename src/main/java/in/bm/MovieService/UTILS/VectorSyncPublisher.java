package in.bm.MovieService.UTILS;

import in.bm.MovieService.CLIENT.VectorSyncClient;
import in.bm.MovieService.ENTITY.Operation;
import in.bm.MovieService.RequestDTO.VectorSyncRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class VectorSyncPublisher {

    private final VectorSyncClient vectorSyncClient;

    public void upsertMovie(String movieCode) {

        try {
            vectorSyncClient.syncVectorData(
                    new VectorSyncRequest(
                            movieCode,
                            Operation.UPSERT
                    )
            );
        } catch (Exception e) {
            log.error("Error while syncing vector data for movieId: {}", movieCode);
        }
    }

    public void deleteMovie(String movieCode) {

        try {
            vectorSyncClient.syncVectorData(
                    new VectorSyncRequest(
                            movieCode,
                            Operation.DELETE
                    )
            );
        }catch (Exception e) {
            log.error("Error while syncing vector data for movieId: {}", movieCode);
        }
    }
}