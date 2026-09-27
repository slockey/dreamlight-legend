package com.slockey.dreamlightlegend.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.slockey.dreamlightlegend.gameobjects.entities.Actor;
import com.slockey.dreamlightlegend.gameobjects.entities.Chest;
import com.slockey.dreamlightlegend.gameobjects.entities.Item;
import com.slockey.dreamlightlegend.gameobjects.map.Direction;

/*
 * Sample Java file by Huw Collingbourne
 *
 * This code (and other sample code) accompanies the book 
 *    "The Little Book of Adventure Game Programming In Java"
 * Source code can be downloaded from:
 *    http://www.bitwisebooks.com
 * 
 */
public class Parser {

    static HashMap<String, WordType> vocab = new HashMap<>();

    static final String MSG_CANT_MOVE_THAT_DIRECTION = "You can't move in that direction.";

    static void initParser() {
        initVocab();
    }

    static void initVocab() {
        // vocab.put("acorn", WordType.NOUN);
        // vocab.put("bed", WordType.NOUN);
        // vocab.put("bone", WordType.NOUN);
        // vocab.put("bowl", WordType.NOUN);
        // vocab.put("box", WordType.NOUN);
        // vocab.put("button", WordType.NOUN);
        // vocab.put("carrot", WordType.NOUN);
        vocab.put("chest", WordType.NOUN);
        // vocab.put("coin", WordType.NOUN);
        vocab.put("dagger", WordType.NOUN);
        vocab.put("door", WordType.NOUN);
        // vocab.put("dust", WordType.NOUN);
        // vocab.put("gardenia", WordType.NOUN);
        // vocab.put("key", WordType.NOUN);
        // vocab.put("knife", WordType.NOUN);
        // vocab.put("lamp", WordType.NOUN);
        // vocab.put("leaflet", WordType.NOUN);
        // vocab.put("lever", WordType.NOUN);
        // vocab.put("paper", WordType.NOUN);
        // vocab.put("pencil", WordType.NOUN);
        // vocab.put("sack", WordType.NOUN);
        vocab.put("self", WordType.NOUN);
        // vocab.put("sausage", WordType.NOUN);
        // vocab.put("sign", WordType.NOUN);
        // vocab.put("slot", WordType.NOUN);
        // vocab.put("squirrel", WordType.NOUN);
        vocab.put("torch", WordType.NOUN);
        // vocab.put("tree", WordType.NOUN);
        // vocab.put("wombat", WordType.NOUN);

        vocab.put("test", WordType.VERB);
        vocab.put("attack", WordType.VERB);
        vocab.put("break", WordType.VERB);
        vocab.put("get", WordType.VERB);
        vocab.put("i", WordType.VERB);
        vocab.put("inventory", WordType.VERB);
        vocab.put("take", WordType.VERB);
        vocab.put("drop", WordType.VERB);
        vocab.put("put", WordType.VERB);
        vocab.put("l", WordType.VERB);
        vocab.put("look", WordType.VERB);
        vocab.put("open", WordType.VERB);
        vocab.put("close", WordType.VERB);
        // vocab.put("pull", WordType.VERB);
        // vocab.put("push", WordType.VERB);
        vocab.put("go", WordType.VERB);
        vocab.put("n", WordType.VERB);
        vocab.put("north", WordType.VERB);
        vocab.put("s", WordType.VERB);
        vocab.put("south", WordType.VERB);
        vocab.put("w", WordType.VERB);
        vocab.put("west", WordType.VERB);
        vocab.put("e", WordType.VERB);
        vocab.put("east", WordType.VERB);
        vocab.put("u", WordType.VERB);
        vocab.put("up", WordType.VERB);
        vocab.put("d", WordType.VERB);
        vocab.put("down", WordType.VERB);
        vocab.put("q", WordType.VERB);
        vocab.put("quit", WordType.VERB);
        vocab.put("wait", WordType.VERB);

        vocab.put("help", WordType.VERB);
        vocab.put("commands", WordType.VERB);

        vocab.put("a", WordType.ARTICLE);
        vocab.put("an", WordType.ARTICLE);
        vocab.put("the", WordType.ARTICLE);

        vocab.put("in", WordType.PREPOSITION);
        vocab.put("into", WordType.PREPOSITION);
        vocab.put("at", WordType.PREPOSITION);
        vocab.put("from", WordType.PREPOSITION);
    }

