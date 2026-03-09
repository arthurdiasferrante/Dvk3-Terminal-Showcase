package core_logic.controller;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import core_logic.models.BunkerState;
import static core_logic.models.rules.Dvk3Config.*;

import core_logic.models.physical.Dvk3Core;
import core_logic.views.BootSequence;
import core_logic.views.DocumentWindowViewer;
import core_logic.views.TerminalViewer;
import core_logic.models.system.Dvk3System;

import java.io.IOException;
import java.security.Key;

public class GameController {
    private long lastTickAnimation = System.currentTimeMillis();
    private Screen screen;
    private Dvk3System dvk3System;
    private Dvk3Core dvk3Core;
    private ProcessCommands processComands;
    private TerminalViewer viewer;
    private DocumentWindowViewer docWindowView;
    private BunkerState bunkerState;
    private String historyCommand;
    private int commandIndex = 0;

    public GameController(Screen screen) {
        this.screen = screen;
        this.bunkerState = new BunkerState();
        this.dvk3System = new Dvk3System();
        this.dvk3Core = new Dvk3Core(30);
        this.processComands = new ProcessCommands();
        this.viewer = new TerminalViewer(screen);
        this.docWindowView = new DocumentWindowViewer();
    }

    public void processTick() {
        dvk3Core.processTick(dvk3System, bunkerState);
        dvk3System.processTick(dvk3Core);
        bunkerState.processTick();
    }

    public void startGame() throws Exception {
        BootSequence bootSequence = new BootSequence(screen);
        bootSequence.execute();
        flushInput();

        resetTimers();
        long lastTick = System.currentTimeMillis();
        long animTick = 0;

        try {
            while (true) {

                screen.clear();

                if (!dvk3System.isOn()) {
                } else {
                    viewer.draw(bunkerState, dvk3System, dvk3Core, docWindowView, animTick);
                }

                screen.refresh();
                long currentTime = System.currentTimeMillis();
                processKey();

                if (currentTime - lastTick > GAME_TICK_SPEED) {
                    processTick();
                    lastTick = currentTime;
                }
                if (currentTime - lastTickAnimation > ANIMATION_FRAME_DELAY) {
                    dvk3System.processQueue();
                    dvk3System.getDocReader().processTick();
                    animTick++;
                    lastTickAnimation = currentTime;
                }
                Thread.sleep(20);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void resetTimers() {
        this.lastTickAnimation = System.currentTimeMillis();
    }

    private void flushInput() throws IOException {
        while (screen.pollInput() != null) {
            // Limpa buffer de teclas
        }
    }

    public void processKey() throws Exception {
        KeyStroke key = screen.pollInput();

        if (key != null){
            if (dvk3Core.getStandBy()) {
                dvk3Core.setStandBy(false);
                return;
            }
        }

        // Se o logger estiver escrevendo, ignora input
        if (dvk3System.getLogger().isBusy()) {
            return;
        }

        if (key != null) {
            dvk3System.notifyTyping();

            // Lógica do ESC (Ligar/Desligar/Sair do Documento)
            if (key.getKeyType() == KeyType.Escape) {
                // Se estiver a ler documento, sai dele
                if (dvk3System.getDocReader().isOpen()) {
                    dvk3System.getDocReader().setOpen(false);
                    dvk3System.getTaskManager().killTaskByName("CHITAT_PROTOKOL");
                    return;
                }
                if (!dvk3System.isOn()) {
                    BootSequence boot = new BootSequence(screen);
                    boot.execute();
                    flushInput();
                    dvk3Core.turnOn(dvk3System);
                    resetTimers();
                    return;
                }
            }

            if (!dvk3System.isOn()) {
                return;
            }

            // --- LÓGICA DE CONTROLE DO LEITOR DE DOCUMENTOS (READ) ---
            if (dvk3System.getDocReader().isOpen()) {
                if (key.getKeyType() == KeyType.ArrowLeft) {
                    dvk3System.getDocReader().returnPage();
                }

                if (key.getKeyType() == KeyType.ArrowRight) {
                    dvk3System.getDocReader().nextPage();
                }
            }

            // -- ACESSANDO HISTÓRICO DE MENSAGENS DO TERMINAL --
            int historySize = dvk3System.getCommandHistoryMessageSize();
            if (key.getKeyType() == KeyType.ArrowUp) {
                if (commandIndex > 0) {
                    commandIndex--;
                } else {
                    commandIndex = historySize;
                }
                dvk3System.inputBuffer.setLength(0);
                if (commandIndex != historySize) {
                    String historyCommand = dvk3System.getCommandHistoryMessage(commandIndex);
                    dvk3System.inputBuffer.append(historyCommand);
                }
            } else if (key.getKeyType() == KeyType.ArrowDown) {
                if (commandIndex < historySize) {
                    commandIndex++;
                } else {
                    commandIndex = 0;
                }

                dvk3System.inputBuffer.setLength(0);

                if (commandIndex != historySize) {
                    String historyCommand = dvk3System.getCommandHistoryMessage(commandIndex);
                    dvk3System.inputBuffer.append(historyCommand);
                }

            } else {
                commandIndex = historySize;
            }

            // Se o documento estiver aberto, ignora digitação no terminal
            if (dvk3System.getDocReader().isOpen()) {
                return;
            }

            // --- LÓGICA DE DIGITAÇÃO NO TERMINAL ---
            else if (key.getKeyType() == KeyType.Character) {
                // Proteção contra ghosting (tecla repetida muito rápido)
                KeyStroke spyKey = screen.pollInput();
                if (spyKey != null) {
                    flushInput();
                    return;
                }

                char c = key.getCharacter();
                if (c >= 32 && c < 127) {
                    if (!Character.isISOControl(c)) {
                        if (dvk3System.inputBuffer.length() < 50) {
                            dvk3System.inputBuffer.append(key.getCharacter());
                        }
                    }
                }
            }
            else if (key.getKeyType() == KeyType.Backspace) {
                if (!dvk3System.inputBuffer.isEmpty()) {
                    dvk3System.inputBuffer.deleteCharAt(dvk3System.inputBuffer.length() - 1);
                }
            }
            else if (key.getKeyType() == KeyType.Enter) {
                String finalCommand = dvk3System.inputBuffer.toString();
                dvk3System.getLogger().userLog(finalCommand, dvk3System.getFormattedHour());
                dvk3System.addCommandToHistory(finalCommand);
                commandIndex = dvk3System.getCommandHistoryMessageSize();
                Thread.sleep(50);
                processComands.executeCommand(finalCommand, dvk3System, dvk3Core, bunkerState);
                dvk3System.inputBuffer.setLength(0);
            }
        }
    }
}