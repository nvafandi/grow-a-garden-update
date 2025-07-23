package grow.a.garden.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import grow.a.garden.constant.Constant;
import grow.a.garden.dto.response.stok.*;
import grow.a.garden.dto.response.weather.Weather;
import grow.a.garden.repository.ExternalApi;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class Util {

    private static ExternalApi externalApi = null;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Util(ExternalApi externalApi) {
        this.externalApi = externalApi;
    }

    public static String buildStockMessage(Inventory inventory) {
        StringBuilder message = new StringBuilder();

        Gear gear = inventory.getGear();
        Seed seed = inventory.getSeed();
        Egg egg = inventory.getEgg();
        Honey honey = inventory.getHoney();
        Travelingmerchant travelingmerchant = inventory.getTravelingmerchant();

        message.append("*Gear* \n").append(
                gear.getItems().stream()
                        .map(item -> {
                            String name = item.getName();
                            if (name.contains(Constant.Gear.MASTER_SPRINKLER)) {
                                name = "*" + name + "*";
                            }
                            return String.format("%s %s - %d", item.getEmoji(), name, item.getQuantity());
                        })
                        .collect(Collectors.joining("\n"))
        );

        message.append("\n\n*Seed* \n").append(
                seed.getItems().stream()
                        .map(item -> String.format("%s %s - %d", item.getEmoji(), item.getName(), item.getQuantity()))
                        .collect(Collectors.joining("\n"))
        );

        message.append("\n\n*Egg* \n").append(
                egg.getItems().stream()
                        .map(item -> String.format("%s %s - %d", item.getEmoji(), item.getName(), item.getQuantity()))
                        .collect(Collectors.joining("\n"))
        );

        if (honey != null) {
            message.append("\n\n*Additional* \n").append(
                    honey.getItems().stream()
                            .map(item -> String.format("%s %s - %d", item.getEmoji(), item.getName(), item.getQuantity()))
                            .collect(Collectors.joining("\n"))
            );
        }

        if (travelingmerchant != null && !travelingmerchant.getStatus().equals(Constant.Other.LEAVED)) {
            message.append("\n\n*Traveling Merchant* \n").append(
                    travelingmerchant.getItems().stream()
                            .map(item -> String.format("%s %s - %d", item.getEmoji(), item.getName(), item.getQuantity()))
                            .collect(Collectors.joining("\n"))
            );
        }

        message.append("\n\nupdated at : ")
                .append(inventory.getUpdateAt());

        return message.toString();
    }

    public static String buildWeatherMessage(List<Weather> weatherList) {
        StringBuilder message =  new StringBuilder();

        message.append("\uD83C\uDF26\uFE0F *Weather Event Alert!*\n")
                .append("\n\n");

        weatherList.stream()
                .filter(weather -> weather.isActive()) // Filter weather berdasarkan id yang ada di list
                .forEach(weather -> {
                    message.append("*" + weather.getWeatherName() + "*")
                            .append("\n\n\nDuration : ")
                            .append(weather.getDuration() / 60)
                            .append(" minutes");
                });

        return message.toString();
    }


    public static boolean isRare(String message) {
        return message.contains(Constant.Gear.BASIC_SPRINKLER) ||
                message.contains(Constant.Gear.ADVANCED_SPRINKLER) ||
                message.contains(Constant.Gear.GODLY_SPRINKLER) ||
                message.contains(Constant.Gear.MASTER_SPRINKLER) ||
                message.contains(Constant.Gear.MEDIUM_TOY) ||
                message.contains(Constant.Gear.MEDIUM_TREAT) ||
                message.contains(Constant.Gear.TANNING_MIRROR) ||
                message.contains(Constant.Gear.LEVELUP_LOLLIPOP) ||
                message.contains(Constant.Egg.COMMON_SUMMER_EGG) ||
                message.contains(Constant.Egg.RARE_SUMMER_EGG) ||
                message.contains(Constant.Egg.PARADISE_EGG) ||
                message.contains(Constant.Egg.BUG_EGG) ||
                message.contains(Constant.Seed.MUSHROOM) ||
                message.contains(Constant.Seed.BEANSTALK) ||
                message.contains(Constant.Seed.EMBER_LILY) ||
                message.contains(Constant.Seed.SUGAR_APPLE) ||
                message.contains(Constant.Seed.BURNING_BUD) ||
                message.contains(Constant.Seed.GIANT_PINECONE) ||
//                message.contains(Constant.ADDITIONAL) ||
                message.contains(Constant.Other.TRAVELING);
    }

    public static String getCurrentTimeFormatted() {
        ZonedDateTime nowJakarta = ZonedDateTime.now(ZoneId.of("Asia/Jakarta"));
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss", new Locale("id", "ID"));
        return nowJakarta.format(timeFormatter);
    }

    public static boolean isActive(List<Weather> weathers) {
        return weathers.stream()
                .anyMatch(Weather::isActive);
    }


    public static <T> String serialize(T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    public static <T> T deserialize(String json, TypeReference<T> typeReference) throws JsonProcessingException {
        if (json == null || json.isEmpty()) {
            // return empty object based on type, example for collections: empty list
            if (typeReference.getType().getTypeName().startsWith("java.util.List")) {
                return (T) Collections.emptyList();
            }
            return null;
        }
        return objectMapper.readValue(json, typeReference);
    }

    public static <T> boolean updateIfChanged(T oldValue, T newValue, Consumer<T> setter) {
        if (!Objects.equals(oldValue, newValue)) {
            setter.accept(newValue);
            return true;
        }
        return false;
    }

}