    static String processVerbNounPrepositionNoun(Game game, Actor actor, List<WordAndType> command) {
        WordAndType wt = command.get(0);
        WordAndType wt2 = command.get(1);
        WordAndType wt3 = command.get(2);
        WordAndType wt4 = command.get(3);
        String msg = "";
        
        if ((wt.getWordtype() != WordType.VERB) || (wt3.getWordtype() != WordType.PREPOSITION)) {
            msg = "Can't do this because I don't understand how to '" + wt.getWord() + " something " + wt3.getWord() + "' something!";
        } else if (wt2.getWordtype() != WordType.NOUN) {
            msg = "Can't do this because '" + wt2.getWord() + "' is not an object!\r\n";
        } else if (wt4.getWordtype() != WordType.NOUN) {
            msg = "Can't do this because '" + wt4.getWord() + "' is not an object!\r\n";
        } else {
            switch (wt.getWord() + wt3.getWord()) {
                case "putin":
                case "putinto":
                    if (wt4.getWord().equalsIgnoreCase("chest")) {
                        if (actor.getRoom().getInventory().contains("chest")) {
                            Item chest = actor.getRoom().getInventory().getItem("chest");
                            msg = game.putItemInChest(actor, (Chest)chest, wt2.getWord());
                        }
                    }
                    break;
                case "getfrom":
                case "takefrom":
                    if (wt4.getWord().equalsIgnoreCase("chest")) {
                        if (actor.getRoom().getInventory().contains("chest")) {
                            Item chest = actor.getRoom().getInventory().getItem("chest");
                            msg = game.takeItemFromChest(actor, (Chest)chest, wt2.getWord());
                        }
                    }
                    break;
                default:
                    msg = "I don't know how to " + wt.getWord() + " " + wt2.getWord() + " " + wt3.getWord() + " " + wt4.getWord() + "!";
                    break;
            }
        }
        return msg;
    }

    static String processVerbPrepositionNoun(Game game, Actor actor, List<WordAndType> command) {
        // "look at" is the only implemented command of this type
        WordAndType wt = command.get(0);
        WordAndType wt2 = command.get(1);
        WordAndType wt3 = command.get(2);
        String msg = "";
        
        if ((wt.getWordtype() != WordType.VERB) || (wt2.getWordtype() != WordType.PREPOSITION)) {
            msg = "Can't do this because I don't understand '" + wt.getWord() + " " + wt2.getWord() + "' !";
        } else if (wt3.getWordtype() != WordType.NOUN) {
            msg = "Can't do this because '" + wt3.getWord() + "' is not an object!\r\n";
        } else {
            switch (wt.getWord() + wt2.getWord()) {
                case "lookat":
                    // msg = AdventureGame.game.lookAtOb(wt3.getWord());
                    msg = String.format("Adventure Game: look at object: {}", wt3.getWord());
                    break;
                case "lookin":
                    if (wt3.getWord().equalsIgnoreCase("chest")) {
                        // is there a chest here
                        if (actor.getRoom().getInventory().contains("chest")) {
                            Item chest = actor.getRoom().getInventory().getItem("chest");
                            msg = game.lookInChest(actor, (Chest)chest);
                        } else {
                            msg = "There doesn't seem to be a chest here.";
                        }
                    }
                    break;
                default:
                    msg = "I don't know how to " + wt.getWord() + " " + wt2.getWord() + " " + wt3.getWord() + "!";
                    break;
            }
        }
        return msg;
    }

