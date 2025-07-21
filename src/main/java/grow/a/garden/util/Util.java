package grow.a.garden.util;

import grow.a.garden.constant.Constant;
import grow.a.garden.dto.response.stok.*;
import grow.a.garden.repository.External;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
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
                        .map(item -> {
                            String name = item.getName();
                            if (name.contains(Constant.Gear.MASTER_SPRINKLER)) {
                                name = "**" + name + "**";
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

}
