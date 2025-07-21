package grow.a.garden.dto.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Message {

    @JsonProperty("message_id")
    private long messageId;

    private From from;

    private Chat chat;

    private long date;

    private String text;

    private List<Entity> entities;

}
