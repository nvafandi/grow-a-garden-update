package grow.a.garden.service;

import grow.a.garden.dto.response.base.BaseResponse;

public interface WeatherService {

    BaseResponse<Object> getUpdateWeather();

}
