package com.slockey.dreamlightlegend.gameobjects.map;

import java.io.File;
import java.util.ArrayList;

import com.fasterxml.jackson.core.type.TypeReference;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RoomGenerator {

    private static final String ROOM_DESCRIPTIONS = "/room_descriptions.json";
    private static final String CORRIDOR_DESCRIPTIONS = "/corridor_descriptions.json";

    private final ObjectMapper mapper = new ObjectMapper();

    private ArrayList<String> roomDescriptions = null;
    private ArrayList<String> corridorDescriptions = null;

    public RoomGenerator() {
        init();
    }

    public Room generateRoom(Room parentRoom, Direction parentDirection) {

        int numberOfExits = getNumberOfExits();

        String roomName = "Room...";
        String roomDescription = getRandomRoomDescription();
        if (numberOfExits == 2) {
            // this may be a corridor - for now we'll say it is
            roomName = "Corridor...";
            roomDescription = getRandomCorridorDescription();
        }

        ArrayList<Direction> availableExitDirections = new ArrayList<>();
        availableExitDirections.add(Direction.NORTH);
        availableExitDirections.add(Direction.EAST);
        availableExitDirections.add(Direction.SOUTH);
        availableExitDirections.add(Direction.WEST);

        Room generatedRoom = new Room(roomName, roomDescription);
        // determine the exits - start with the parent
        Exit parentExit = parentRoom.getExit(parentDirection);
        parentExit.addLinkedRoomId(generatedRoom.getId());
        generatedRoom.putExit(Direction.getOppositeDirection(parentDirection), parentExit);
        availableExitDirections.remove(Direction.getOppositeDirection(parentDirection));

        // if more than 1 exit then flag the remainders for lazy loading
        // 60% is a door
        // 40% of doors are locked
        for (int idx = 1; idx < numberOfExits; idx++) {
            for (Direction dir : availableExitDirections) {
                if (generatedRoom.getExit(dir).getExitState().equals(ExitState.NONE)) {
                    Exit anotherExit = new Exit(generatedRoom.getId());
                    // determine if this is a locked door, door or passage
                    int exitTypePercentile = getPercentile();
                    if (exitTypePercentile <= 20) {
                        // locked door
                        anotherExit.setExitState(ExitState.LOCKED);
                        anotherExit.setDescription("This is a locked door");
                        generatedRoom.putExit(dir, anotherExit);
                        availableExitDirections.remove(dir);
                        break;
                    } else if (exitTypePercentile <= 60) {
                        // door
                        anotherExit.setExitState(ExitState.CLOSED);
                        anotherExit.setDescription("This is a closed door");
                        generatedRoom.putExit(dir, anotherExit);
                        availableExitDirections.remove(dir);
                        break;
                    } else {
                        // passage
                        anotherExit.setExitState(ExitState.OPEN);
                        anotherExit.setDescription("This passage leads off into the dark.");
                        generatedRoom.putExit(dir, anotherExit);
                        availableExitDirections.remove(dir);
                        break;
                    }
                }
            }
        }
        return generatedRoom;
    }

    /*
        The starting room:
        + Always a ROOM
        + Always has 4 exits - this guarantees there will be at least 5 rooms total
        + Always shows the dungeon exit UP that is blocked
     */
    public Room generateStartingRoom() {
        String startingRoomName = "Starting Room";
        String startingRoomDescription = "This is a large chamber barely illuminated by a single beam of sunlight from a hole 30 meters above you. A stinking pile of refuse is in the center of the floor.";

        // create the starting room
        Room startingRoom = new Room(startingRoomName, startingRoomDescription);
        // the starting room has passages on 4 cardinal directions
        startingRoom.putExit(Direction.NORTH, generatePassageExit(startingRoom.getId()));
        startingRoom.putExit(Direction.EAST, generatePassageExit(startingRoom.getId()));
        startingRoom.putExit(Direction.SOUTH, generatePassageExit(startingRoom.getId()));
        startingRoom.putExit(Direction.WEST, generatePassageExit(startingRoom.getId()));
        // the starting room has a dungeon exit that is currently blocked
        Exit theExit = new Exit();
        theExit.addLinkedRoomId(startingRoom.getId());
        theExit.setExitState(ExitState.BLOCKED);
        theExit.setDescription("A natural rock chimney leads up to a point of light. If only you could climb up.");
        startingRoom.putExit(Direction.UP, theExit);
        return startingRoom;
    }

    public Exit generatePassageExit(String linkedRoomId) {
        Exit theExit = new Exit();
        // apply starting passage data
        theExit.setExitState(ExitState.OPEN);
        theExit.setDescription("This passage leads off into the dark.");
        // store the parent room id
        theExit.addLinkedRoomId(linkedRoomId);
        return theExit;
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
