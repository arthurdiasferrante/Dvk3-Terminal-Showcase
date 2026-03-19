package core_logic.views;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import java.util.Random;

public class BootSequence {
    private Screen screen;
    private TextGraphics tGraphics;
    private final Random random = new Random();

    private final TextColor AMBER = new TextColor.RGB(255, 176, 0);

    public BootSequence(Screen screen) {
        this.screen = screen;
        this.tGraphics = screen.newTextGraphics();
    }

    public void execute() throws Exception {
        TerminalSize size = screen.getTerminalSize();


        tGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        tGraphics.setForegroundColor(AMBER);
        tGraphics.fillRectangle(new com.googlecode.lanterna.TerminalPosition(0,0), size, ' ');
        screen.setCursorPosition(null);
        screen.refresh();

        Thread.sleep(500);

        int line = 1;
        printSlowly(2, line++, "DVK-3 ROM BIOS v1.0a (c) 198X ACADEMY OF SCIENCES USSR", 10);
        line++;


        screen.refresh();
        Thread.sleep(800);
        line++;

        runMemoryTest(2, line++);
        line++;


        String[] hardwareSteps = {
                "ЦП: К1801ВМ1 ОБНАРУЖЕН..... [ОК]",
                "СОПРОЦЕССОР: ОТСУТСТВУЕТ... [НЕТ]",
                "ШИНА: СИСТЕМА МПИ.......... [ОК]",
                "ВИДЕО: КСМ-01 МОНОХРОМ..... [ОК]",
                "ДИСК А: СМОНТИРОВАН (ТОЛЬКО ЧТЕНИЕ)",
                "ДИСК Б: НЕ ОБНАРУЖЕН",
                "--------------------------------",
                "ПЕРВИЧНЫЙ ЗАГРУЗЧИК........ [ОК]",
                "ЗАГРУЗКА ЯДРА МИР-11....... [ОК]",
                "МОНТИРОВАНИЕ ФС (ЦСН)...... [ОК]",
        };

        for (String step : hardwareSteps) {
            tGraphics.putString(2, line, step);
            screen.refresh();

            Thread.sleep(random.nextInt(150) + 50);

            if (random.nextInt(10) > 7) Thread.sleep(300);

            line++;
        }

        line++;

        animatedLoadingBar(2, line, 40);

        line += 2;
        systemReadyText(2, line, "СИСТЕМА ГОТОВА", 3);

        Thread.sleep(1200);
    }

    private void runMemoryTest(int x, int y) throws Exception {
        String prefix = "MEMORY CHECK: ";
        tGraphics.putString(x, y, prefix);

        for (int i = 0; i <= 64; i += 4) {
            String memStatus = i + " KB OK";
            tGraphics.putString(x + prefix.length(), y, memStatus);
            screen.refresh();
            Thread.sleep(30);
        }
        Thread.sleep(400);
    }

    private void printSlowly(int x, int y, String text, int delay) throws Exception {
        for (int i = 0; i < text.length(); i++) {
            tGraphics.putString(x + i, y, String.valueOf(text.charAt(i)));
            screen.refresh();
            Thread.sleep(delay);
        }
    }

    private void animatedLoadingBar(int x, int y, int largura) throws Exception {
        String title = "ЗАГРУЗКА...";
        tGraphics.putString(x, y - 1, title);

        char fullBlock = '█';
        char emptyBlock = '░';

        for (int i = 0; i <= 100; i += random.nextInt(4) + 1) {
            if (i > 100) i = 100;

            int fullBlocks = (i * largura) / 100;

            StringBuilder bar = new StringBuilder();
            bar.append("[");
            for (int b = 0; b < fullBlocks; b++) {
                bar.append(fullBlock);
            }
            for (int b = fullBlocks; b < largura; b++) {
                bar.append(emptyBlock);
            }
            bar.append("] ").append(i).append("%");

            tGraphics.putString(x, y, bar.toString());
            screen.refresh();


            if (i < 30) {
                Thread.sleep(10);
            } else if (i > 80 && i < 90) {
                Thread.sleep(150);
            } else if (i == 99) {
                Thread.sleep(800);
            } else {
                Thread.sleep(30);
            }
        }
    }

    private void systemReadyText(int x, int y, String text, int vezes) throws Exception {
        for (int i = 0; i < vezes; i++) {
            tGraphics.putString(x, y, text);
            screen.refresh();
            Thread.sleep(500);

            tGraphics.putString(x, y, " ".repeat(text.length()));
            screen.refresh();
            Thread.sleep(300);
        }
        tGraphics.putString(x, y, text);
        screen.refresh();
    }

}