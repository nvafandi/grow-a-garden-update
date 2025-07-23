package grow.a.garden.service.impl;

import grow.a.garden.constant.Constant;
import grow.a.garden.dto.response.base.BaseResponse;
import grow.a.garden.dto.response.user.UsersResponse;
import grow.a.garden.repository.ExternalApi;
import grow.a.garden.repository.UserRepository;
import grow.a.garden.repository.jpa.UsersJpaRepository;
import grow.a.garden.service.StockService;
import grow.a.garden.util.Util;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StockServiceImpl implements StockService {

    private final ExternalApi externalApi;

    private final UserRepository userRepository;

    private final UsersJpaRepository usersJpaRepository;

    public StockServiceImpl(ExternalApi externalApi, UserRepository userRepository, UsersJpaRepository usersJpaRepository) {
        this.externalApi = externalApi;
        this.userRepository = userRepository;
        this.usersJpaRepository = usersJpaRepository;
    }

    @Override
    public BaseResponse getUpdate() {
        BaseResponse baseResponse;

        try {
            var response = externalApi.getStock();

            baseResponse = BaseResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message(Constant.Message.SUCCESS_GET_STOCK)
                    .data(response.getData())
                    .build();
        } catch (Exception e) {
            baseResponse = BaseResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error when get stock : " + e.getMessage())
                    .build();
        }
        return baseResponse;
    }

    @Override
    public BaseResponse sendMessage() {
        BaseResponse baseResponse;

        var stock = externalApi.getStock();

        var message = Util.buildStockMessage(stock.getData());

        try {
            if (StringUtils.isNotBlank(message)) {
                externalApi.sendMessage(message);
            } else {
                return BaseResponse.builder()
                        .status(HttpStatus.NO_CONTENT.value())
                        .message(Constant.Message.MESSAGE_ALREADY_SENT)
                        .build();
            }

            baseResponse = BaseResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message(Constant.Message.SUCCESS_SEND_MESSAGE)
                    .build();
        } catch (Exception e) {
            baseResponse = BaseResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error when send message : " + e.getMessage())
                    .build();
        }

        return baseResponse;
    }

    @Scheduled(fixedRate = 15000) // setiap 15 detik
    private void updatedStock() {
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
        } else {
            log.info("Nothing is rare");
        }

        log.info("End of scheduler");
    }

}
