package com.slockey.dreamlightlegend.gameobjects.map;

import com.slockey.dreamlightlegend.engine.Direction;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Room {

    private int id;
    private String name, description;
    private int north, east, south, west;

    public int getDirection(Direction direction) {
        int directionOrdinal = -1;
        switch (direction) {
            case NORTH:
                directionOrdinal = north;
                break;
            case EAST:
                directionOrdinal = east;
                break;
            case SOUTH:
                directionOrdinal = south;
                break;
            case WEST:
                directionOrdinal = west;
                break;
            default:
                break;
        }
        return directionOrdinal;
    }

    public String getDisplayString() {
        boolean isExit = (north == -2 || north >= 0 
            || east == -2 || east >= 0 
            || south == -2 || south >= 0 
            || west == -2 || west >= 0);

        StringBuilder builder = new StringBuilder();
        builder.append(name);
        builder.append("\n");
        builder.append(description);
        builder.append("\n");
        if ( isExit ) {
            builder.append("There are exits: ");
            if (north == -2 || north >= 0) builder.append("  North");
            if (east == -2 || east >= 0) builder.append("  East");
            if (south == -2 || south >= 0) builder.append("  South");
            if (west == -2 || west >= 0) builder.append("  West");
        } else {
            builder.append("There are no exits");
        }
        return builder.toString();
    }
}
