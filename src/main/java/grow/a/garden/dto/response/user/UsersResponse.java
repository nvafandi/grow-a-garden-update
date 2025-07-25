package grow.a.garden.dto.response.user;

import grow.a.garden.entity.UsersEntity;
import lombok.Data;

import java.util.List;

@Data
public class UsersResponse {

    private boolean ok;

    private List<Users> result;

    public static List<String> fromUsersList(List<Users> users) {
        return users.stream()
                .map(user -> String.valueOf(user.getMessage().getFrom().getId()))
                .distinct()
                .toList();
    }

    public static List<UsersEntity> usersEntityFromUserResponse(List<Users> users) {
        return users.stream()
                .map(user -> UsersEntity.builder()
                        .userId(String.valueOf(user.getMessage().getFrom().getId()))
                        .name(user.getMessage().getFrom().getLastName())
                        .build())
                .toList();

    }
}
