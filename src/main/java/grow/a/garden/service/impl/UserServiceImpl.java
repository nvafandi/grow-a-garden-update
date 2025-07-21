package grow.a.garden.service.impl;

import grow.a.garden.dto.response.base.BaseResponse;
import grow.a.garden.repository.External;
import grow.a.garden.repository.UserRepository;
import grow.a.garden.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final External external;

    private final UserRepository userRepository;

    public UserServiceImpl(External external, UserRepository userRepository) {
        this.external = external;
        this.userRepository = userRepository;
    }

    @Override
    public BaseResponse<Object> getUsers() {
        BaseResponse baseResponse;

        try {
            var response = external.getUsers();

            var user = userRepository.getListUser();

            log.info("user: {}", user);

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
}

