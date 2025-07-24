package grow.a.garden.repository.impl;

import grow.a.garden.dto.response.user.Users;
import grow.a.garden.dto.response.user.UsersResponse;
import grow.a.garden.entity.UsersEntity;
import grow.a.garden.repository.ExternalApi;
import grow.a.garden.repository.UserRepository;
import grow.a.garden.repository.jpa.UsersJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository {

    @Value("${redis.timeout}")
    private int duration;

    private final ExternalApi externalApi;

    private final UsersJpaRepository usersJpaRepository;

    private RedisTemplate<String, String> redisTemplate;

    public UserRepositoryImpl(UsersJpaRepository usersJpaRepository, RedisTemplate<String, String> redisTemplate, ExternalApi externalApi) {
        this.usersJpaRepository = usersJpaRepository;
        this.redisTemplate = redisTemplate;
        this.externalApi = externalApi;
    }

    @Override
    public List<String> getListUser() {
        var userList = externalApi.getUsers();

        return userList.getResult().stream()
                .filter(user -> user.getMessage() != null) // prevent error null
                .map(user -> String.valueOf(user.getMessage().getFrom().getId()))
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getListUser(List<Users> userList) {
        return userList.stream()
                .filter(user -> user.getMessage() != null) // prevent error null
                .map(user -> String.valueOf(user.getMessage().getFrom().getId()))
                .distinct()
                .collect(Collectors.toList());

    }

    @Override
    public void storeUser(List<String> users) {
        String key = "|USER|";

        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();

        String value = String.join(",", users);

        valueOps.set(key, value, Duration.ofHours(duration));

        log.info("Store user to redis");
    }

    @Override
    public List<String> checkExistUser() {
        String key = "|USER|";

        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();

        String value = valueOps.get(key);

        if (value != null && !value.isEmpty()) {
            return List.of(value.split(","));
        }

        return List.of();
    }

    @Override
    public void saveUser(UsersResponse usersResponse) {
        if (usersResponse == null || usersResponse.getResult().isEmpty()) {
            return;
        }

        List<String> incomingUserIds = usersResponse.getResult().stream()
                .map(users -> String.valueOf(users.getMessage().getFrom().getId()))
                .distinct()
                .toList();

        List<String> existingUserIds = usersJpaRepository.findAllByUserIdIn(incomingUserIds)
                .stream()
                .map(UsersEntity::getUserId)
                .toList();

        List<UsersEntity> entitiesToSave = usersResponse.getResult().stream()
                .filter(users -> !existingUserIds.contains(String.valueOf(users.getMessage().getFrom().getId())))
                .map(users -> UsersEntity.builder()
                        .userId(String.valueOf(users.getMessage().getFrom().getId()))
                        .name(users.getMessage().getFrom().getLastName())
                        .createdAt(new Date())
                        .build())
                .distinct()
                .toList();

        if (!entitiesToSave.isEmpty()) {
            usersJpaRepository.saveAll(entitiesToSave);
            log.info("Saved new users to database: {}", entitiesToSave.size());
        } else {
            log.info("No new users to save.");
        }

    }
}
