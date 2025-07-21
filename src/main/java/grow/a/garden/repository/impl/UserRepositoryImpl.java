package grow.a.garden.repository.impl;

import grow.a.garden.dto.response.user.Users;
import grow.a.garden.repository.External;
import grow.a.garden.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Value("${redis.timeout}")
    private int duration;

    private final External external;

    private RedisTemplate<String, String> redisTemplate;

    public UserRepositoryImpl(External external, RedisTemplate<String, String> redisTemplate) {
        this.external = external;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<String> getListUser() {
        var users = external.getUsers().getResult();

        return users.stream()
                .filter(user -> user.getMessage() != null) // prevent error null
                .map(user -> String.valueOf(user.getMessage().getFrom().getId()))
//                .filter(id -> !excludedSet.contains(id)) for next excluded if any
                .distinct()
                .collect(Collectors.toList());

    }

    @Override
    public void storeUser(List<String> userList) {
        String key = "|USER|";

        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();

        String value = String.join(",", userList);

        valueOps.set(key, value, Duration.ofHours(duration));
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
}
