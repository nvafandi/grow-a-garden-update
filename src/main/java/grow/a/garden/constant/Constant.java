package grow.a.garden.constant;

public class Constant {

    public static class URL {
        public static String TELEGRAM_URL = "https://api.telegram.org/bot";
        public static String STOCK_URL = "https://gagstock.gleeze.com/grow-a-garden";
        public static String WEATHER_URL = "https://api.joshlei.com/v2/growagarden/weather";
        public static String ALL_ITEMS = "https://api.joshlei.com/v2/growagarden/info";
    }

    public static class Keys {
        public static String WEATHER_KEY = "|WEATHER|";
        public static String WEATHER = "weather";
        public static String STOCK_ETTEMPS = "|STOCK_ETTEMPS|";
        public static String WEATHER_ETTEMPS = "|WEATHER_ETTEMPS|";
    }

    public static class Message {
        public static final String SUCCESS_GET_STOCK = "Success get stock";
        public static final String SUCCESS_SEND_MESSAGE = "Success sent message";
        public static final String MESSAGE_ALREADY_SENT = "Message has already been sent";
        public static final String SUCCESS_GET_WEATHER = "Success get weather";
        public static final String FAILED_GET_WEATHER = "Failed get weather";
    }

    public static class Gear {
        public static final String BASIC_SPRINKLER = "Basic Sprinkler";
        public static final String ADVANCED_SPRINKLER = "Advanced Sprinkler";
        public static final String GODLY_SPRINKLER = "Godly Sprinkler";
        public static final String MASTER_SPRINKLER = "Master Sprinkler";
        public static final String TANNING_MIRROR = "Tanning Mirror";
        public static final String MEDIUM_TOY = "Medium Toy";
        public static final String MEDIUM_TREAT = "Medium Treat";
        public static final String LEVELUP_LOLLIPOP = "Levelup Lollipop";
    }

    public static class Seed {
        public static final String MUSHROOM = "Mushroom";
        public static final String BEANSTALK = "Beanstalk";
        public static final String EMBER_LILY = "Ember Lily";
        public static final String SUGAR_APPLE = "Sugar Apple";
        public static final String BURNING_BUD = "Burning Bud";
        public static final String GRAPE_SEED = "Grape Seed";
        public static final String GIANT_PINECONE = "Giant Pinecone";
    }

    public static class Egg {
        public static final String COMMON_SUMMER_EGG = "Common Summer Egg";
        public static final String RARE_SUMMER_EGG = "Rare Summer Egg";
        public static final String PARADISE_EGG = "Paradise Egg";
        public static final String BUG_EGG = "Bug Egg";
    }

    public static class Other {
        public static final String ADDITIONAL = "Additional";
        public static final String TRAVELING = "Traveling Merchant";
        public static final String LEAVED = "leaved";
    }

}
