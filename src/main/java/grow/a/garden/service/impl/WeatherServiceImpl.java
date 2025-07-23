package grow.a.garden.service.impl;

import grow.a.garden.constant.Constant;
import grow.a.garden.dto.response.base.BaseResponse;
import grow.a.garden.dto.response.user.UsersResponse;
import grow.a.garden.repository.ExternalApi;
import grow.a.garden.repository.UserRepository;
import grow.a.garden.repository.WeatherRepository;
import grow.a.garden.repository.jpa.UsersJpaRepository;
import grow.a.garden.service.WeatherService;
import grow.a.garden.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    private final ExternalApi externalApi;

    private final WeatherRepository weatherRepository;

    private final UserRepository userRepository;

    private final UsersJpaRepository usersJpaRepository;

    public WeatherServiceImpl(ExternalApi externalApi, WeatherRepository weatherRepository, UserRepository userRepository, UsersJpaRepository usersJpaRepository) {
        this.externalApi = externalApi;
        this.weatherRepository = weatherRepository;
        this.userRepository = userRepository;
        this.usersJpaRepository = usersJpaRepository;
    }

    @Override
    public BaseResponse<Object> getUpdateWeather() {
        BaseResponse baseResponse;

        try {
            var response = externalApi.getWeather();

//            updateWeather();

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

    @Scheduled(fixedRate = 15000)
    private void updateWeather() {
        var weather = weatherRepository.listWeather();

        if (Util.isActive(weather)) {
            log.info("Check User");
            var users = userRepository.checkExistUser();

            if (users.isEmpty()) {
                var userEntities = usersJpaRepository.findAll();

                if (userEntities.isEmpty()) {
                    var getUsers = externalApi.getUsers();
                    userRepository.saveUser(getUsers);
                    userRepository.storeUser(UsersResponse.fromUsersList(getUsers.getResult()));
                } else {
                    var userList = userEntities.stream()
                            .map(usersEntity -> usersEntity.getId().toString())
                            .distinct()
                            .toList();
                    userRepository.storeUser(userList);
                }
            }

            var message = Util.buildWeatherMessage(weather);

            if (externalApi.checkExistWeather(message)) {
                externalApi.sendMessageV2(message, users);
            }
        } else {
            log.info("No active weather");
        }
    }
}
