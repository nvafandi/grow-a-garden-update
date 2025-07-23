package grow.a.garden.repository.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import grow.a.garden.constant.Constant;
import grow.a.garden.dto.response.weather.Weather;
import grow.a.garden.repository.ExternalApi;
import grow.a.garden.repository.WeatherRepository;
import grow.a.garden.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;

@Repository
@Slf4j
public class WeatherRepositoryImpl implements WeatherRepository {

    @Value("${redis.timeout}")
    private int duration;

    private final ExternalApi externalApi;

    private final RedisTemplate<String, String> redisTemplate;

    public WeatherRepositoryImpl(ExternalApi externalApi, RedisTemplate<String, String> redisTemplate) {
        this.externalApi = externalApi;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<Weather> listWeather() {
        var weatherList = externalApi.getWeather().getWeather();

        if (!weatherList.isEmpty()) {
            return weatherList;
        }

        return List.of();
    }

    @Override
    public List<Weather> checkExistWeather() {
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();

        String value = valueOps.get(Constant.Keys.WEATHER_KEY);

        try {
            if (value != null && !value.isEmpty()) {
                return Util.deserialize(value, new TypeReference<>() {
                });
            }

        } catch (Exception e) {
            log.error("Error when get weather in redis: {}", e.getMessage());
        }

        return List.of();
    }

    @Override
    public void storeWeather(List<Weather> weathers) {
        log.info("store weather");
        try {
            String json = Util.serialize(weathers);

            ValueOperations<String, String> valueOps = redisTemplate.opsForValue();

            valueOps.set(Constant.Keys.WEATHER_KEY, json, Duration.ofHours(duration));
        } catch (Exception e) {
            log.error("Error when parse json: {}", e.getMessage());
        }
    }
}
