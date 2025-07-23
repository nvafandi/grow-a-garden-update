package grow.a.garden.service;

import grow.a.garden.dto.response.base.BaseResponse;

public interface UserService {

    BaseResponse<Object> getUsers();

    BaseResponse<Object> syncUsers();

}
