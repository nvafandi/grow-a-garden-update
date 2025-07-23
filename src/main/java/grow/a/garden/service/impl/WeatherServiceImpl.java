package grow.a.garden.service.impl;

import grow.a.garden.constant.Constant;
import grow.a.garden.dto.response.base.BaseResponse;
import grow.a.garden.dto.response.weather.Weather;
import grow.a.garden.repository.ExternalApi;
import grow.a.garden.repository.jpa.ItemsJpaRepository;
import grow.a.garden.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    private final ExternalApi externalApi;

    private final ItemsJpaRepository itemsJpaRepository;

    public WeatherServiceImpl(ExternalApi externalApi, ItemsJpaRepository itemsJpaRepository) {
        this.externalApi = externalApi;
        this.itemsJpaRepository = itemsJpaRepository;
    }

    @Override
    public BaseResponse<Object> getWeather() {
        BaseResponse baseResponse;

        try {
            var item = itemsJpaRepository.findAllByType(Constant.Keys.WEATHER);

            if (item.isEmpty()) {
                return BaseResponse.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("Not weather found")
                        .build();
            }

            List<Weather> weatherList = item.stream()
                    .map(itemsEntity -> Weather.builder()
                            .weatherId(itemsEntity.getItemId())
                            .weatherName(itemsEntity.getDisplayName())
                            .icon(itemsEntity.getIcon())
                            .build()
                    )
                    .toList();

            baseResponse = BaseResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("Success get weather")
                    .data(weatherList)
                    .build();

        } catch (Exception e) {
            baseResponse = BaseResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error when get weather : " + e.getMessage())
                    .build();
        }

        return baseResponse;
    }

    @Override
    public BaseResponse<Object> getUpdateWeather() {
        BaseResponse baseResponse;

        try {
            var response = externalApi.getWeather();

            if (response == null) {
                return BaseResponse.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message(Constant.Message.FAILED_GET_WEATHER)
                        .build();
            }

            baseResponse = BaseResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message(Constant.Message.SUCCESS_GET_WEATHER)
                    .data(response)
                    .build();
        } catch (Exception e) {
            baseResponse = BaseResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error when get weather: {}" + e.getMessage())
                    .build();
        }
        return baseResponse;
    }

}
