package grow.a.garden.repository.impl;

import grow.a.garden.dto.response.items.ItemsReponse;
import grow.a.garden.entity.ItemsEntity;
import grow.a.garden.entity.WishEntity;
import grow.a.garden.repository.ItemsRepository;
import grow.a.garden.repository.jpa.ItemsJpaRepository;
import grow.a.garden.repository.jpa.WishJpaRespository;
import grow.a.garden.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@Slf4j
public class ItemsRepositoryImpl implements ItemsRepository {

    private final ItemsJpaRepository itemsJpaRepository;

    private final WishJpaRespository wishJpaRespository;

    public ItemsRepositoryImpl(ItemsJpaRepository itemsJpaRepository, WishJpaRespository wishJpaRespository) {
        this.itemsJpaRepository = itemsJpaRepository;
        this.wishJpaRespository = wishJpaRespository;
    }

    @Override
    public void saveItems(List<ItemsReponse> items) {
        if (items == null || items.isEmpty()) {
            log.info("No items to save");
            return;
        }

        List<String> itemIds = items.stream()
                .map(ItemsReponse::getItemId)
                .toList();

        List<ItemsEntity> existingList = itemsJpaRepository.findAllByItemIdIn(itemIds);

        if (existingList.isEmpty()) {
            // if table empty, save new data
            Date now = new Date();
            List<ItemsEntity> newEntities = items.stream()
                    .map(item -> ItemsEntity.builder()
                            .itemId(item.getItemId())
                            .displayName(item.getDisplayName())
                            .rarity(item.getRarity())
                            .price(item.getPrice())
                            .currency(item.getCurrency())
                            .description(item.getDescription())
                            .duration(item.getDuration())
                            .type(item.getType())
                            .createdAt(now)
                            .updatedAt(null)
                            .build())
                    .toList();

            itemsJpaRepository.saveAll(newEntities);
            log.info("Table empty, saved all {} new items", newEntities.size());
            return;
        }

        //if any existing
        Map<String, ItemsEntity> existingMap = existingList.stream()
                .collect(Collectors.toMap(ItemsEntity::getItemId, Function.identity()));

        Date now = new Date();

        List<ItemsEntity> itemsToSaveOrUpdate = items.stream()
                .map(item -> {
                    ItemsEntity existing = existingMap.get(item.getItemId());

                    if (existing == null) {
                        return ItemsEntity.builder()
                                .itemId(item.getItemId())
                                .displayName(item.getDisplayName())
                                .rarity(item.getRarity())
                                .price(item.getPrice())
                                .currency(item.getCurrency())
                                .description(item.getDescription())
                                .duration(item.getDuration())
                                .type(item.getType())
                                .createdAt(now)
                                .build();
                    } else {
                        boolean changed = Stream.of(
                                Util.updateIfChanged(existing.getDisplayName(), item.getDisplayName(), existing::setDisplayName),
                                Util.updateIfChanged(existing.getRarity(), item.getRarity(), existing::setRarity),
                                Util.updateIfChanged(existing.getPrice(), item.getPrice(), existing::setPrice),
                                Util.updateIfChanged(existing.getCurrency(), item.getCurrency(), existing::setCurrency),
                                Util.updateIfChanged(existing.getDescription(), item.getDescription(), existing::setDescription),
                                Util.updateIfChanged(existing.getDuration(), item.getDuration(), existing::setDuration),
                                Util.updateIfChanged(existing.getType(), item.getType(), existing::setType)
                        ).anyMatch(changedFlag -> changedFlag);

                        if (changed) {
                            existing.setCreatedAt(now);
                            return existing;
                        } else {
                            return null; // no changes, filter out later
                        }
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        if (!itemsToSaveOrUpdate.isEmpty()) {
            itemsJpaRepository.saveAll(itemsToSaveOrUpdate);
            log.info("Saved/updated {} item(s) to database", itemsToSaveOrUpdate.size());
        } else {
            log.info("No changes detected; no save/update performed");
        }
    }

    @Override
    public void saveWishes(String userId, List<ItemsEntity> itemsEntities) {
        if (itemsEntities == null || itemsEntities.isEmpty()) {
            log.info("No items found");
            return;
        }

        Date now = new Date();

        List<String> existingWishIds = wishJpaRespository.findByUserId(userId)
                .stream()
                .map(WishEntity::getWishId)
                .toList();

        List<WishEntity> wishEntitiesToSave = itemsEntities.stream()
                .filter(item -> !existingWishIds.contains(item.getItemId()))
                .map(itemsEntity -> WishEntity.builder()
                        .wishId(itemsEntity.getItemId())
                        .displayName(itemsEntity.getDisplayName())
                        .type(itemsEntity.getType())
                        .userId(userId)
                        .createdAt(now)
                        .build()
                )
                .toList();

        if (!wishEntitiesToSave.isEmpty()) {
            wishJpaRespository.saveAll(wishEntitiesToSave);
            log.info("Saved new wishes to database: {}", wishEntitiesToSave.size());
        } else {
            log.info("No new wishes to save.");
        }
    }

}
