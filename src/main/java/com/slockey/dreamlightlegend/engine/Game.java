package com.slockey.dreamlightlegend.engine;

import java.util.ArrayList;
import java.util.List;

import com.slockey.dreamlightlegend.gameobjects.entities.Actor;
import com.slockey.dreamlightlegend.gameobjects.entities.ItemFactory;
import com.slockey.dreamlightlegend.gameobjects.entities.Player;
import com.slockey.dreamlightlegend.gameobjects.map.Map;
import com.slockey.dreamlightlegend.gameobjects.map.Room;
import com.slockey.dreamlightlegend.gameobjects.map.RoomGenerator;

public class Game {

    private Map map;
    private Player player;

    public Game() {
        Parser.initParser();
        initGame();

        // XXX: temp name, description, first room on map
        player = new Player("Player", "Just some person, you know?", map.getRooms().get(0));
        // just for fun give the player a dagger
        player.getInventory().add(ItemFactory.getDaggerInstance());

    }

    private void initGame() {
        // TODO: generate or reconstitute the game
        // init the map
        map = new Map();
		map.init();
        // init the stuff
    }

    public String runCommand(String input) {
        List<String> wordlist;
        String msg;
        String lowstr;

        msg = "ok";
        lowstr = input.trim().toLowerCase();
        
        if (!lowstr.equals("q")) {
            if (lowstr.equals("")) {
                msg = "You must enter a command";
            } else {
                wordlist = Parser.wordList(lowstr);
                // XXX: this sucks. the parser should likely not be static
                msg = Parser.parseCommand(this, player, wordlist);
            }
        }
        return msg;

    }

    public String lookDirection(Actor actor, Direction direction) {
        String msg = "";
        Room currentRoom = actor.getRoom();
        int targetDirection = currentRoom.getDirection(direction);
        switch(targetDirection) {
            case (Direction.NOEXIT):
                msg = "You stare at stone wall. There is no exit " + direction.toString().toLowerCase();
                break;
            case (Direction.DOOR):
            case (Direction.LOCKED_DOOR):
                msg = "The way " + direction.toString().toLowerCase() + " is blocked by a door.";
                break;
            case (Direction.BARRICADE):
                msg = "The way " + direction.toString().toLowerCase() + " is barricaded.";
                break;
            default:
                msg = "The passage " + direction.toString().toLowerCase() + " leads off into the dark.";
                break;
        };

        return msg;
    }

    public boolean moveActor(Actor actor, Direction direction) {
        boolean moved = false;

        // get the actor's current room
        Room currentRoom = actor.getRoom();

        // determine if the move can happen
        int targetDirection = currentRoom.getDirection(direction);
        if (targetDirection != Direction.NOEXIT) {
            // check if we should generate a next room
            if (targetDirection == Direction.GENEXIT) {
                // generate a room
                // TODO: refactor this into RoomGenerator
                RoomGenerator roomGenerator = new RoomGenerator();
                ArrayList<Room> rooms = map.getRooms();
                int newRoomId = rooms.size();
                switch (direction) {
                    case Direction.NORTH:
                        currentRoom.setNorth(newRoomId);
                        break;
                    case Direction.EAST:
                        currentRoom.setEast(newRoomId);
                        break;
                    case Direction.SOUTH:
                        currentRoom.setSouth(newRoomId);
                        break;
                    case Direction.WEST:
                        currentRoom.setWest(newRoomId);
                    default:
                        break;
                }
                Room generatedRoom = roomGenerator.generateRoom(newRoomId, currentRoom);
                rooms.add(generatedRoom);
                targetDirection = newRoomId;

                // get the target room
                Room targetRoom = map.getRooms().get(targetDirection);
                // apply room to actor
                actor.setRoom(targetRoom);
                moved = true;
            }

            if (targetDirection == Direction.DOOR || targetDirection == Direction.LOCKED_DOOR) {
                moved = false;
            }

        }

        return moved;
    }

    // utility method to display string if not empty
    // stripping any trailing newlines
    public void showStr(String s) {
        if (s.endsWith("\n")) {
            s = s.substring(0, s.length() - 1);
        }
        if (!s.isEmpty()) {
            System.out.println(s);
        }
    }

}
