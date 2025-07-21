package grow.a.garden.service;

import grow.a.garden.dto.response.base.BaseResponse;

public interface StockService {

    BaseResponse<Object> getUpdate();

    BaseResponse<Object> sendMessage();

}
