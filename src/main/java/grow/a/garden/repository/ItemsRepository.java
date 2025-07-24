package grow.a.garden.repository;

import grow.a.garden.dto.response.items.ItemsReponse;
import grow.a.garden.entity.ItemsEntity;
import grow.a.garden.entity.WishEntity;

import java.util.List;

public interface ItemsRepository {

    void saveItems(List<ItemsReponse> items);

    void saveWishes(String userId, List<ItemsEntity> itemsEntities);

}
