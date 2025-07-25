package grow.a.garden.service.impl;

import grow.a.garden.dto.response.base.BaseResponse;
import grow.a.garden.dto.response.user.UsersResponse;
import grow.a.garden.entity.UsersEntity;
import grow.a.garden.repository.ExternalApi;
import grow.a.garden.repository.UserRepository;
import grow.a.garden.repository.jpa.UsersJpaRepository;
import grow.a.garden.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final ExternalApi externalApi;

    private final UserRepository userRepository;

    private final UsersJpaRepository usersJpaRepository;

    public UserServiceImpl(ExternalApi externalApi, UserRepository userRepository, UsersJpaRepository usersJpaRepository) {
        this.externalApi = externalApi;
        this.userRepository = userRepository;
        this.usersJpaRepository = usersJpaRepository;
    }

    @Override
    public BaseResponse<Object> getUsers() {
        BaseResponse baseResponse;

        try {
            var response = externalApi.getUsers();

            log.info(response.toString());

            baseResponse = BaseResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("Success")
                    .data(response)
                    .build();

        } catch (Exception e) {
            baseResponse = BaseResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error when get users: " + e.getMessage())
                    .build();
        }

        return baseResponse;
    }

    @Override
    public BaseResponse<Object> syncUsers() {
        BaseResponse baseResponse;

        try {
            var response = externalApi.getUsers();

            if (response == null) {
                return BaseResponse.builder()
                        .status(HttpStatus.NO_CONTENT.value())
                        .message("No users found")
                        .build();
            }

            userRepository.saveUser(response);
            userRepository.storeUser(UsersResponse.fromUsersList(response.getResult()));

            var userFromResponse = UsersResponse.usersEntityFromUserResponse(response.getResult());
            var usersFromDb = usersJpaRepository.findAll();

            var allUsers = Stream.concat(usersFromDb.stream(), userFromResponse.stream())
                    .collect(Collectors.toMap(
                            UsersEntity::getUserId,
                            Function.identity(),
                            (existing, replacement) -> existing
                    ))
                    .values()
                    .stream()
                    .toList();

            baseResponse = BaseResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("Success save users")
                    .data(allUsers)
                    .build();
        } catch (Exception e) {
            baseResponse = BaseResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error when get users: " + e.getMessage())
                    .build();
        }

        return baseResponse;
    }
}

