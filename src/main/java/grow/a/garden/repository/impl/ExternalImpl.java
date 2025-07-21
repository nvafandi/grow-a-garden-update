package grow.a.garden.repository.impl;

import grow.a.garden.dto.response.stok.UpdateResponse;
import grow.a.garden.dto.response.telegram.TelegramMessageResponse;
import grow.a.garden.repository.External;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Repository
@Slf4j
public class ExternalImpl implements External {

    @Value("${base.url}")
    private String baseUrl;

    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.bot.channel.id}")
    private String chatId;

    @Value("${redis.timeout}")
    private int duration;

    @Value("${external.url}")
    private String externalUrl;

    private final RestTemplate restTemplate;

    private RedisTemplate<String, String> redisTemplate;

    public ExternalImpl(RestTemplate restTemplate, RedisTemplate<String, String> redisTemplate) {
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public UpdateResponse getStock() {
        UpdateResponse updateResponse = new UpdateResponse();
        try {
            ResponseEntity<UpdateResponse> response = restTemplate.getForEntity(externalUrl, UpdateResponse.class);
            if (response.getStatusCode().equals(HttpStatus.OK)) {
                updateResponse = response.getBody();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updateResponse;
    }

    @Override
    public void sendMessage(String message) {
        try {
            StringBuilder url = new StringBuilder();
            url.append(baseUrl)
                    .append(token)
                    .append("/sendMessage?")
                    .append("chat_id=")
                    .append(chatId)
                    .append("&text=")
                    .append(message)
                    .append("&parse_mode=Markdown");

            restTemplate.exchange(
                    url.toString(),
                    HttpMethod.GET,
                    null,
                    TelegramMessageResponse.class
            );
            log.info("Message sent");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Message failed to send: {}", e.getMessage());
        }
    }

    @Override
    public boolean isMessageSame(String message) {
        String hash = DigestUtils.md5DigestAsHex(message.getBytes());
        String key = "|MESSAGE|";

        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String existingMessage = ops.get(key);

        if (existingMessage == null) {
            ops.set(key, hash, Duration.ofHours(duration)); // new message
            return true;
        } else if (!existingMessage.equals(hash)) {
            ops.set(key, hash, Duration.ofHours(duration)); // message already in redis but different
            return true;
        }
        log.info("message still the same");
        return false;
    }

}
