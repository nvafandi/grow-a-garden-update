package grow.a.garden.dto.response.stok;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

import java.time.LocalTime;
import java.time.OffsetDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Inventory {

    private Egg egg;

    private Gear gear;

    private Seed seed;

    private Honey honey;

    private Travelingmerchant travelingmerchant;

    private LocalTime updateAt;

    @JsonProperty("updated_at")
    @JsonSetter
    public void setUpdateAt(String updatedAt) {
        OffsetDateTime dateTime = OffsetDateTime.parse(updatedAt);
        this.updateAt = dateTime.toLocalTime().minusHours(1);
    }

}
