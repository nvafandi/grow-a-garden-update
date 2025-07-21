package grow.a.garden.dto.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class From {

    private long id;

    @JsonProperty("is_bot")
    private boolean isBot;

    @JsonProperty("firstName")
    private String first_name;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("language_code")
    private String languageCode;

}
