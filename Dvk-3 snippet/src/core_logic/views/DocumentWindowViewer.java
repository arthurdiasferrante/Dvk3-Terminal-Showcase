package core_logic.views;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.TextCharacter;

import core_logic.models.physical.Dvk3Core;
import core_logic.models.system.Dvk3DocReader;
import core_logic.models.system.Dvk3System;

import java.text.DecimalFormat;
import java.util.List;

public class DocumentWindowViewer {

    TextColor amber = new TextColor.RGB(255, 176, 0);

    public void draw(Dvk3System system, Dvk3Core core, Screen screen, TextGraphics tGraphics, long animTick, TerminalSize screenSize) {
        Dvk3DocReader data = system.getDocReader();
        TextColor dimColor = new TextColor.RGB(60, 40, 0);

        int w = 55;
        int h = 30;
        int startY = (screenSize.getRows() - h) / 2;
        int startX = (screenSize.getColumns() - w) / 2;

        for (int y = 0; y < screenSize.getRows(); y++) {
            for (int x = 0; x < screenSize.getColumns(); x++) {
                TextCharacter c = screen.getFrontCharacter(x, y);
                if (c != null) {
                    TextCharacter dimmedChar = c.withForegroundColor(dimColor);
                    tGraphics.setCharacter(x, y, dimmedChar);
                }
            }
        }

        // desenha o Fundo da Janela
        tGraphics.setForegroundColor(amber);
        tGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        tGraphics.fillRectangle(new TerminalPosition(startX, startY), new TerminalSize(w, h), ' ');

        // bordas
        tGraphics.drawLine(startX, startY, startX + w - 1, startY, '═');
        tGraphics.drawLine(startX, startY + h - 1, startX + w - 1, startY + h - 1, '═');
        tGraphics.drawLine(startX, startY, startX, startY + h - 1, '║');
        tGraphics.drawLine(startX + w - 1, startY, startX + w - 1, startY + h - 1, '║');

        // cantos
        tGraphics.setCharacter(startX, startY, '╔');
        tGraphics.setCharacter(startX + w - 1, startY, '╗');
        tGraphics.setCharacter(startX, startY + h - 1, '╚');
        tGraphics.setCharacter(startX + w - 1, startY + h - 1, '╝');

        // header, topo com informações
        tGraphics.drawLine(startX, startY + 2, startX + w - 1, startY + 2, '═');
        tGraphics.setCharacter(startX, startY + 2, '╠');
        tGraphics.setCharacter(startX + w - 1, startY + 2, '╣');

        tGraphics.putString(startX + 2, startY + 1, "FILE: " + data.getReadFileName());

        // paginas na direita do header
        int current = data.getCurrentPage() + 1;
        int total = (data.getTotalLines() + data.getLINES_PER_PAGE() - 1) / data.getLINES_PER_PAGE();
        if (total == 0) total = 1;
        String pages = String.format("[ PG %02d/%02d ]", current, total);

        tGraphics.putString(startX + w - pages.length() - 2, startY + 1, pages);

        drawFooter(system, tGraphics, screenSize);

    }

    public void drawFooter(Dvk3System system, TextGraphics tGraphics, TerminalSize screenSize) {
        Dvk3DocReader data = system.getDocReader();
        int w = 55;
        int h = 30;
        int startY = (screenSize.getRows() - h) / 2;
        int startX = (screenSize.getColumns() - w) / 2;

        String prev = "[◄] PREV";
        String exit = "[ESC] EXIT";
        String next = "[►] NEXT";

        if (data.isTuningMode()) {
            DecimalFormat df = new DecimalFormat("#,##0,000");
            exit = df.format(data.getCurrentFrequency()) + "Hz";
            next = "[ENTER] CONFIRM";
            prev = "[ESC] CANCEL";
            drawTuningControls(tGraphics, screenSize);
        }

        tGraphics.drawLine(startX, startY + h - 3, startX + w - 1, startY + h - 3, '═');
        tGraphics.setCharacter(startX, startY + h - 3, '╠');
        tGraphics.setCharacter(startX + w - 1, startY + h - 3, '╣');

        tGraphics.putString(startX + 2, startY + h - 2, prev);
        tGraphics.putString(startX + (w - exit.length()) / 2, startY + h - 2, exit);
        tGraphics.putString(startX + w - next.length() - 2, startY + h - 2, next);

        List<String> lines = data.getFormattedLines();
        if (lines == null) return;

        int start = data.getCurrentPage() * data.getLINES_PER_PAGE();
        int end = Math.min(start + data.getLINES_PER_PAGE(), lines.size());

        for (int i = start; i < end; i++) {
            String line = lines.get(i);

            int yScreen = startY + 3 + ((i - start) * 2);
            int xBase = startX + 2;

            tGraphics.putString(xBase, yScreen, line);
        }
    }

    private void drawTuningControls(TextGraphics tGraphics, TerminalSize size) {

        String anchorHeader = " ▓▓▌ ";
        String anchorBody = " ░░▌ ";

        String titleText = " TUNING MODULE CONTROLS ";
        String divider = "═════════════════════════════";
        String line1Text = "▸ COARSE : [◄][►] ±1";
        String line2Text = "▸ FINE : [⇧]+[◄][►] ±0.50";

        String l0 = anchorHeader + titleText;
        String l1 = anchorHeader + divider;
        String l2 = anchorBody + line1Text;
        String l3 = anchorBody + line2Text;

        int maxWidth = Math.max(l0.length(), Math.max(l2.length(), l3.length()));

        int xBase = size.getColumns() - maxWidth - 1;
        int yBase = size.getRows() - 6;

        tGraphics.setForegroundColor(amber);
        tGraphics.setBackgroundColor(TextColor.ANSI.BLACK);

        tGraphics.putString(xBase, yBase, l0);
        tGraphics.putString(xBase, yBase + 1, l1);
        tGraphics.putString(xBase, yBase + 2, l2);
        tGraphics.putString(xBase, yBase + 3, l3);

        tGraphics.setForegroundColor(TextColor.ANSI.DEFAULT);
    }

    public void drawSafeDecryption() {

    }
}