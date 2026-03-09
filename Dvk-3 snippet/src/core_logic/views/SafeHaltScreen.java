package core_logic.views;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import core_logic.models.system.Dvk3System;

public class SafeHaltScreen {

    TextColor amber = new TextColor.RGB(255, 176, 0);

    public void draw(Screen screen, TextGraphics tGraphics, long animTick, TerminalSize screenSize) {

        screen.setCursorPosition(null);
        tGraphics.setBackgroundColor(TextColor.ANSI.BLACK);

        String text1 = "It's now safe to turn off";
        String text2 = "your computer.";
        int line = screenSize.getRows() / 2;

        tGraphics.putString((screenSize.getColumns() - text1.length()) / 2, line, text1);
        tGraphics.putString((screenSize.getColumns() - text2.length()) / 2, line + 1, text2);
    }
}
