package grow.a.garden.service.impl;

import grow.a.garden.constant.Constant;
import grow.a.garden.dto.response.base.BaseResponse;
import grow.a.garden.dto.response.stok.*;
import grow.a.garden.dto.response.telegram.TelegramMessageResponse;
import grow.a.garden.service.StockService;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;

@Service
@Slf4j
public class StockServiceImpl implements StockService {

    public static final String C = "Rare";
    @Value("${base.url}")
    private String baseUrl;

    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.bot.channel.id}")
    private String chatId;


    private final RestTemplate restTemplate;

    private final RedisTemplate<String, String> redisTemplate;

    public StockServiceImpl(RestTemplate restTemplate, RedisTemplate<String, String> redisTemplate) {
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public BaseResponse<Object> getStock() {
        String url = "https://gagstock.gleeze.com/grow-a-garden";

        ResponseEntity<UpdateResponse> response = restTemplate.getForEntity(url, UpdateResponse.class);

        BaseResponse<Object> baseResponse;

        baseResponse = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(response.getBody().getStatus())
                .data(response.getBody().getData())
                .build();

        return baseResponse;
    }

    private String message() {
        String url = "https://gagstock.gleeze.com/grow-a-garden";

        ResponseEntity<UpdateResponse> response = restTemplate.getForEntity(url, UpdateResponse.class);

        Inventory inventory = response.getBody().getData();

        Gear gear = inventory.getGear();
        Seed seed = inventory.getSeed();
        Egg egg = inventory.getEgg();
        Honey honey = inventory.getHoney();
        Travelingmerchant travelingmerchant = inventory.getTravelingmerchant();

        StringBuilder message = new StringBuilder();

        message.append("*Gear* \n");

        message.append(
                gear.getItems().stream()
                        .map(item -> String.format("%s %s - %d", item.getEmoji(), item.getName(), item.getQuantity()))
                        .collect(Collectors.joining("\n"))
        );

        message.append("\n\n*Seed* \n");

        message.append(
                seed.getItems().stream()
                        .map(item -> String.format("%s %s - %d", item.getEmoji(), item.getName(), item.getQuantity()))
                        .collect(Collectors.joining("\n"))
        );

        message.append("\n\n*Egg* \n");

        message.append(
                egg.getItems().stream()
                        .map(item -> String.format("%s %s - %d", item.getEmoji(), item.getName(), item.getQuantity()))
                        .collect(Collectors.joining("\n"))
        );

        if (honey != null) {
            message.append("\n\n*Additional* \n");

            message.append(
                    honey.getItems().stream()
                            .map(item -> String.format("%s %s - %d", item.getEmoji(), item.getName(), item.getQuantity()))
                            .collect(Collectors.joining("\n"))
            );
        }

        if (travelingmerchant != null &&
            !travelingmerchant.getStatus().equals(Constant.LEAVED)) {
            message.append("\n\n*Traveling Merchant* \n");

            message.append(
                    travelingmerchant.getItems().stream()
                            .map(item -> String.format("%s %s - %d", item.getEmoji(), item.getName(), item.getQuantity()))
                            .collect(Collectors.joining("\n"))
            );
        }

        message.append("\n\nupdated at : ")
                .append(inventory.getUpdateAt());

        if (!isSame(message.toString())) message.setLength(0);

        return message.toString();
    }

    private boolean isRare(String message) {
        if (message.contains(Constant.MASTER_SPRINKLER) ||
                message.contains(Constant.RARE_SUMMER_EGG) ||
                message.contains(Constant.PARADISE_EGG) ||
                message.contains(Constant.BUG_EGG) ||
                message.contains(Constant.MUSHROOM_SEED) ||
                message.contains(Constant.BEANSTALK_SEED) ||
                message.contains(Constant.EMBER_LILY) ||
                message.contains(Constant.SUGAR_APPLE) ||
                message.contains(Constant.BURNING_BUD) ||
                message.contains(Constant.GIANT_PINECONE_SEED) ||
                message.contains(Constant.ADDITIONAL) ||
                message.contains(Constant.TRAVELING)
        ) {
            return true;
        }

        return false;
    }

    private boolean isSame(String message) {
        String hash = DigestUtils.md5DigestAsHex(message.getBytes());
        String key = "|MESSAGE|";

        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        String existingMessage = ops.get(key);

        if (existingMessage == null) {
            // Tidak ada di Redis, simpan
            ops.set(key, hash);
            return true;
        } else if (!existingMessage.equals(hash)) {
            // Ada tapi berbeda, update
            ops.set(key, hash);
            return true; // berarti boleh kirim pesan
        }

        return false;
    }

    @Scheduled(fixedRate = 30000) // setiap 30 detik
    private void updatedStock() {
        log.info("scheduler running");

        String message = message();

        log.warn("message : {}", message);

        StringBuilder url = new StringBuilder();
        url.append(baseUrl)
                .append(token)
                .append("/sendMessage?")
                .append("chat_id=")
                .append(chatId)
                .append("&text=")
                .append(message)
                .append("&parse_mode=Markdown");

        log.info("sendMessage: {}", url);

        if (isRare(message) && StringUtils.isNotBlank(message)) {
            restTemplate.exchange(
                    url.toString(),
                    HttpMethod.GET,
                    null,
                    TelegramMessageResponse.class
            );
            log.warn("message sent");
        }
    }

    @Override
    public BaseResponse<Object> sendMessage() {

        String message = message();

        StringBuilder url = new StringBuilder();
        url.append(baseUrl)
                .append(token)
                .append("/sendMessage?")
                .append("chat_id=")
                .append(chatId)
                .append("&text=")
                .append(message)
                .append("&parse_mode=Markdown");

        log.info("sendMessage: {}", url);

        ResponseEntity<TelegramMessageResponse> response = restTemplate.exchange(
                url.toString(),
                HttpMethod.GET,
                null,
                TelegramMessageResponse.class
        );

        TelegramMessageResponse body = response.getBody();

        BaseResponse<Object> baseResponse = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(body)
                .build();

        return baseResponse;
    }

}
