package grow.a.garden.service.impl;

import grow.a.garden.dto.response.base.BaseResponse;
import grow.a.garden.repository.ExternalApi;
import grow.a.garden.repository.ItemsRepository;
import grow.a.garden.repository.jpa.ItemsJpaRepository;
import grow.a.garden.service.ItemsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ItemsServiceImpl implements ItemsService {

    private final ExternalApi externalApi;

    private final ItemsRepository itemsRepository;
    private final ItemsJpaRepository itemsJpaRepository;

    public ItemsServiceImpl(ExternalApi externalApi, ItemsRepository itemsRepository, ItemsJpaRepository itemsJpaRepository) {
        this.externalApi = externalApi;
        this.itemsRepository = itemsRepository;
        this.itemsJpaRepository = itemsJpaRepository;
    }

    @Override
    public BaseResponse<Object> getAllItems() {
        BaseResponse baseResponse;
        try {
            var items = itemsJpaRepository.findAll();

            if (items == null) {
                return BaseResponse.builder()
                        .status(HttpStatus.NO_CONTENT.value())
                        .message("Failed to get all items")
                        .build();
            }

            baseResponse = BaseResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("Success get items")
                    .data(items)
                    .build();
        } catch (Exception e) {
            baseResponse = BaseResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error when get all items : " + e.getMessage())
                    .build();
        }

        return baseResponse;
    }

    @Override
    public BaseResponse<Object> syncItems() {
        BaseResponse baseResponse;
        try {
            var items = externalApi.getItems();

            if (items == null) {
                return BaseResponse.builder()
                        .status(HttpStatus.NO_CONTENT.value())
                        .message("Failed to get all items")
                        .build();
            }

            itemsRepository.saveItems(items);

            baseResponse = BaseResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("Success save items")
                    .build();
        } catch (Exception e) {
            baseResponse = BaseResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error when save items : " + e.getMessage())
                    .build();
        }

        return baseResponse;
    }

    @Override
    public BaseResponse<Object> updateItemIcon(String itemId, String icon) {
        BaseResponse baseResponse;
        try {
            var item = itemsJpaRepository.findByItemId(itemId);

            if (item == null) {
                return BaseResponse.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("Item not found")
                        .build();
            }

            item.setIcon(icon);

            itemsJpaRepository.save(item);

            baseResponse = BaseResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("Success update item icon")
                    .build();
        } catch (Exception e) {
            baseResponse = BaseResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error when update icon : " + e.getMessage())
                    .build();
        }

        return baseResponse;
    }
}
