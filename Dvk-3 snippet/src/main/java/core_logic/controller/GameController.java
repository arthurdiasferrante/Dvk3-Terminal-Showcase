package core_logic.controller;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import core_logic.controller.states.DocDecryptingState;
import core_logic.controller.states.DocReaderState;
import core_logic.controller.states.SystemState;
import core_logic.controller.states.TerminalState;
import core_logic.models.BunkerState;
import static core_logic.models.rules.Dvk3Config.*;

import core_logic.models.physical.Dvk3Core;
import core_logic.views.BootSequence;
import core_logic.views.DocumentWindowViewer;
import core_logic.views.TerminalViewer;
import core_logic.models.system.Dvk3System;

import java.io.IOException;
import java.time.LocalDateTime;

public class GameController {
    private long lastTickAnimation = System.currentTimeMillis();
    private Screen screen;
    private Dvk3System dvk3System;
    private Dvk3Core dvk3Core;
    private ProcessCommands processCommands;
    private TerminalViewer viewer;
    private DocumentWindowViewer docWindowView;
    private BunkerState bunkerState;
    private String historyCommand;
    private int commandIndex = 0;
    private LocalDateTime realHourTime;
    private TerminalState terminalState;
    private DocReaderState docReaderState;
    private SystemState currentState;

    public GameController(Screen screen) {
        this.screen = screen;
        this.bunkerState = new BunkerState();
        this.dvk3System = new Dvk3System();
        this.dvk3Core = new Dvk3Core(30);
        this.processCommands = new ProcessCommands();
        this.viewer = new TerminalViewer(screen);
        this.docWindowView = new DocumentWindowViewer();
        this.terminalState = new TerminalState();
        this.docReaderState = new DocReaderState();
        this.currentState = terminalState;
    }

    public void processTick() {
        dvk3Core.processTick(dvk3System, bunkerState);
        dvk3System.processTick(dvk3Core);
        bunkerState.processTick();
    }

    public void changeState(SystemState newState) {
        this.currentState = newState;
    }


    public void startGame() throws Exception {
        BootSequence bootSequence = new BootSequence(screen);
        bootSequence.execute();
        flushInput();

        resetTimers();
        dvk3System.greetings();

        if (!dvk3System.getLogger().hasTyped()) {
            dvk3System.getInputBuffer().append("Type HELP for useful commands!");
        }
        long lastTick = System.currentTimeMillis();
        long animTick = 0;

        try {
            while (true) {

                screen.clear();

                if (dvk3System.isOn()) {
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
                    this.realHourTime = LocalDateTime.now();
                    dvk3System.processQueue();
                    dvk3System.getDocReader().processTick();
                    dvk3System.getLogger().processTick(realHourTime);
                    viewer.processTick(realHourTime);
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

            // ESC
            if (key.getKeyType() == KeyType.Escape) {
                // Quando lendo um documento, sai dele
                if (dvk3System.isSafeHalt()) {
                    dvk3Core.turnOff(dvk3System);
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

            if (dvk3System.getDocReader().isOpen()) {
                if (dvk3System.getDocReader().isTuningMode()) {
                    changeState(new DocDecryptingState());
                } else {
                    changeState(new DocReaderState());
                }
            }
            currentState.handleInput(dvk3System, key, this, screen);
        }
    }



    // ------ GETTERS, SETTERS ------


    public Dvk3Core getDvk3Core() {
        return dvk3Core;
    }

    public BunkerState getBunkerState() {
        return bunkerState;
    }

    public ProcessCommands getProcessCommands() {
        return processCommands;
    }

    public int getCommandIndex() {
        return commandIndex;
    }

    public void setCommandIndex(int commandIndex) {
        this.commandIndex = commandIndex;
    }
}