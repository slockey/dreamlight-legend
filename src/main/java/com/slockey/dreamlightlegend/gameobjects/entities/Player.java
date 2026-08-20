package com.slockey.dreamlightlegend.gameobjects.entities;

import java.util.ArrayList;

import com.slockey.dreamlightlegend.gameobjects.map.Room;

public class Player extends Actor {

    public Player(String name, String description, Room room) {
        super(name, description, room, new ArrayList<Item>());
    }

}
