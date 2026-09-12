package com.slockey.dreamlightlegend.gameobjects.entities;

import java.util.ArrayList;

import com.slockey.dreamlightlegend.gameobjects.map.Room;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Actor {

    private String name, description;
    private int health;
    private Room room;
    private ArrayList<Item> inventory;

    public String displayInventoryItemByName(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item.getDescription();
            }
        }
        return "You don't seem to have a " + itemName;
    }

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

    public String displaySelf() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Name: ");
        buffer.append(name);
        buffer.append("\n\n");

        buffer.append("Attributes\n");
        buffer.append("Health: ");
        buffer.append(health);
        buffer.append("\n\n");

        buffer.append("Description:\n");
        buffer.append(description);
        buffer.append("\n\n");

        buffer.append("Inventory:\n");
        for (Item item : inventory) {
            buffer.append(item.getName());
            buffer.append("\n");
        }

        return buffer.toString();
    }

}
