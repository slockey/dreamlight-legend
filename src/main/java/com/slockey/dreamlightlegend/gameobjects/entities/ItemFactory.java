package com.slockey.dreamlightlegend.gameobjects.entities;

public class ItemFactory {

    public static Dagger getDaggerInstance() {
        return new Dagger();
    }

    public static Torch getTorchInstance() {
        return new Torch();
    }
}
