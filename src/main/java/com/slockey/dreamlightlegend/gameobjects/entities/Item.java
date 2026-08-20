package com.slockey.dreamlightlegend.gameobjects.entities;

public interface Item {

    String getName();
    String getDescription();
    boolean isEquipable();
    boolean isConsumable();
    boolean isCraftable();
}
