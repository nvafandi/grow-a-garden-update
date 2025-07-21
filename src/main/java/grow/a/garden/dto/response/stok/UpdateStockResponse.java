package grow.a.garden.dto.response.stok;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateStockResponse {

    private String status;

    @JsonProperty("update_at")
    private String updateAt;

    private Inventory data;

}
