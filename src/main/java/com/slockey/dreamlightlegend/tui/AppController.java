package com.slockey.dreamlightlegend.tui;

import com.slockey.dreamlightlegend.engine.Game;

import dev.tamboui.tui.TuiRunner;
import dev.tamboui.tui.event.KeyCode;
import dev.tamboui.tui.event.KeyEvent;

public class AppController {

    private final AppModel model = new AppModel();
    private final AppView view = new AppView();
    private final Game game = new Game();

    public void start() throws Exception {
        // Set the landing message to the model
        model.setSubmittedMessage(game.runCommand("look"));
        // update the ui turn counter
        model.setTurnCounter(game.getTurnCounter());

        // Initialize the mid-level TuiRunner event manager loop
        try (TuiRunner tui = TuiRunner.create()) {
            
            tui.run(
                // Hook A: Process Keyboard Input Events
                (event, runner) -> {
                    if (event instanceof KeyEvent keyEvent) {
                        return handleKeyboard(keyEvent, runner);
                    }
                    return false;
                },
                // Hook B: Drive the Immediate Mode Rendering Engine
                frame -> view.draw(frame.area(), frame.buffer(), model)
            );
        }
    }

    private boolean handleKeyboard(KeyEvent event, TuiRunner runner) {
        // Use standard TamboUI helper hooks to exit cleanly on "q" or "Esc" keys
        if (event.isQuit() || event.isCancel()) {
            runner.quit();
            return true;
        }

        // Handle mutations to our target text field state
        if (event.code() == KeyCode.CHAR) {
            model.getInputState().insert(event.string());
            return true;
        } else if (event.code() == KeyCode.BACKSPACE) {
            model.getInputState().deleteBackward();
            return true;
        } else if (event.code() == KeyCode.DELETE) {
            model.getInputState().deleteForward();
            return true;
        } else if (event.code() == KeyCode.LEFT) {
            model.getInputState().moveCursorLeft();
            return true;
        } else if (event.code() == KeyCode.RIGHT) {
            model.getInputState().moveCursorRight();
            return true;
        } else if (event.code() == KeyCode.ENTER) {
            // Process the message inside the controller layer
            String text = model.getInputState().text();
            // run game command
            String resultString = game.runCommand(text);
            if (!text.isBlank()) {
                model.setSubmittedMessage(resultString);
                model.setTurnCounter(game.getTurnCounter());
                model.getInputState().clear(); // Reset line input
            }
            return true;
        }

        return false;
    }
}