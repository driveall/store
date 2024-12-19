package com.daw.store.mapper;

import com.daw.store.dynamodb.entity.Account;
import com.daw.store.entity.ItemEntity;
import com.daw.store.enums.ItemType;
import org.springframework.stereotype.Service;

@Service
public class ItemEntityMapper {
    public Account toItem(ItemEntity itemEntity) {
        return Account.builder()
                .id(itemEntity.getId())
                .name(itemEntity.getName())
                .price(itemEntity.getPrice())
                .description(itemEntity.getDescription())
                .image(itemEntity.getImage())
                .level(itemEntity.getLevel())
                .points(itemEntity.getPoints())
                .type(itemEntity.getType().name())
                .build();
    }

    public ItemEntity toItemEntity(Account item) {
        return ItemEntity.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .description(item.getDescription())
                .image(item.getImage())
                .level(item.getLevel())
                .points(item.getPoints())
                .type(ItemType.valueOf(item.getType()))
                .build();
    }
}
