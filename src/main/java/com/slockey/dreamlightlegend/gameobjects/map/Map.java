package com.slockey.dreamlightlegend.gameobjects.map;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class Map {

    private RoomGenerator roomGenerator = new RoomGenerator();
    private LinkedHashMap<String,Room> rooms = new LinkedHashMap<>();

    public Room getRoom(String roomId) {
        return rooms.get(roomId);
    }

    public Room putRoom(String roomId, Room room) {
        return rooms.put(roomId, room);
    }

    public Room removeRoom(String roomId) {
        return rooms.remove(roomId);
    }

    public void removeAllRooms() {
        rooms.clear();
    }

    public Room getStartingRoom() {
        Entry<String,Room> firstEntry = rooms.firstEntry();
        return firstEntry.getValue();
    }

    public Room addNewRoom(Room currentRoom, Direction direction) {
        Room newRoom = roomGenerator.generateRoom(currentRoom, direction);
        putRoom(newRoom.getId(), newRoom);
        return newRoom;
    }

    // For now all this does is create the starting room
    public void init() {

        Room startingRoom = roomGenerator.generateStartingRoom();
        // for testing - add a passage north
        Exit northExit = roomGenerator.generatePassageExit(startingRoom.getId());
        startingRoom.putExit(Direction.NORTH, northExit);
        // store the starting room in the map
        rooms.put(startingRoom.getId(), startingRoom);
    }
}
