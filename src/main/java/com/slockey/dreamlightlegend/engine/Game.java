package com.slockey.dreamlightlegend.engine;

import java.util.ArrayList;
import java.util.List;

import com.slockey.dreamlightlegend.gameobjects.entities.Actor;
import com.slockey.dreamlightlegend.gameobjects.entities.Chest;
import com.slockey.dreamlightlegend.gameobjects.entities.Inventory;
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
        Inventory roomInventory = actor.getRoom().getInventory();
        if (roomInventory.contains(itemName)) {
            Item item = roomInventory.getItem(itemName);
            actor.getInventory().addItem(item);
            roomInventory.removeItem(item);
            msg = "You take the " + item.getName() + ".";
        }

        return msg;
    }

    public String dropItemToRoom(Actor actor, String itemName) {
        String msg = "You don't have a " + itemName + " to drop.";
        // find the item in the actor inventory
        if (actor.getInventory().contains(itemName)) {
            Item item = actor.getInventory().getItem(itemName);
            actor.getRoom().getInventory().addItem(item);
            actor.getInventory().removeItem(item);
            msg = "You drop the " + item.getName() + " on the floor.";
        }

        return msg;
    }

    // from inventory to container
    public String putItemInChest(Actor actor, Chest chest, String itemName) {
        String msg = "You don't have a " + itemName + " to put in the chest.";

        if (actor.getInventory().contains(itemName)) {
            Item item = actor.getInventory().getItem(itemName);
            chest.getInventory().addItem(item);
            actor.getInventory().removeItem(item);
            msg = "You put the " + item.getName() + " into the chest.";
        }

        return msg;
    }

    // from container to inventory
    public String takeItemFromChest(Actor actor, Chest chest, String itemName) {
        String msg = "The chest doesn't contain a " + itemName;

        if (chest.getInventory().contains(itemName)) {
            Item item = chest.getInventory().getItem(itemName);
            actor.getInventory().addItem(item);
            chest.getInventory().removeItem(item);
            msg = "You take the " + item.getName() + ".";
        }

        return msg;
    }

    // break the lock
    public String breakChest(Actor actor, Chest chest) {
        String msg = "Why would you want to do that? Perhaps you should just open the chest.";

        if (chest.isLocked()) {
            if (attemptToBreak(actor)) {
                chest.setLocked(false);
                msg = "You hammer at the lock and with a great crack it is now unlocked.";
            } else {
                msg = "You hammer at the lock, but it has not yet broken.";
            }
        }

        return msg;
    }

    public String openChest(Actor actor, Chest chest) {
        String msg = "The lid of the chest hangs open revealing it's contents.";

        if (chest.isClosed()) {
            if (chest.isLocked()) {
                return "The chest appears to be locked.";
            }
            if (chest.isTrapped()) {
                msg = "The hinges squeek as you open the lid of the chest.\n";
                msg += "You gasp and choke as the chest releases toxic fumes.";
                actor.setHealth(actor.getHealth() - 5);
                // trap only goes off one time
                chest.setTrapped(false);
            }
            chest.setClosed(false);
        }
        return msg;
    }

    public String lookInChest(Actor actor, Chest chest) {
        String msg = "The chest is empty.";

        if (chest.isClosed()) {
            return "You must open the chest first.";
        }

        if (chest.getInventory().getSize() > 0) {
            msg = "The chest contains: \n";
            msg += chest.getInventory().displayContents();
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
            if (attemptToBreak(actor)) {
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

    private boolean attemptToBreak(Actor actor) {
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

    // this could be in actor inventory or in the current room
    public String lookAtItem(Actor actor, String itemName) {
        String msg = "There doesn't seem to be a " + itemName + " here.";

        // is this item in the actor inventory?
        // is this item in the current room?
        if (actor.getInventory().contains(itemName)) {
            msg = actor.displayInventoryItemByName(itemName);
        } else if (actor.getRoom().getInventory().contains(itemName)) {
            Item item = actor.getRoom().getInventory().getItem(itemName);
            msg = item.getDescription();
        }

        return msg;
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
