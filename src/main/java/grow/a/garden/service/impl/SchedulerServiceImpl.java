package grow.a.garden.service.impl;

import grow.a.garden.constant.Constant;
import grow.a.garden.dto.response.user.UsersResponse;
import grow.a.garden.entity.UsersEntity;
import grow.a.garden.repository.ExternalApi;
import grow.a.garden.repository.ItemsRepository;
import grow.a.garden.repository.UserRepository;
import grow.a.garden.repository.WeatherRepository;
import grow.a.garden.repository.jpa.UsersJpaRepository;
import grow.a.garden.repository.jpa.WishJpaRespository;
import grow.a.garden.util.Util;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Slf4j
public class SchedulerServiceImpl {

    private final ExternalApi externalApi;

    private final UserRepository userRepository;

    private final WeatherRepository weatherRepository;

    private final UsersJpaRepository usersJpaRepository;

    private final WishJpaRespository wishJpaRespository;

    private final ItemsRepository itemsRepository;

    public SchedulerServiceImpl(ExternalApi externalApi, UserRepository userRepository, WeatherRepository weatherRepository, UsersJpaRepository usersJpaRepository, WishJpaRespository wishJpaRespository, ItemsRepository itemsRepository) {
        this.externalApi = externalApi;
        this.userRepository = userRepository;
        this.weatherRepository = weatherRepository;
        this.usersJpaRepository = usersJpaRepository;
        this.wishJpaRespository = wishJpaRespository;
        this.itemsRepository = itemsRepository;
    }

    @Scheduled(fixedRate = 15000) // setiap 15 detik
    public void updatedStock() {
        var users = userRepository.checkExistUser();

        if (users.isEmpty()) {
            var userEntities = usersJpaRepository.findAll();

            if (userEntities.isEmpty()) {
                var getUsers = externalApi.getUsers();
                if (getUsers != null && getUsers.getResult() != null) {
                    userRepository.saveUser(getUsers);
                    userRepository.storeUser(UsersResponse.fromUsersList(getUsers.getResult()));
                }
            } else {
                var userList = userEntities.stream()
                        .map(UsersEntity::getUserId)
                        .distinct()
                        .toList();
                userRepository.storeUser(userList);
            }

            users = userRepository.checkExistUser();
            if (users.isEmpty()) {
                return;
            }
        }

        var stockData = externalApi.getStock().getData();
        var message = Util.buildStockMessage(stockData);

        if (StringUtils.isBlank(message)) {
            return;
        }

        users.forEach(user -> {
            var wish = wishJpaRespository.findByUserId(user);

            if (wish == null) {
                return;
            }

            var wishList = itemsRepository.wishList(wish);

            if (Util.isRare(wishList, message)) {
                if (externalApi.checkExistingMessage(message)) {
                    externalApi.sendMessage(message, user);
                    externalApi.resetEttemps(Constant.Keys.STOCK_ETTEMPS + user + "|");
                } else {
                    externalApi.countEttemps(Constant.Keys.STOCK_ETTEMPS + user + "|");
                }
                externalApi.resetEttemps(Constant.Keys.NO_RARE_ETTEMPS);
            } else {
                externalApi.countEttemps(Constant.Keys.NO_RARE_ETTEMPS);
            }
        });
    }

    @Scheduled(fixedRate = 15000)
    public void updateWeather() {
        var weather = weatherRepository.listWeather();

        if (Util.isActive(weather)) {
            var users = userRepository.checkExistUser();

            if (users.isEmpty()) {
                var userEntities = usersJpaRepository.findAll();

                if (userEntities.isEmpty()) {
                    var getUsers = externalApi.getUsers();
                    if (getUsers != null && getUsers.getResult() != null) {
                        userRepository.saveUser(getUsers);
                        userRepository.storeUser(UsersResponse.fromUsersList(getUsers.getResult()));
                    }
                } else {
                    var userList = userEntities.stream()
                            .map(UsersEntity::getUserId)
                            .distinct()
                            .toList();
                    userRepository.storeUser(userList);
                }

                users = userRepository.checkExistUser();
                if (users.isEmpty()) {
                    return;
                }
            }

            var message = Util.buildWeatherMessage(weather);

            if (StringUtils.isBlank(message)) {
                return;
            }

            users.forEach(user -> {
                if (externalApi.checkExistWeather(message)) {
                    externalApi.sendMessage(message, user);
                    externalApi.resetEttemps(Constant.Keys.WEATHER_ETTEMPS + user + "|");
                } else {
                    externalApi.countEttemps(Constant.Keys.WEATHER_ETTEMPS + user + "|");
                }
                externalApi.resetEttemps(Constant.Keys.WEATHER_ETTEMPS);
            });
        } else {
            externalApi.countEttemps(Constant.Keys.WEATHER_ETTEMPS);
        }
    }

}
