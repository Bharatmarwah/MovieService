package in.bm.MovieService.CLIENT;

import in.bm.MovieService.RequestDTO.VectorSyncRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class VectorSyncClient {

    private final WebClient.Builder webClientBuilder;

    public void syncVectorData(VectorSyncRequest vectorSyncRequest){
        webClientBuilder.build()
                .post()
                .uri("/internal/vector/sync")
                .bodyValue(vectorSyncRequest)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

}
