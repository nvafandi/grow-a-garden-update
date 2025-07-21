package grow.a.garden.dto.response.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateWeatherResponse {

    @JsonProperty("discord_invite")
    private String discordInvite;

    List<Weather> weather;

}
