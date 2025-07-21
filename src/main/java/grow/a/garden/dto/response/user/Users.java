package grow.a.garden.dto.response.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Users {

    @JsonProperty("update_id")
    private long updateId;

    private Message message;

}
