package grow.a.garden.repository;

import java.util.List;

public interface UserRepository {

    List<String> getListUser();

    void storeUser(List<String> userList);

    List<String> checkExistUser();

}
