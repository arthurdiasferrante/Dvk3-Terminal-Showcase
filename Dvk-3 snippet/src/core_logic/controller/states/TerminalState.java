package core_logic.controller.states;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import core_logic.controller.GameController;
import core_logic.controller.ProcessCommands;
import core_logic.models.BunkerState;
import core_logic.models.physical.Dvk3Core;
import core_logic.models.system.Dvk3System;

import java.io.IOException;

public class TerminalState implements SystemState {

    private void flushInput(Screen screen) throws IOException {
        while (screen.pollInput() != null) {
            // limpa buffer de teclas
        }
    }

    @Override
    public void handleInput(Dvk3System dvk3System, KeyStroke key, GameController controller, Screen screen) throws Exception {

        if (key.getKeyType() == KeyType.Character) {
            if (!dvk3System.getLogger().hasTyped()) {
                dvk3System.getLogger().triggerTyped();
                dvk3System.inputBuffer.setLength(0);
            }

            // essa é uma proteção contra ghosting (tecla repetida muito rápido)
            KeyStroke spyKey = screen.pollInput();
            if (spyKey != null) {
                flushInput(screen);
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

        else if (key.getKeyType() == KeyType.ArrowUp || key.getKeyType() == KeyType.ArrowDown) {
            historyMessagesNavigator(dvk3System, controller, key);
        }

        else if (key.getKeyType() == KeyType.Backspace) {
            if (!dvk3System.getLogger().hasTyped()) {
                return;
            }
            if (!dvk3System.inputBuffer.isEmpty()) {
                dvk3System.inputBuffer.deleteCharAt(dvk3System.inputBuffer.length() - 1);
            }
        }
        else if (key.getKeyType() == KeyType.Enter) {
            if (!dvk3System.getLogger().hasTyped() || dvk3System.inputBuffer.isEmpty()) {
                return;
            }
            processNewCommands(dvk3System, controller);
        }
    }

    private void historyMessagesNavigator(Dvk3System dvk3System, GameController controller, KeyStroke key) {
        int commandIndex = controller.getCommandIndex();

        int historySize = dvk3System.getCommandHistoryMessageSize();
        if (key.getKeyType() == KeyType.ArrowUp) {
            if (commandIndex > 0) {
                commandIndex--;
                controller.setCommandIndex(commandIndex);
            } else {
                controller.setCommandIndex(historySize);
            }
            dvk3System.inputBuffer.setLength(0);
            if (commandIndex != historySize) {
                String historyCommand = dvk3System.getCommandHistoryMessage(commandIndex);
                dvk3System.inputBuffer.append(historyCommand);
            }
        } else if (key.getKeyType() == KeyType.ArrowDown) {
            if (commandIndex < historySize) {
                commandIndex++;
                controller.setCommandIndex(commandIndex);
            } else {
                controller.setCommandIndex(0);
            }

            dvk3System.inputBuffer.setLength(0);

            if (commandIndex != historySize) {
                String historyCommand = dvk3System.getCommandHistoryMessage(commandIndex);
                dvk3System.inputBuffer.append(historyCommand);
            }

        } else {
            controller.setCommandIndex(historySize);
        }
    }

    private void processNewCommands(Dvk3System dvk3System, GameController controller) throws InterruptedException {
        ProcessCommands processCommands = controller.getProcessCommands();
        Dvk3Core dvk3Core = controller.getDvk3Core();
        BunkerState bunkerState = controller.getBunkerState();

        String finalCommand = dvk3System.inputBuffer.toString();
        dvk3System.getLogger().userLog(finalCommand, dvk3System.getFormattedHour());
        dvk3System.addCommandToHistory(finalCommand);
        controller.setCommandIndex(dvk3System.getCommandHistoryMessageSize());
        Thread.sleep(50);
        processCommands.executeCommand(finalCommand, dvk3System, dvk3Core, bunkerState);
        dvk3System.inputBuffer.setLength(0);
    }
}
