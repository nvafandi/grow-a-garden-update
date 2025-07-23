package grow.a.garden.service;

import grow.a.garden.dto.response.base.BaseResponse;

public interface ItemsService {

    BaseResponse<Object> getAllItems();

    BaseResponse<Object> syncItems();

}
