package grow.a.garden.repository;

import grow.a.garden.dto.response.items.ItemsReponse;

import java.util.List;

public interface ItemsRepository {

    void saveItems(List<ItemsReponse> items);

}
