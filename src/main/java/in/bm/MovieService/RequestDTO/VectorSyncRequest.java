package in.bm.MovieService.RequestDTO;

import in.bm.MovieService.ENTITY.Operation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VectorSyncRequest {

    public String movieCode;
    private Operation operation;


}
