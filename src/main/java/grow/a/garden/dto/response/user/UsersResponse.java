package grow.a.garden.dto.response.user;

import lombok.Data;

import java.util.List;

@Data
public class UsersResponse {

    private boolean ok;

    private List<Users> result;

}
