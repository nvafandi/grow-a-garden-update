package grow.a.garden.dto.response.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {

    @JsonProperty("weather_id")
    private String weatherId;

    @JsonProperty("end_duration_unix")
    private int endDurationUnix;

    private boolean active;

    private int duration;

    @JsonProperty("start_duration_unix")
    private String startDurationUnix;

    @JsonProperty("weather_name")
    private String weatherName;

    private String icon;

}
