package core_logic.views;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.TextCharacter;

import core_logic.models.system.Dvk3DocReader;
import core_logic.models.system.Dvk3System;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

import static core_logic.models.utils.SignalVisualizer.getWaveForm;
import static core_logic.models.rules.Dvk3Config.*;

public class DocumentWindowViewer {

    private final TextColor amber = new TextColor.RGB(255, 176, 0);
    private final TextColor dimColor = new TextColor.RGB(60, 40, 0);

    public void draw(Dvk3System system, Screen screen, TextGraphics tGraphics, long animTick, TerminalSize screenSize) {
        Dvk3DocReader data = system.getDocReader();

        int w = 55;
        int h = 30;
        int startY = (screenSize.getRows() - h) / 2;
        int startX = (screenSize.getColumns() - w) / 2;

        dimBackground(screen, tGraphics, screenSize);
        drawWindowFrame(tGraphics, startX, startY, w, h);
        drawHeader(tGraphics, data, startX, startY, w);
        drawFooter(tGraphics, data, startX, startY, w, h);
        drawContent(tGraphics, data, startX, startY, animTick);

        if (data.isTuningMode()) {
            drawTuningControls(tGraphics, screenSize);
            drawSignalScope(tGraphics, startX, startY + h, w, data);
        }
    }

    private void dimBackground(Screen screen, TextGraphics tGraphics, TerminalSize screenSize) {
        for (int y = 0; y < screenSize.getRows(); y++) {
            for (int x = 0; x < screenSize.getColumns(); x++) {
                TextCharacter c = screen.getFrontCharacter(x, y);
                if (c != null) {
                    TextCharacter dimmedChar = c.withForegroundColor(dimColor);
                    tGraphics.setCharacter(x, y, dimmedChar);
                }
            }
        }
    }

    private void drawWindowFrame(TextGraphics tGraphics, int startX, int startY, int w, int h) {
        tGraphics.setForegroundColor(amber);
        tGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        tGraphics.fillRectangle(new TerminalPosition(startX, startY), new TerminalSize(w, h), ' ');

        tGraphics.drawLine(startX, startY, startX + w - 1, startY, '═');
        tGraphics.drawLine(startX, startY + h - 1, startX + w - 1, startY + h - 1, '═');
        tGraphics.drawLine(startX, startY, startX, startY + h - 1, '║');
        tGraphics.drawLine(startX + w - 1, startY, startX + w - 1, startY + h - 1, '║');

        tGraphics.setCharacter(startX, startY, '╔');
        tGraphics.setCharacter(startX + w - 1, startY, '╗');
        tGraphics.setCharacter(startX, startY + h - 1, '╚');
        tGraphics.setCharacter(startX + w - 1, startY + h - 1, '╝');
    }

    private void drawHeader(TextGraphics tGraphics, Dvk3DocReader data, int startX, int startY, int w) {
        tGraphics.drawLine(startX, startY + 2, startX + w - 1, startY + 2, '═');
        tGraphics.setCharacter(startX, startY + 2, '╠');
        tGraphics.setCharacter(startX + w - 1, startY + 2, '╣');

        tGraphics.putString(startX + 2, startY + 1, "FILE: " + data.getReadFileName());

        int current = data.getCurrentPage() + 1;
        int total = (data.getTotalLines() + data.getLINES_PER_PAGE() - 1) / data.getLINES_PER_PAGE();
        if (total == 0) total = 1;

        String pages = String.format("[ PG %02d/%02d ]", current, total);
        tGraphics.putString(startX + w - pages.length() - 2, startY + 1, pages);
    }

    private void drawFooter(TextGraphics tGraphics, Dvk3DocReader data, int startX, int startY, int w, int h) {
        String prev = "[◄] PREV";
        String exit = "[ESC] EXIT";
        String next = "[►] NEXT";

        if (data.isTuningMode()) {
            DecimalFormat df = new DecimalFormat("#,##0,000");
            exit = df.format(data.getCurrentFrequency()) + "Hz";
            next = "[ENTER] CONFIRM";
        }

        tGraphics.drawLine(startX, startY + h - 3, startX + w - 1, startY + h - 3, '═');
        tGraphics.setCharacter(startX, startY + h - 3, '╠');
        tGraphics.setCharacter(startX + w - 1, startY + h - 3, '╣');

        if (!data.isTuningMode()) {
            tGraphics.putString(startX + 2, startY + h - 2, prev);
        } else {
            tGraphics.putString(startX + 2, startY + h - 2, "[ESC] CANCEL");
        }

        tGraphics.putString(startX + (w - exit.length()) / 2, startY + h - 2, exit);
        tGraphics.putString(startX + w - next.length() - 2, startY + h - 2, next);
    }

