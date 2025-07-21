package grow.a.garden.dto.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Chat {

    private long id;

    @JsonProperty("firstName")
    private String first_name;

    @JsonProperty("last_name")
    private String last_name;

    private String type;

}
