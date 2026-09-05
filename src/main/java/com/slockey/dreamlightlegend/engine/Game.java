package com.slockey.dreamlightlegend.engine;

import java.util.List;

import com.slockey.dreamlightlegend.gameobjects.entities.Actor;
import com.slockey.dreamlightlegend.gameobjects.entities.ItemFactory;
import com.slockey.dreamlightlegend.gameobjects.entities.Player;
import com.slockey.dreamlightlegend.gameobjects.map.Direction;
import com.slockey.dreamlightlegend.gameobjects.map.Exit;
import com.slockey.dreamlightlegend.gameobjects.map.ExitState;
import com.slockey.dreamlightlegend.gameobjects.map.IdGenerator;
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
        player = new Player("Player", 
                    "Just some person, you know?", 
                                map.getStartingRoom());
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

    // public String openDirection(Actor actor, Direction_Deprecated direction) {
    //     String msg = "";
    //     Room currentRoom = actor.getRoom();
    //     int targetDirection = currentRoom.getDirection(direction);
    //     if (targetDirection == Direction_Deprecated.DOOR) {
    //         if (changeRoomDirectionValue(currentRoom, direction, Direction_Deprecated.GENEXIT)) {
    //             msg = "With some effort the door becomes unstuck. The door screams into the darkness as you pull it open.";
    //         } else {
    //             msg = "You fail to open the door. It remains stuck fast.";
    //         }
    //     } else if (targetDirection == Direction_Deprecated.LOCKED_DOOR) {
    //         msg = "You must unlock the door first.";
    //     } else if (targetDirection == Direction_Deprecated.BARRICADE) {
    //         msg = "You must break the barricade first.";
    //     }
    //     return msg;
    // }

    // private boolean changeRoomDirectionValue(Room_Deprecated room, Direction_Deprecated fromDirection, int toDirection) {
    //     boolean success = false;
    //     switch (fromDirection) {
    //         case Direction_Deprecated.NORTH:
    //             room.setNorth(toDirection);
    //             success = true;
    //             break;
    //         case Direction_Deprecated.EAST:
    //             room.setEast(toDirection);
    //             success = true;
    //             break;
    //         case Direction_Deprecated.SOUTH:
    //             room.setSouth(toDirection);
    //             success = true;
    //             break;
    //         case Direction_Deprecated.WEST:
    //             room.setWest(toDirection);
    //             success = true;
    //             break;
    //         default:
    //             break;
    //     }
    //     return success;
    // }

    public String lookDirection(Actor actor, Direction direction) {
        Room currentRoom = actor.getRoom();
        Exit targetExit = currentRoom.getExit(direction);
        return targetExit.getDescriptionString();
    }

    public boolean moveActor(Actor actor, Direction direction) {
        boolean result = false;

        // get the actor's current room
        Room currentRoom = actor.getRoom();
        // get the target Exit
        Exit targetExit = currentRoom.getExit(direction);

        if (targetExit.getExitState() == ExitState.OPEN) {
            // determine if we need to generate a room
            if (targetExit.leadsToRoom(currentRoom.getId()).equals(IdGenerator.getNullIdString())) {
                Room newRoom = map.addNewRoom(currentRoom, direction);
                actor.setRoom(newRoom);
                result = true;
            } else {
                // the room probably already exists
                Room toRoom = map.getRoom(targetExit.leadsToRoom(currentRoom.getId()));
                actor.setRoom(toRoom);
                result = true;
            }
        }

        return result;

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
