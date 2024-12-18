package com.daw.store.mapper;

import com.daw.store.dynamodb.entity.Item;
import com.daw.store.entity.ItemEntity;
import org.springframework.stereotype.Service;

@Service
public class ItemEntityMapper {
    public Item toItem(ItemEntity itemEntity) {
        Item item = new Item();
        item.setId(itemEntity.getId());
        item.setName(itemEntity.getName());
        item.setPrice(itemEntity.getPrice());
        item.setDescription(itemEntity.getDescription());
        item.setImage(itemEntity.getImage());
        item.setLevel(itemEntity.getLevel());
        item.setPoints(itemEntity.getPoints());
        return item;

    }
    public ItemEntity toItemEntity(Item item) {
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(item.getId());
        itemEntity.setName(item.getName());
        itemEntity.setPrice(item.getPrice());
        itemEntity.setDescription(item.getDescription());
        itemEntity.setImage(item.getImage());
        itemEntity.setLevel(item.getLevel());
        itemEntity.setPoints(item.getPoints());
        return itemEntity;
    }
}
