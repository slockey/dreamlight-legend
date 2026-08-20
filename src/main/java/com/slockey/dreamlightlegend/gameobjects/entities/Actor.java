package com.slockey.dreamlightlegend.gameobjects.entities;

import java.util.ArrayList;

import com.slockey.dreamlightlegend.gameobjects.map.Room;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Actor {

    private String name, description;
    private Room room;
    private ArrayList<Item> inventory;

    public String displayInventory() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(name);
        buffer.append(" has:\n");
        for (Item item : inventory) {
            buffer.append(item.getName());
            buffer.append("\n");
        }
        return buffer.toString();
    }
}
