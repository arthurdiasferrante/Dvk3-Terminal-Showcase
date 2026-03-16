package core_logic;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import core_logic.controller.GameController;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class Main {

    public static void main(String[] args) {
        try {
            Screen screen = initializeSystemScreen();
            screen.startScreen();

            GameController game = new GameController(screen);
            game.startGame();

            screen.stopScreen();

        } catch (Exception e) {
            System.err.println("CRITICAL SYSTEM FAILURE: DVK-3 boot sequence aborted.");
            e.printStackTrace();
        }
    }

    private static Screen initializeSystemScreen() throws IOException, FontFormatException {
        DefaultTerminalFactory factory = new DefaultTerminalFactory();

        Font customFont = loadCustomFont("Glass_TTY_VT220.ttf", 23f);
        Font fontBackup = new Font("Monospaced", Font.PLAIN, 20);

        SwingTerminalFontConfiguration fontConfig = SwingTerminalFontConfiguration.newInstance(customFont, fontBackup);

        factory.setTerminalEmulatorFontConfiguration(fontConfig);
        factory.setTerminalEmulatorTitle("DVK-3");

        Terminal terminal = factory.createTerminalEmulator();
        return new TerminalScreen(terminal);
    }

    private static Font loadCustomFont(String fileName, float size) throws IOException, FontFormatException {
        InputStream fontStream = Main.class.getClassLoader().getResourceAsStream(fileName);

        if (fontStream == null) {
            throw new IOException("Arquivo de fonte não encontrado: " + fileName);
        }

        Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
        return font.deriveFont(size);
    }
}