package com.slockey.dreamlightlegend.gameobjects.map;

import java.util.ArrayList;
import java.util.HashMap;

import com.slockey.dreamlightlegend.gameobjects.entities.Inventory;
import com.slockey.dreamlightlegend.gameobjects.entities.Item;

public class Room {

    private String id, name, description;
    private HashMap<String,Exit> exitHashMap;

    private boolean scavenged = false;
    private Inventory inventory;

    public Room() {
        this.id = IdGenerator.getRandomIdString();
        this.name = "default_room_name";
        this.description = "default_room_description";
        this.exitHashMap = new HashMap<>();
        this.exitHashMap.put(Direction.NORTH.name(), new Exit(this.id));
        this.exitHashMap.put(Direction.SOUTH.name(), new Exit(this.id));
        this.exitHashMap.put(Direction.EAST.name(), new Exit(this.id));
        this.exitHashMap.put(Direction.WEST.name(), new Exit(this.id));
        this.exitHashMap.put(Direction.UP.name(), new Exit(this.id));
        this.exitHashMap.put(Direction.DOWN.name(), new Exit(this.id));

        this.inventory = new Inventory();
    }

    public Room(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Exit getExit(Direction direction) {
        return exitHashMap.get(direction.name());
    }

    public Exit putExit(Direction direction, Exit exit) {
        return this.exitHashMap.replace(direction.name(), exit);
    }

    public String getDescription() {
        return description;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void scavenge() {
        if (!scavenged) {
            // TODO: find some random junk and materials
            scavenged = true;
        }
    }

    public String getDisplayString() {

        StringBuilder builder = new StringBuilder();
        builder.append(name);
        builder.append("\n");
        builder.append(description);
        builder.append("\n\n");

        // list items if there are any
        if (inventory.getSize() > 0) {
            builder.append("There are items: \n");
            for (Item item : inventory.getAllItems()) {
                builder.append(item.getName());
                builder.append("\n");
            }
            builder.append("\n\n");
        }

        builder.append("There are exits: \n");

        builder.append(displayExit(Direction.NORTH));
        builder.append(displayExit(Direction.EAST));
        builder.append(displayExit(Direction.SOUTH));
        builder.append(displayExit(Direction.WEST));
        builder.append(displayExit(Direction.UP));
        builder.append(displayExit(Direction.DOWN));

        return builder.toString();
    }

    private String displayExit(Direction direction) {
        String display = "";
        Exit exit = this.exitHashMap.get(direction.name());
        // this comparison is balls
        if (exit.getExitState() != ExitState.NONE
            && exit.getExitState() != ExitState.HIDDEN) {
            display += "There is an exit ";
            display += direction.toString().toLowerCase();
            display += ". ";
            display += exit.getDescriptionString();
            display += "\n";
        }
        return display;
    }
}
