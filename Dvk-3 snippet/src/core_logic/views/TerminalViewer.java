package core_logic.views;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import javax.swing.JFrame;
import core_logic.models.BunkerState;
import core_logic.models.system.Dvk3System;
import core_logic.models.physical.Dvk3Core;
import core_logic.models.system.Dvk3SystemLogger;

import java.util.Random;

public class TerminalViewer {
    private Screen screen;
    private TextGraphics tGraphics;
    private Random random;

    public TerminalViewer(Screen screen) {
        this.screen = screen;
        this.tGraphics = screen.newTextGraphics();
        this.random = new Random();

        if (screen instanceof TerminalScreen) {
            Terminal terminal = ((TerminalScreen) screen).getTerminal();

            if (terminal instanceof SwingTerminalFrame) {
                ((SwingTerminalFrame) terminal).setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        }
    }

    public void draw(BunkerState bunkerState, Dvk3System system, Dvk3Core core, DocumentWindowViewer docWindowView, long animTick) throws Exception {

        TerminalSize size = screen.doResizeIfNecessary();
        if (size == null) size = screen.getTerminalSize();

        screen.setCursorPosition(null);
        tGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        // Cor padrão do sistema (âmbar/laranja) em vez de dinâmica
        tGraphics.setForegroundColor(new TextColor.RGB(255, 176, 0));
        tGraphics.fillRectangle(new com.googlecode.lanterna.TerminalPosition(0, 0), size, ' ');

        tGraphics.drawLine(0, 0, size.getColumns() - 1, 0, '#');

        header(size, core, system);
        showMessages(system, size, animTick);

        // DOC READER AQUI EM BAIXO
        if (system.getDocReader().isOpen()) {
            docWindowView.draw(system, core, screen, tGraphics, animTick, size);
        }

        screen.refresh();
    }

    public void header(TerminalSize size, Dvk3Core core, Dvk3System system) {

        // borda superior
        String topBorder = "╔" + "═".repeat(size.getColumns() - 2) + "╗";
        tGraphics.putString(0, 0, topBorder);

        // laterais e infos
        tGraphics.putString(0, 1, "║");
        tGraphics.putString(2, 1, "SYS: MIR-11 [ONLINE]");

        tGraphics.putString(25, 1, "MEM: 64KB OK");
        tGraphics.putString(40, 1, "UID: SN-0373");

        // Removida temperatura pois core não processa mais isso na demo
        // Se quiser manter texto estático:
        // tGraphics.putString(size.getColumns() - 20, 1, "CORE: STABLE");

        // borda direita
        tGraphics.putString(size.getColumns() - 1, 1, "║");

        // borda inferior
        String bottomBorder = "╚" + "═".repeat(size.getColumns() - 2) + "╝";
        tGraphics.putString(0, 2, bottomBorder);
    }

    public void showMessages(Dvk3System dvk3System, TerminalSize size, long animTick) {
        int yPrompt = size.getRows() - 2;

        // Retorna para a cor padrão do texto
        tGraphics.setForegroundColor(new TextColor.RGB(255, 176, 0));

        boolean isCursorVisible;
        long currentTime = System.currentTimeMillis();
        long timeSinceLastType = currentTime - dvk3System.getLastTypingTime();

        if (timeSinceLastType < 800) {
            isCursorVisible = true;
        } else {
            isCursorVisible = (animTick / 5) % 2 == 0;
        }

        String prompt = "[SN-0373@bunker ~]$ ";
        String cursorChar = isCursorVisible ? "█" : " ";
        String promptTyping = dvk3System.inputBuffer.toString();

        tGraphics.putString(2, yPrompt, prompt + promptTyping + cursorChar);

        java.util.List<Dvk3SystemLogger.LogEntry> history = dvk3System.getLogger().getHistory();
        int totalMessages = history.size();
        long now = System.currentTimeMillis();
        int charSpeedMs = 30;

        for (int i = 0; i < totalMessages; i++) {
            Dvk3SystemLogger.LogEntry entry = history.get(totalMessages - 1 - i);
            int yPos = yPrompt - 1 - i;

            if (yPos > 2) {
                String textToDraw;
                String prefix = entry.getPrefix();
                String message = entry.getMessage();

                if (entry.isInstant()) {
                    textToDraw = prefix + message;
                } else {
                    textToDraw = prefix;
                    long timeAlive = now - entry.getTimestamp();
                    int charsVisible = (int) (timeAlive / charSpeedMs);
                    if (charsVisible > message.length()) charsVisible = message.length();
                    textToDraw += message.substring(0, charsVisible);
                }

                tGraphics.putString(2, yPos, textToDraw);
            }
        }
    }
}