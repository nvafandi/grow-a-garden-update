package grow.a.garden.repository;

import grow.a.garden.dto.response.weather.Weather;

import java.util.List;

public interface WeatherRepository {

    List<Weather> listWeather();

    List<Weather> checkExistWeather();

    void storeWeather(List<Weather> weathers);

}
