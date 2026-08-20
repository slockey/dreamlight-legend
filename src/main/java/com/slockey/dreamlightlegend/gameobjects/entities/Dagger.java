package com.slockey.dreamlightlegend.gameobjects.entities;

import lombok.Data;

@Data
public class Dagger implements Item {

    String name = "Dagger";
    String description = "It's got a handle, sharp edges and a pointy bit at the end. It looks dangerous.";
    boolean equipable = true;
    boolean consumable = false;
    boolean craftable = false;

}
