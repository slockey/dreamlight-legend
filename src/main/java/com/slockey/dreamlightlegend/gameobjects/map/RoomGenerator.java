package com.slockey.dreamlightlegend.gameobjects.map;

import java.io.File;
import java.util.ArrayList;

import com.fasterxml.jackson.core.type.TypeReference;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slockey.dreamlightlegend.engine.Direction;

public class RoomGenerator {

    private static final String ROOM_DESCRIPTIONS = "/room_descriptions.json";
    private static final String CORRIDOR_DESCRIPTIONS = "/corridor_descriptions.json";

    private final ObjectMapper mapper = new ObjectMapper();

    private ArrayList<String> roomDescriptions = null;
    private ArrayList<String> corridorDescriptions = null;

    public RoomGenerator() {
        init();
    }

    public Room generateRoom(int id, Room parentRoom) {
        // determine how many exits there should be
        int numberOfExits = getNumberOfExits();

        String name = "Room " + id;
        String description = getRandomRoomDescription();
        if (numberOfExits == 2) {
            // this may be a corridor - for now we'll say it is
            name = "Corridor " + id;
            description = getRandomCorridorDescription();
        }

        // Determine the exits
        // exits: north, east, south, west
        int[] exits = { Direction.NOEXIT, 
                        Direction.NOEXIT, 
                        Direction.NOEXIT, 
                        Direction.NOEXIT };

        // determine exits - start with the parent
        if (parentRoom.getNorth() == id) {
            exits[2] = parentRoom.getId();
        } else if (parentRoom.getEast() == id) {
            exits[3] = parentRoom.getId();
        } else if (parentRoom.getSouth() == id) {
            exits[0] = parentRoom.getId();
        } else if (parentRoom.getWest() == id) {
            exits[1] = parentRoom.getId();
        }

        // if more than 1 exit then flag the remainders for lazy loading
        // 60% is a door
        // 40% of doors are locked
        for (int idx = 1; idx < numberOfExits; idx++) {
            for (int jdx = 0; jdx < exits.length; jdx++) {
                if (exits[jdx] == Direction.NOEXIT) {
                    // determine if this is a door or corridor
                    int exitTypePercentile = getPercentile();
                    if (exitTypePercentile <= 20) {
                        exits[jdx] = Direction.LOCKED_DOOR;
                        break;
                    } else if (exitTypePercentile <= 60) {
                        exits[jdx] = Direction.DOOR;
                        break;
                    } else {
                        // this is a corridor
                        exits[jdx] = Direction.GENEXIT;
                    }
                    break;
                }
            }
        }

        return new Room(id, name, description, exits[0], exits[1], exits[2], exits[3]);
    }

    /*
        The starting room:
        + Always ID 0
        + Always a ROOM
        + Always has 4 exits - this guarantees there will be at least 5 rooms total
     */
    public Room generateStartingRoom() {
        int id = 0;
        String name = "Room " + id;
        String description = """
                This is a large chamber barely illuminated by a single beam of sunlight from a hole 30 meters above you. A stinking pile of refuse is in the center of the floor.""";

        // determine exits - starting room always has 4 exits
        return new Room(id, name, description, 
            Direction.GENEXIT, 
            Direction.GENEXIT, 
            Direction.GENEXIT, 
            Direction.GENEXIT);
    }

    // 15% of rooms have 4 exits
    // 10% of rooms have 3 exits
    // 45% of rooms have 2 exits
    // 30% of rooms have 1 exit
    public int getNumberOfExits() {
        // generate random percentile to determine number of exits
        // minimum is 1 since there will be a linking room id
        int percentile = getPercentile();
        if (percentile <= 30) {
            return 1;
        } else if (percentile <= 75) {
            return 2;
        } else if (percentile <= 85) {
            return 3;
        } else {
            return 4;
        }
    }

    public String getRandomRoomDescription() {
        int limit = roomDescriptions.size();
        int index = (int)(Math.random() * limit);
        return roomDescriptions.get(index);
    }

    public String getRoomDescription(int id) {
        return roomDescriptions.get(id);
    }

    public String getRandomCorridorDescription() {
        int limit = corridorDescriptions.size();
        int index = (int)(Math.random() * limit);
        return corridorDescriptions.get(index);
    }

    public String getCorridorDescription(int id) {
        return corridorDescriptions.get(id);
    }

    private int getPercentile() {
        return (int)(Math.random() * 100) + 1;
    }

    private void init() {
        try {

            roomDescriptions = mapper.readValue(
                new File(this.getClass().getResource(ROOM_DESCRIPTIONS).toURI() ), 
                new TypeReference<ArrayList<String>>(){});

            corridorDescriptions = mapper.readValue(
                new File(this.getClass().getResource(CORRIDOR_DESCRIPTIONS).toURI() ), 
                new TypeReference<ArrayList<String>>(){});

            } catch (Exception e) {
                System.err.println(e.getMessage());
        }
    }
}
