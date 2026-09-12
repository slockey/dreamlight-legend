package com.slockey.dreamlightlegend.tui;

import dev.tamboui.buffer.Buffer;
import dev.tamboui.layout.Alignment;
import dev.tamboui.layout.Constraint;
import dev.tamboui.layout.Layout;
import dev.tamboui.layout.Rect;
import dev.tamboui.widgets.block.Block;
import dev.tamboui.widgets.block.Borders;
import dev.tamboui.widgets.paragraph.Paragraph;
import dev.tamboui.widgets.input.TextInput;
import dev.tamboui.style.Overflow;
import dev.tamboui.style.Style;

import java.util.List;

public class AppView {

    public void draw(Rect area, Buffer buffer, AppModel model) {
        // Divide terminal screen into split sections (Text Field on top, Output on bottom)
        List<Rect> chunks = Layout.vertical()
                .constraints(new Constraint[]{
                        Constraint.length(3), // Height of text input
                        Constraint.min(1)     // Remaining space for output
                })
                .split(area);

        // 1. Build and render the text input box
        TextInput textInput = TextInput.builder()
                .placeholder("Type a message and hit Enter...")
                .cursorStyle(Style.EMPTY.reversed())
                .block(Block.builder().title(" Input ").borders(Borders.ALL).build())
                .build();
        
        textInput.render(chunks.get(0), buffer, model.getInputState());

        // 2. Build and render the feedback block
        String feedbackBlockTitle = " Game Turn: " + model.getTurnCounter() + " ";
        Paragraph feedbackText = Paragraph.builder()
                // .text("Last Submission: " + model.getSubmittedMessage())
                .text(model.getSubmittedMessage())
                .overflow(Overflow.WRAP_WORD)
                .alignment(Alignment.LEFT)
                .block(Block.builder().title(feedbackBlockTitle).borders(Borders.ALL).build())
                .build();

        feedbackText.render(chunks.get(1), buffer);
    }
}