package grow.a.garden.repository.impl;

import grow.a.garden.dto.response.items.ItemsReponse;
import grow.a.garden.entity.ItemsEntity;
import grow.a.garden.repository.ItemsRepository;
import grow.a.garden.repository.jpa.ItemsJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Slf4j
public class ItemsRepositoryImpl implements ItemsRepository {

    private final ItemsJpaRepository itemsJpaRepository;

    public ItemsRepositoryImpl(ItemsJpaRepository itemsJpaRepository) {
        this.itemsJpaRepository = itemsJpaRepository;
    }

    @Override
    public void saveItems(List<ItemsReponse> items) {
        List<ItemsEntity> itemsEntities = items.stream()
                .map(item -> ItemsEntity.builder()
                        .itemId(item.getItemId())
                        .displayName(item.getDisplayName())
                        .rarity(item.getRarity())
                        .price(item.getPrice())
                        .currency(item.getCurrency())
                        .description(item.getDescription())
                        .duration(item.getDuration())
                        .type(item.getType())
                        .createdAt(new Date())
                        .build()
                ).toList();

        itemsJpaRepository.saveAll(itemsEntities);

        log.info("Save items to database");
    }

}
