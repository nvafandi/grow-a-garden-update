package grow.a.garden.dto.response.items;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemsReponse {

    @JsonProperty("item_id")
    private String itemId;

    @JsonProperty("display_name")
    private String displayName;

    private String rarity;

    private String currency;

    private String price;

    private String icon;

    private String description;

    private String duration;

    private String type;

}
