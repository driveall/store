package com.daw.store.mapper;

import com.daw.store.dynamodb.entity.Account;
import com.daw.store.entity.ItemEntity;
import com.daw.store.enums.ItemType;
import org.springframework.stereotype.Service;

@Service
public class ItemEntityMapper {
    public Account toItem(ItemEntity itemEntity) {
        Account item = new Account();
        item.setId(itemEntity.getId());
        item.setName(itemEntity.getName());
        item.setPrice(itemEntity.getPrice());
        item.setDescription(itemEntity.getDescription());
        item.setImage(itemEntity.getImage());
        item.setLevel(itemEntity.getLevel());
        item.setPoints(itemEntity.getPoints());
        item.setType(itemEntity.getType().name());
        return item;

    }
    public ItemEntity toItemEntity(Account item) {
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(item.getId());
        itemEntity.setName(item.getName());
        itemEntity.setPrice(item.getPrice());
        itemEntity.setDescription(item.getDescription());
        itemEntity.setImage(item.getImage());
        itemEntity.setLevel(item.getLevel());
        itemEntity.setPoints(item.getPoints());
        itemEntity.setType(ItemType.valueOf(item.getType()));
        return itemEntity;
    }
}
