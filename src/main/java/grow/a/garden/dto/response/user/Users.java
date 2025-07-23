package grow.a.garden.dto.response.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import grow.a.garden.entity.UsersEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Users {

    @JsonProperty("update_id")
    private long updateId;

    private Message message;

}
