package com.slockey.dreamlightlegend.gameobjects.map;

public enum Direction {

    NONE,
    NORTH,
    EAST,
    SOUTH,
    WEST,
    UP,
    DOWN;

    // support the parser to convert typed cardinal direction into Direction
    public static Direction getDirection(String id) {
        switch (id) {
            case "north":
            case "n":
                return NORTH;
            case "east":
            case "e":
                return EAST;
            case "south":
            case "s":
                return SOUTH;
            case "west":
            case "w":
                return WEST;
            case "up":
            case "u":
                return UP;
            case "down":
            case "d":
                return DOWN;
            default:
                return NONE;
        }
    }

    // it's opposite day
    public static Direction getOppositeDirection(Direction direction) {
        if (Direction.NORTH == direction ) {
            return SOUTH;
        } else if (Direction.EAST == direction) {
            return WEST;
        } else if (Direction.SOUTH == direction) {
            return NORTH;
        } else if (Direction.WEST == direction) {
            return EAST;
        } else if (Direction.UP == direction) {
            return DOWN;
        } else if (Direction.DOWN == direction) {
            return UP;
        }
        return NONE;
    }
}