    private void drawContent(TextGraphics tGraphics, Dvk3DocReader data, int startX, int startY, long animTick) {
        List<String> lines = data.getFormattedLines();
        if (lines == null || lines.isEmpty()) return;

        int start = data.getCurrentPage() * data.getLINES_PER_PAGE();
        int end = Math.min(start + data.getLINES_PER_PAGE(), lines.size());

        Random random = new Random();
        long currentTime = System.currentTimeMillis();
        long lastInteractionTime = data.getLastInteractionTime();
        int difference = data.getCurrentFrequency() - data.getIdealFrequency();
        char[] waveChars = {'⋅', '.', '˳', '˳', '.', '⋅', 'ॱ', '˙', '˙', 'ॱ', '⋅'};

        boolean isGlitch = false;
        float intensity = 0f;
        float factor = 0f;

        if (data.getLastKeyInteracted() != null) {
            if (data.getLastKeyInteracted().equals("coarse")) {
                isGlitch = currentTime - lastInteractionTime < 5000;
                factor = 5000f;
            } else if (data.getLastKeyInteracted().equals("fine")) {
                isGlitch = currentTime - lastInteractionTime < FINE_FREQUENCY_DELAY;
                factor = (float) FINE_FREQUENCY_DELAY;
            }
        }

        for (int i = start; i < end; i++) {
            if (isGlitch) {
                long delta = currentTime - lastInteractionTime;
                intensity = 1.0f - (delta / factor);
                if (intensity < 0) intensity = 0;
            }

            String originalLine = lines.get(i);
            StringBuilder glitchedBuilder = new StringBuilder();

            for (int j = 0; j < originalLine.length(); j++) {
                char realChar = originalLine.charAt(j);

                if (realChar == ' ') {
                    glitchedBuilder.append(' ');
                    continue;
                }

                if (isGlitch && random.nextFloat() < intensity) {
                    int waveIndex = (j + (int) animTick) % waveChars.length;
                    glitchedBuilder.append(waveChars[waveIndex]);
                } else {
                    glitchedBuilder.append(realChar);
                }
            }

            int yScreen = startY + 3 + ((i - start) * 2);
            int xBase = startX + 2;
            int offset = 0;

            if (Math.abs(difference) < 2000 && Math.abs(difference) > 20) {
                if (difference > 0) {
                    if (random.nextInt(100) < 40) offset = -1;
                } else {
                    if (random.nextInt(100) < 40) offset = 1;
                }
            }

            if (offset != 0) {
                tGraphics.setForegroundColor(dimColor);
                tGraphics.putString(xBase + (offset * 2), yScreen, glitchedBuilder.toString());
            }

            tGraphics.setForegroundColor(amber);
            tGraphics.putString(xBase + offset, yScreen, glitchedBuilder.toString());
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

    private void drawSignalScope(TextGraphics tGraphics, int x, int y, int width, Dvk3DocReader data) {
        tGraphics.setForegroundColor(amber);
        tGraphics.setBackgroundColor(TextColor.ANSI.BLACK);

        int availableWidth = width - 4;
        String waveForm = getWaveForm(data.getCurrentFrequency(), data.getIdealFrequency(), availableWidth);

        tGraphics.drawLine(x, y, x + width - 1, y, '═');
        tGraphics.drawLine(x, y + 2, x + width - 1, y + 2, '═');

        tGraphics.setCharacter(x, y, '╒');
        tGraphics.setCharacter(x + width - 1, y, '╕');
        tGraphics.setCharacter(x, y + 1, '│');
        tGraphics.setCharacter(x + width - 1, y + 1, '│');
        tGraphics.setCharacter(x, y + 2, '└');
        tGraphics.setCharacter(x + width - 1, y + 2, '┘');

        String title = " SIGNAL INTERCEPT ";
        tGraphics.putString(x + (width - title.length()) / 2, y, title);
        tGraphics.putString(x + 2, y + 1, waveForm);
    }
}