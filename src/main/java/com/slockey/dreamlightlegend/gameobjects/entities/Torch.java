package com.slockey.dreamlightlegend.gameobjects.entities;

import lombok.Data;

@Data 
public class Torch implements Item {

    String name = "Torch";
    String description = "A shaft of wood with strips of cloth balled up at one end. Cloth can be lit on fire to provide light.";
    boolean equipable = true;
    boolean consumable = true;
    boolean craftable = true;
    boolean container = false;

}