    static String processVerbNoun(Game game, Actor actor, List<WordAndType> command) {
        WordAndType wt = command.get(0);
        WordAndType wt2 = command.get(1);
        String msg = "";

        if (wt.getWordtype() != WordType.VERB) {
            msg = "Can't do this because '" + wt.getWord() + "' is not a command!";
        // this is a special case to deal with 'go <direction>' commands
        } else if (wt.getWordtype() == WordType.VERB 
                && wt.getWord().equals("break")
                && wt2.getWordtype() == WordType.VERB 
                && Direction.getDirection(wt2.getWord()) != null) {
                    msg = game.breakDirection(actor, Direction.getDirection(wt2.getWord()));
        } else if (wt.getWordtype() == WordType.VERB 
                && wt.getWord().equals("go")
                && wt2.getWordtype() == WordType.VERB 
                && Direction.getDirection(wt2.getWord()) != null) {
                    List<WordAndType> tempVerb = new ArrayList<WordAndType>();
                    tempVerb.add(wt2);
                    msg = processVerb(game, actor, tempVerb);
        } else if (wt.getWordtype() == WordType.VERB 
                && (wt.getWord().equals("look") || wt.getWord().equals("l"))
                && wt2.getWordtype() == WordType.VERB 
                && Direction.getDirection(wt2.getWord()) != null) {
                    msg = game.lookDirection(actor, Direction.getDirection(wt2.getWord()));
        } else if (wt.getWordtype() == WordType.VERB 
                && wt.getWord().equals("open")
                && wt2.getWordtype() == WordType.VERB 
                && Direction.getDirection(wt2.getWord()) != null) {
                    msg = game.openDirection(actor, Direction.getDirection(wt2.getWord()));
        } else if (wt2.getWordtype() != WordType.NOUN) {
            msg = "Can't do this because '" + wt2.getWord() + "' is not an object!";
        } else {
            switch (wt.getWord()) {
                case "look":
                case "l":
                    if (wt2.getWord().equalsIgnoreCase("self")) {
                        // show self
                        msg = actor.displaySelf();
                    } else {
                        // look at a thing in inventory or in local environment
                        msg = game.lookAtItem(actor, wt2.getWord());
                    }
                    break;
                case "take":
                case "get":
                    // the is probably in a room
                    msg = game.takeItemFromRoom(actor, wt2.getWord());
                    break;
                case "drop":
                    // drop the item in the current room
                    msg = game.dropItemToRoom(actor, wt2.getWord());
                    break;
                case "open":
                    msg = "I don't know how to open a " + wt2.getWord() + ".";
                    if (wt2.getWord().equalsIgnoreCase("chest")) {
                        if (actor.getRoom().getInventory().contains("chest")) {
                            Item chest = actor.getRoom().getInventory().getItem("chest");
                            msg = game.openChest(actor, (Chest)chest);
                        } else {
                            msg = "There doesn't seem to be a chest here to open.";
                        }
                    }
                    break;
                case "break":
                    // trying to break an item
                    msg = "Why would you want to break the " + wt2.getWord() + "?";
                    // break the chest
                    if (wt2.getWord().equalsIgnoreCase("chest")) {
                        if (actor.getRoom().getInventory().contains("chest")) {
                            Item chest = actor.getRoom().getInventory().getItem("chest");
                            msg = game.breakChest(actor, (Chest)chest);
                        } else {
                            msg = "There doesn't seem to be a chest here to break.";
                        }
                    }
                    break;
                case "close":
                    msg = "I don't know how to close a " + wt2.getWord() + ".";
                    if (wt2.getWord().equalsIgnoreCase("chest")) {
                        if (actor.getRoom().getInventory().contains("chest")) {
                            Item chest = actor.getRoom().getInventory().getItem("chest");
                            msg = game.closeChest(actor, (Chest)chest);
                        } else {
                            msg = "There doesn't seem to be a chest here to open.";
                        }
                    }
                    break;
                default:
                    msg += " (not yet implemented)";
                    break;
            }
        }
        return msg;
    }

