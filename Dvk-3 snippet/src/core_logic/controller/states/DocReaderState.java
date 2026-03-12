package core_logic.controller.states;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import core_logic.controller.GameController;
import core_logic.models.*;
import core_logic.models.system.Dvk3System;

public class DocReaderState implements SystemState {

    @Override
    public void handleInput(Dvk3System system, KeyStroke key, GameController controller, Screen screen) throws Exception {

        if (key.getKeyType() == KeyType.ArrowLeft) {
            system.getDocReader().returnPage();
        }
        else if (key.getKeyType() == KeyType.ArrowRight) {
            system.getDocReader().nextPage();
        }
        else if (key.getKeyType() == KeyType.Escape) {
            system.getDocReader().closeReadMode(system);

//            controller.changeState(new TerminalState());
        }
    }
}
