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

        StringBuilder builder = new StringBuilder();
        builder.append(name);
        builder.append("\n");
        builder.append(description);
        builder.append("\n");

        builder.append("There are exits: \n");

        if (north <= -2 || north >= 0) {
            builder.append("There is a ");
            builder.append(displayExit(north));
            builder.append("  to the North\n");
        }

        if (east <= -2 || east >= 0) {
            builder.append("There is a ");
            builder.append(displayExit(east));
            builder.append("  to the East\n");
        }

        if (south <= -2 || south >= 0) {
            builder.append("There is a ");
            builder.append(displayExit(south));
            builder.append("  to the South\n");
        }

        if (west <= -2 || west >= 0) {
            builder.append("There is a ");
            builder.append(displayExit(west));
            builder.append("  to the West\n");
        }

        return builder.toString();
    }

    private String displayExit(int direction) {
        String type = "";
        switch(direction) {
            case (Direction.BARRICADE):
                type = "barricade";
                break;
            case (Direction.DOOR):
                type = "door";
                break;
            case (Direction.LOCKED_DOOR):
                type = "locked door";
                break;
            default:
                type = "passage";
                break;
        }
        return type;

    }

}
