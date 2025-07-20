package grow.a.garden.dto.response.telegram;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramMessageResponse {

    private boolean ok;

    private Result result;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        private Long message_id;
        private Chat chat;
        private String text;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Chat {
            private Long id;
            private String first_name;
            private String last_name;
            private String type;
        }
    }

}
