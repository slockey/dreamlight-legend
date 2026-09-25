package com.slockey.dreamlightlegend.engine;

import java.util.ArrayList;
import java.util.List;

import com.slockey.dreamlightlegend.gameobjects.entities.Actor;
import com.slockey.dreamlightlegend.gameobjects.entities.Item;
import com.slockey.dreamlightlegend.gameobjects.entities.Player;
import com.slockey.dreamlightlegend.gameobjects.map.Direction;
import com.slockey.dreamlightlegend.gameobjects.map.Exit;
import com.slockey.dreamlightlegend.gameobjects.map.ExitState;
import com.slockey.dreamlightlegend.gameobjects.map.IdGenerator;
import com.slockey.dreamlightlegend.gameobjects.map.Map;
import com.slockey.dreamlightlegend.gameobjects.map.Room;

public class Game {

    private Map map;
    private Player player;
    private int turnCounter;

    public Game() {
        Parser.initParser();
        // init local game stuff incl map
        initGame();

        // 8 + D6 starting health
        int playerStartingHealth = (int)(Math.random() * 6) + 1;
        playerStartingHealth += 8;
        // starting skill
        int athletics = 10;

        // XXX: temp name, description, first room on map
        player = new Player("Player", 
                    "Just some person, you know?", 
                    playerStartingHealth,
                    athletics,
                    map.getStartingRoom());

    }

    private void initGame() {
        // TODO: generate or reconstitute the game
        // init the map
        map = new Map();
		map.init();
        // init the stuff
        turnCounter = 0;
    }

    public int getTurnCounter() {
        return turnCounter;
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

    public String takeItemFromRoom(Actor actor, String itemName) {
        String msg = "You don't see a " + itemName + " here to take.";
        // find the item in the current room
        ArrayList<Item> roomItems = actor.getRoom().getItems();
        for (Item item : roomItems) {
            // find the first matching item
            if (item.getName().equalsIgnoreCase(itemName)) {
                actor.getInventory().add(item);
                roomItems.remove(item);
                msg = "You take the " + item.getName() + ".";
                break;
            }
        }

        return msg;
    }

    public String dropItemToRoom(Actor actor, String itemName) {
        String msg = "You don't have a " + itemName + " to drop.";
        // find the item in the actor inventory
        ArrayList<Item> actorItems = actor.getInventory();
        for (Item item : actorItems) {
            // find the first matching item
            if (item.getName().equalsIgnoreCase(itemName)) {
                actor.getRoom().getItems().add(item);
                actorItems.remove(item);
                msg = "You drop the " + item.getName() + " on the floor.";
                break;
            }
        }
        return msg;
    }

    public String breakDirection(Actor actor, Direction direction) {
        String msg = "";
        Room currentRoom = actor.getRoom();
        Exit exit = currentRoom.getExit(direction);

        // verify - target exit can be broken
        if (exit.getExitState() == ExitState.CLOSED
            || exit.getExitState() == ExitState.LOCKED
            || exit.getExitState() == ExitState.BLOCKED) {
            // test athletics to break down the barrier
            if (attemptToBreakExit(actor)) {
                exit.setExitState(ExitState.OPEN);
                exit.setDescription("A door hangs open and the passage beyond leads off into the dark.");
                msg = "With some effort the door becomes unstuck. The door screams into the darkness as you pull it open.";
            } else {
                msg = "The door begins to crack and strain, but hasn't broken.";
            }
        } else if (exit.getExitState() == ExitState.TRAPPED) {
            exit.setExitState(ExitState.OPEN);
            msg = "A trap is sprung. Something happens!";
        } else {
            msg = "There doesn't seem to be anything here to open.";
        }

        // update the turn counter
        turnCounter += 1;

        return msg;
    }

    private boolean attemptToBreakExit(Actor actor) {
        boolean result = false;

        // get random pct value equal to or less than actor athletics
        int pctValue = NumberGenerator.getPercentile();
        if (pctValue <= actor.getAthletics()) {
            result = true;
        }

        return result;
    }

    public String openDirection(Actor actor, Direction direction) {
        String msg = "";
        Room currentRoom = actor.getRoom();
        Exit exit = currentRoom.getExit(direction);

        // verify - target exit can be opened
        if (exit.getExitState() == ExitState.CLOSED) {
            exit.setExitState(ExitState.OPEN);
            exit.setDescription("A door hangs open and the passage beyond leads off into the dark.");
            msg = "With some effort the door becomes unstuck. The door screams into the darkness as you pull it open.";
        } else if (exit.getExitState() == ExitState.LOCKED) {
            msg = "You must unlock the door first.";
        } else if (exit.getExitState() == ExitState.BLOCKED) {
            msg = "You must break the barricade first.";
        } else if (exit.getExitState() == ExitState.TRAPPED) {
            exit.setExitState(ExitState.OPEN);
            msg = "A trap is sprung. Something happens!";
        } else {
            msg = "There doesn't seem to be anything here to open.";
        }

        // update the turn counter
        turnCounter += 1;

        return msg;
    }

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

        // update the turn counter
        turnCounter += 1;

        return result;

    }

    public String waitCommand() {
        turnCounter += 1;
        return "You wait and time passes.";
    }

    public String displayCommands() {
        StringBuilder builder = new StringBuilder();
        // return "TBD - commands will be displayed";

        // how to write commands
        builder.append("Commands can be composed in the following ways:\n\n");
        builder.append("\tVerb\n");
        builder.append("\tVerb Noun\n");
        builder.append("\tVerb Preposition Noun\n");
        builder.append("\tVerb Noun Preposition Noun");

        // verbs
        builder.append("\n\nExample verbs:\n\n");
        builder.append("look, go, break, wait...\n\n");

        // nouns
        builder.append("\n\nExample nouns:\n\n");
        builder.append("north, torch, self...\n\n");

        // nouns
        builder.append("\n\nExample commands:\n\n");
        builder.append("go north, get torch, look self...\n\n");

        return builder.toString();
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
