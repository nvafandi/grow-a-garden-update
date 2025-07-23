package grow.a.garden.repository.jpa;

import grow.a.garden.entity.ItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ItemsJpaRepository extends JpaRepository<ItemsEntity, UUID> {

    List<ItemsEntity> findAllByItemIdIn(List<String> itemIds);

    ItemsEntity findByItemId(String itemId);

    List<ItemsEntity> findAllByType(String type);

}
