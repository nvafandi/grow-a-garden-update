package grow.a.garden.util;

import grow.a.garden.constant.Constant;
import grow.a.garden.dto.response.stok.*;
import grow.a.garden.repository.External;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class Util {

    private static External external = null;

    public Util(External external) {
        this.external = external;
    }

    public static String buildMessage(Inventory inventory) {
        StringBuilder message = new StringBuilder();

        Gear gear = inventory.getGear();
        Seed seed = inventory.getSeed();
        Egg egg = inventory.getEgg();
        Honey honey = inventory.getHoney();
        Travelingmerchant travelingmerchant = inventory.getTravelingmerchant();

        message.append("*Gear* \n").append(
                gear.getItems().stream()
                        .map(item -> String.format("%s %s - %d", item.getEmoji(), item.getName(), item.getQuantity()))
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

        if (travelingmerchant != null && !travelingmerchant.getStatus().equals(Constant.LEAVED)) {
            message.append("\n\n*Traveling Merchant* \n").append(
                    travelingmerchant.getItems().stream()
                            .map(item -> String.format("%s %s - %d", item.getEmoji(), item.getName(), item.getQuantity()))
                            .collect(Collectors.joining("\n"))
            );
        }

        message.append("\n\nupdated at : ")
                .append(inventory.getUpdateAt());

        if (!external.isMessageSame(message.toString())) return "";

        message.append("\nsent at : ")
                .append(Util.getCurrentTimeFormatted());

        return message.toString();
    }

    public static boolean isRare(String message) {
        return message.contains(Constant.MASTER_SPRINKLER) ||
                message.contains(Constant.RARE_SUMMER_EGG) ||
                message.contains(Constant.PARADISE_EGG) ||
                message.contains(Constant.BUG_EGG) ||
                message.contains(Constant.MUSHROOM_SEED) ||
                message.contains(Constant.BEANSTALK_SEED) ||
                message.contains(Constant.EMBER_LILY) ||
                message.contains(Constant.SUGAR_APPLE) ||
                message.contains(Constant.BURNING_BUD) ||
                message.contains(Constant.GIANT_PINECONE_SEED) ||
//                message.contains(Constant.ADDITIONAL) ||
                message.contains(Constant.TRAVELING);
    }

    public static String getCurrentTimeFormatted() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss", new Locale("id", "ID"));
        LocalTime time = LocalTime.now();
        return time.format(timeFormatter);
    }

}
