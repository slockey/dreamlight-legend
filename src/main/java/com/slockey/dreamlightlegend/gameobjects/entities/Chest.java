package com.slockey.dreamlightlegend.gameobjects.entities;

import java.util.ArrayList;

import lombok.Data;

@Data 
public class Chest implements Item {

    Inventory inventory = new Inventory();

    String name = "Chest";
    String description = "A sturdy wooden box with iron hinges. It looks like there is a hole for a key on the front.";
    boolean equipable = true;
    boolean consumable = true;
    boolean craftable = true;
    boolean container = false;

    boolean closed = true;
    boolean locked = true;
    boolean trapped = true;

    @Override 
    public String getDescription() {
        StringBuilder builder = new StringBuilder();
        builder.append(description);
        builder.append("\n");
        builder.append("The chest appears to be ");

        if (closed) {
            if (locked) {
                builder.append("closed and locked.");
            } else {
                builder.append("closed and unlocked.");
            }
        } else {
            builder.append("open. Look inside to see what fate has gifted you.");
        }

        return builder.toString();
    }
}
