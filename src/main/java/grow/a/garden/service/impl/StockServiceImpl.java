package grow.a.garden.service.impl;

import grow.a.garden.constant.Constant;
import grow.a.garden.dto.response.base.BaseResponse;
import grow.a.garden.repository.External;
import grow.a.garden.service.StockService;
import grow.a.garden.util.Util;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StockServiceImpl implements StockService {

    private final External external;

    public StockServiceImpl(External external) {
        this.external = external;
    }

    @Override
    public BaseResponse getUpdate() {
        BaseResponse baseResponse;

        try {
            var response = external.getStock();

            baseResponse = BaseResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message(Constant.SUCCESS_GET_STOCK)
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
    public BaseResponse<Object> sendMessage() {
        BaseResponse baseResponse;

        var stock = external.getStock();

        var message = Util.buildMessage(stock.getData());

        try {
            if (StringUtils.isNotBlank(message)) {
                external.sendMessage(message);
            } else {
                return BaseResponse.builder()
                        .status(HttpStatus.NO_CONTENT.value())
                        .message(Constant.MESSAGE_ALREADY_SENT)
                        .build();
            }

            baseResponse = BaseResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message(Constant.SUCCESS_SEND_MESSAGE)
                    .build();
        } catch (Exception e) {
            baseResponse = BaseResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error when send message : " + e.getMessage())
                    .build();
        }

        return baseResponse;
    }

    @Scheduled(fixedRate = 15000) // setiap 15 detik
    private void updatedStock() {
        log.info("scheduler running");

        var stock = external.getStock();

        var message = Util.buildMessage(stock.getData());

        log.warn("message : {}", message);

        if (Util.isRare(message) && StringUtils.isNotBlank(message)) {
            external.sendMessage(message);
        }
        log.info("End of scheduler");
    }

}
