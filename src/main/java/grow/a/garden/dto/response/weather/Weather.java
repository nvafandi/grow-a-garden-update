package grow.a.garden.dto.response.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {

    @JsonProperty("weather_id")
    private String weatherId;

    private boolean active;

    private int duration;

    @JsonProperty("weather_name")
    private String weatherName;

    private String icon;

}
