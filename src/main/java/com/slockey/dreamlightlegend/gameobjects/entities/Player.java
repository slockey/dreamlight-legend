package com.slockey.dreamlightlegend.gameobjects.entities;

import java.util.ArrayList;

import com.slockey.dreamlightlegend.gameobjects.map.Room;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class Player extends Actor {

    private int toughness;
    private int aether;
    private int sanity;
    private int magicResistance;
    private int exhaustion;
    private int exhaustionResistance;

    public Player(String name, String description, int health, Room room) {
        super(name, description, health, room, new ArrayList<Item>());
    }

}
