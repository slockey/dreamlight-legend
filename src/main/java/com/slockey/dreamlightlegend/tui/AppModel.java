package com.slockey.dreamlightlegend.tui;

import dev.tamboui.widgets.input.TextInputState;

public class AppModel {
    // Stores text field cursor, text selection, and current content
    private final TextInputState inputState = new TextInputState("");
    private String submittedMessage = "No submissions yet.";
    private int turnCounter = 0;

    public TextInputState getInputState() {
        return inputState;
    }

    public int getTurnCounter() {
        return turnCounter;
    }

    public void setTurnCounter(int turnCounter) {
        this.turnCounter = turnCounter;
    }

    public String getSubmittedMessage() {
        return submittedMessage;
    }

    public void setSubmittedMessage(String message) {
        this.submittedMessage = message;
    }
}