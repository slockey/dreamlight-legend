package com.slockey.dreamlightlegend.gameobjects.map;

import java.util.ArrayList;
import java.util.Iterator;

public class Map {
    private RoomGenerator roomGenerator = new RoomGenerator();
    private ArrayList<Room> rooms = new ArrayList<>();

    public Iterator<Room> getIterator() {
        return rooms.iterator();
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void init() {
        rooms.add(roomGenerator.generateStartingRoom());
        // rooms.add(new Room(1, "Forest", roomGenerator.getRandomDescription(), -1, -1, -1, 0));
        // rooms.add(new Room(2, "Cave", roomGenerator.getRandomDescription(), 0, 3, -1, -1));
        // rooms.add(new Room(3, "Dungeon", roomGenerator.getRandomDescription(), -1, -1, -1, 2));
    }

}