    static String processVerb(Game game, Actor actor, List<WordAndType> command) {
        WordAndType wt = command.get(0);
        boolean success = false;
        String msg = "";

        if (wt.getWordtype() != WordType.VERB) {
            msg = "Can't do this because '" + wt.getWord() + "' is not a command!";
        } else {
            switch (wt.getWord()) {
                case "n":
                case "north":
                    success = game.moveActor(actor, Direction.NORTH);
                    msg = (success) ? actor.getRoom().getDisplayString() : MSG_CANT_MOVE_THAT_DIRECTION;
                    break;
                case "s":
                case "south":
                    success = game.moveActor(actor, Direction.SOUTH);
                    msg = (success) ? actor.getRoom().getDisplayString() : MSG_CANT_MOVE_THAT_DIRECTION;
                    break;
                case "w":
                case "west":
                    success = game.moveActor(actor, Direction.WEST);
                    msg = (success) ? actor.getRoom().getDisplayString() : MSG_CANT_MOVE_THAT_DIRECTION;
                    break;
                case "e":
                case "east":
                    success = game.moveActor(actor, Direction.EAST);
                    msg = (success) ? actor.getRoom().getDisplayString() : MSG_CANT_MOVE_THAT_DIRECTION;
                    break;
                case "u":
                case "up":
                    success = game.moveActor(actor, Direction.UP);
                    msg = (success) ? actor.getRoom().getDisplayString() : MSG_CANT_MOVE_THAT_DIRECTION;
                    break;
                case "d":
                case "down":
                    success = game.moveActor(actor, Direction.DOWN);
                    msg = (success) ? actor.getRoom().getDisplayString() : MSG_CANT_MOVE_THAT_DIRECTION;
                    break;
                case "l":
                case "look":
                    msg = actor.getRoom().getDisplayString();
                    break;
                case "inventory":
                case "i":
                    msg = actor.displayInventory();
                    break;
                case "help":
                    msg = String.format("The void helps those who help themselves. You must find your way out or through.\nPerhaps if you referred to the commands...");
                    break;
                case "commands":
                    msg = game.displayCommands();
                    break;
                case "wait":
                    msg = game.waitCommand();
                    break;
                case "test":
                    // AdventureGame.game.test();
                    msg = String.format("Adventure Game: test");
                    break;
                default:
                    msg = wt.getWord() + " (not yet implemented)";
                    break;
            }
        }
        return msg;
    }

    static String processCommand(Game game, Actor actor, List<WordAndType> command) {
        String s = "";
        
        if (command.size() == 0) {
            s = "You must write a command!";
        } else if (command.size() > 4) {
            s = "That command is too long!";
        } else {           
            switch (command.size()) {
                case 1:
                    s = processVerb(game, actor, command);
                    break;
                case 2:
                    s = processVerbNoun(game, actor, command);
                    break;
                case 3:
                    s = processVerbPrepositionNoun(game, actor, command);
                    break;
                case 4:
                    s = processVerbNounPrepositionNoun(game, actor, command);
                    break;
                default:
                    s = "Unable to process command";
                    break;
            }
        }
        return s;
    }

    static String parseCommand(Game game, Actor actor, List<String> wordlist) {
        List<WordAndType> command = new ArrayList<>();
        WordType wordtype;
        String errmsg = "";
        String msg;

        for (String k : wordlist) {
            if (vocab.containsKey(k)) {
                wordtype = vocab.get(k);
                if (wordtype == WordType.ARTICLE) {       // ignore articles             
                } else {
                    command.add(new WordAndType(k, wordtype));
                }
            } else { // if word not found in vocab
                command.add(new WordAndType(k, WordType.ERROR));
                errmsg = "Sorry, I don't understand '" + k + "'";
            }
        }
        if (!errmsg.isEmpty()) {
            msg = errmsg;
        } else {
            msg = processCommand(game, actor, command);
        }
        return msg;
    }

    static List<String> wordList(String input) {
        String delims = "[ \t,.:;?!\"']+";
        List<String> strlist = new ArrayList<>();
        String[] words = input.split(delims);

        for (String word : words) {
            strlist.add(word);
        }
        return strlist;
    }
}
