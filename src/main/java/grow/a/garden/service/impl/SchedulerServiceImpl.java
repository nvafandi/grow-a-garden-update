package grow.a.garden.service.impl;

import grow.a.garden.constant.Constant;
import grow.a.garden.dto.response.user.UsersResponse;
import grow.a.garden.repository.ExternalApi;
import grow.a.garden.repository.UserRepository;
import grow.a.garden.repository.WeatherRepository;
import grow.a.garden.repository.jpa.UsersJpaRepository;
import grow.a.garden.util.Util;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SchedulerServiceImpl {

    private final ExternalApi externalApi;

    private final UserRepository userRepository;

    private final WeatherRepository weatherRepository;

    private final UsersJpaRepository usersJpaRepository;

    public SchedulerServiceImpl(ExternalApi externalApi, UserRepository userRepository, WeatherRepository weatherRepository, UsersJpaRepository usersJpaRepository) {
        this.externalApi = externalApi;
        this.userRepository = userRepository;
        this.weatherRepository = weatherRepository;
        this.usersJpaRepository = usersJpaRepository;
    }

    @Scheduled(fixedRate = 15000) // setiap 15 detik
    public void updatedStock() {
        log.info("Begin scheduler");

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

        var stockData = externalApi.getStock().getData();
        var message = Util.buildStockMessage(stockData);

        if (Util.isRare(message) && StringUtils.isNotBlank(message) && externalApi.checkExistingMessage(message)) {
            externalApi.sendMessageV2(message, users);
            externalApi.resetEttemps(Constant.Keys.STOCK_ETTEMPS);
        } else {
            externalApi.countEttemps(Constant.Keys.STOCK_ETTEMPS);
            log.info("Nothing is rare");
        }

        log.info("End of scheduler");
    }

    @Scheduled(fixedRate = 15000)
    public void updateWeather() {
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
                externalApi.resetEttemps(Constant.Keys.WEATHER_ETTEMPS);
            } else {
                externalApi.countEttemps(Constant.Keys.WEATHER_ETTEMPS);
            }
        } else {
            log.info("No active weather");
        }
    }

}
