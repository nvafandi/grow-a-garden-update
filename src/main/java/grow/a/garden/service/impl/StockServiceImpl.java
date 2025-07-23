package grow.a.garden.service.impl;

import grow.a.garden.constant.Constant;
import grow.a.garden.dto.response.base.BaseResponse;
import grow.a.garden.repository.ExternalApi;
import grow.a.garden.service.StockService;
import grow.a.garden.util.Util;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StockServiceImpl implements StockService {

    private final ExternalApi externalApi;

    public StockServiceImpl(ExternalApi externalApi) {
        this.externalApi = externalApi;
    }

    @Override
    public BaseResponse getUpdate() {
        BaseResponse baseResponse;

        try {
            var response = externalApi.getStock();

            baseResponse = BaseResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message(Constant.Message.SUCCESS_GET_STOCK)
                    .data(response.getData())
                    .build();
        } catch (Exception e) {
            baseResponse = BaseResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error when get stock : " + e.getMessage())
                    .build();
        }
        return baseResponse;
    }

    @Override
    public BaseResponse sendMessage() {
        BaseResponse baseResponse;

        var stock = externalApi.getStock();

        var message = Util.buildStockMessage(stock.getData());

        try {
            if (StringUtils.isNotBlank(message)) {
                externalApi.sendMessage(message);
            } else {
                return BaseResponse.builder()
                        .status(HttpStatus.NO_CONTENT.value())
                        .message(Constant.Message.MESSAGE_ALREADY_SENT)
                        .build();
            }

            baseResponse = BaseResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message(Constant.Message.SUCCESS_SEND_MESSAGE)
                    .build();
        } catch (Exception e) {
            baseResponse = BaseResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error when send message : " + e.getMessage())
                    .build();
        }

        return baseResponse;
    }

}
