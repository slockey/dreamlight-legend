package com.slockey.dreamlightlegend.gameobjects.entities;

import java.util.ArrayList;

public class Inventory {

    private ArrayList<Item> inventory = new ArrayList<>();

    public int getSize() {
        return inventory.size();
    }

    public void clearAllItems() {
        inventory.clear();
    }

    public ArrayList<Item> getAllItems() {
        return inventory;
    }

    public boolean addItem(Item item) {
        return inventory.add(item);
    }

    public boolean removeItem(Item item) {
        return inventory.remove(item);
    }

    public Item getItem(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }

    public boolean contains(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }

    public String displayContents() {
        StringBuilder builder = new StringBuilder();
        for (Item item : inventory) {
            builder.append(item.getName());
            builder.append("\n");
        }
        return builder.toString();
    }
}
