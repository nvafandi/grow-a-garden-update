package grow.a.garden.repository;

import grow.a.garden.dto.response.user.Users;
import grow.a.garden.dto.response.user.UsersResponse;

import java.util.List;

public interface UserRepository {

    List<String> getListUser();

    List<String> getListUser(List<Users> userList);

    void storeUser(List<String> users);

    List<String> checkExistUser();

    void saveUser(UsersResponse usersResponse);
}
