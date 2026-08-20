package com.slockey.dreamlightlegend.engine;

public enum Direction {
    NORTH, 
    EAST, 
    SOUTH, 
    WEST,
    UP,
    DOWN;   
    public static final int NOEXIT = -1;
    public static final int GENEXIT = -2;
    public static final int DOOR = -3;
    public static final int LOCKED_DOOR = -4;
    public static final int BARRICADE = -5;

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
                return null;
        }
    }
}
