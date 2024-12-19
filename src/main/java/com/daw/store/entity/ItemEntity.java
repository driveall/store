package com.daw.store.entity;

import com.daw.store.enums.ItemType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemEntity {
    private String id;
    private String name;
    private Integer points;
    private String description;
    private String image;
    private Integer level;
    private Integer price;
    private ItemType type;

    public String toJson(ObjectMapper mapper) {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
    public static ItemEntity fromJson(String json, ObjectMapper mapper) {
        try {
            return mapper.readValue(json, ItemEntity.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
