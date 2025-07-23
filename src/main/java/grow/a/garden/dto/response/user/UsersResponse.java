package grow.a.garden.dto.response.user;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

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
}
