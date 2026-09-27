package com.slockey.dreamlightlegend.gameobjects.entities;

import com.slockey.dreamlightlegend.engine.NumberGenerator;

public class ItemFactory {

    public static Dagger getDaggerInstance() {
        return new Dagger();
    }

    public static Torch getTorchInstance() {
        return new Torch();
    }

    public static Chest getChestInstance() {
        Chest chest = new Chest();

        // chest is locked 50% of the time
        int locked = NumberGenerator.getPercentile();
        if (locked >= 51) {
            chest.setLocked(true);
        } else {
            chest.setLocked(false);
        }

        // chest is trapped 40% of the time
        int trapped = NumberGenerator.getPercentile();
        if (trapped >= 61) {
            chest.setTrapped(true);
        } else {
            chest.setTrapped(false);
        }

        // determine random contents
        // for now it's just a dagger
        chest.getInventory().addItem(new Dagger());

        return chest;
    }

}
