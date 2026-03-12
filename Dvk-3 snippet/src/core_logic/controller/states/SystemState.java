package core_logic.controller.states;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import core_logic.controller.GameController;
import core_logic.models.*;
import core_logic.models.system.Dvk3System;

public interface SystemState {

    void handleInput(Dvk3System dvk3System, KeyStroke key, GameController controller, Screen screen) throws Exception;
}
