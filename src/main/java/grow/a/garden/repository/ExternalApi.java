package grow.a.garden.repository;

import grow.a.garden.dto.response.items.ItemsReponse;
import grow.a.garden.dto.response.stok.UpdateStockResponse;
import grow.a.garden.dto.response.user.UsersResponse;
import grow.a.garden.dto.response.weather.UpdateWeatherResponse;
import grow.a.garden.dto.response.weather.Weather;

import java.util.List;
import java.util.Map;

public interface ExternalApi {

    UpdateStockResponse getStock();

    UsersResponse getUsers();

    void sendMessage(String message);

    void sendMessageV2(String message, List<String> users);

    boolean checkExistingMessage(String message);

    boolean checkExistWeather(String weather);

    UpdateWeatherResponse getWeather();

    Map<String, byte[]> wetherIcon(List<Weather> weatherList);

    List<ItemsReponse> getItems();

    void countEttemps(String key);

    void resetEttemps(String key);
}
