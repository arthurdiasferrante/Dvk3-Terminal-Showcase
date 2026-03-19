package core_logic.views;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

public class SafeHaltScreen {

    private final int MAX_INTENSITY = 100;
    TextColor amber = new TextColor.RGB(255, 176, 0);


    public void draw(Screen screen, TextGraphics tGraphics, long animTick, TerminalSize screenSize) {

        screen.setCursorPosition(null);
        tGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        tGraphics.setForegroundColor(amber);

        String text1 = "It is now safe to turn off";
        String text2 = "your computer.";
        String shutdownButton = "[ESC]";
        int centerRow = screenSize.getRows() / 2;
        int bottomRow = screenSize.getRows() - 5;

        tGraphics.putString((screenSize.getColumns() - text1.length()) / 2, centerRow, text1);
        tGraphics.putString((screenSize.getColumns() - text2.length()) / 2, centerRow + 1, text2);
        tGraphics.setForegroundColor(dynamicAmber());
        tGraphics.putString((screenSize.getColumns() - shutdownButton.length()) / 2, bottomRow, shutdownButton);
    }

    private TextColor dynamicAmber() {
        long loopTick = System.currentTimeMillis() % 6000;
        long intensity;
        if (loopTick < 2000) {
            intensity = (loopTick * 100) / 2000;
        } else if (loopTick < 4000) {
            intensity = 100;
        } else {
            intensity = (6000 - loopTick) * 100 / 1999;
        }

        double intensityFactor = (double) intensity / MAX_INTENSITY;

        int r = (int) Math.max(0, intensityFactor * 255);
        int g = (int) Math.max(0, intensityFactor * 176);
        return new TextColor.RGB(r, g, 0);
    }
}
