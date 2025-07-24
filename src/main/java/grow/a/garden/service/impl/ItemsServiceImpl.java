package grow.a.garden.service.impl;

import grow.a.garden.dto.response.base.BaseResponse;
import grow.a.garden.entity.ItemsEntity;
import grow.a.garden.entity.WishEntity;
import grow.a.garden.repository.ExternalApi;
import grow.a.garden.repository.ItemsRepository;
import grow.a.garden.repository.jpa.ItemsJpaRepository;
import grow.a.garden.repository.jpa.WishJpaRespository;
import grow.a.garden.service.ItemsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemsServiceImpl implements ItemsService {

    private final ExternalApi externalApi;

    private final ItemsRepository itemsRepository;

    private final ItemsJpaRepository itemsJpaRepository;

    private final WishJpaRespository wishJpaRespository;

    public ItemsServiceImpl(ExternalApi externalApi, ItemsRepository itemsRepository, ItemsJpaRepository itemsJpaRepository, WishJpaRespository wishJpaRespository) {
        this.externalApi = externalApi;
        this.itemsRepository = itemsRepository;
        this.itemsJpaRepository = itemsJpaRepository;
        this.wishJpaRespository = wishJpaRespository;
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

    @Override
    public BaseResponse<Object> updateWish(String userId, String itemId) {
        BaseResponse baseResponse;

        try {
            var itemList = List.of(itemId.split(","));
            
            var items = itemsJpaRepository.findAllByItemIdIn(itemList);

            if (items.isEmpty()) {
                return BaseResponse.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("Item not found")
                        .build();
            }

            itemsRepository.saveWishes(userId, items);

            baseResponse = BaseResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("Success update wish")
                    .build();
        } catch (Exception e) {
            baseResponse = BaseResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error when update wish : " + e.getMessage())
                    .build();
        }

        return baseResponse;
    }

}
