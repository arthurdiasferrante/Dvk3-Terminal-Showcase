package core_logic.controller.states;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import core_logic.controller.GameController;
import core_logic.models.system.Dvk3DocReader;
import core_logic.models.system.Dvk3System;
import static core_logic.models.rules.Dvk3Config.*;

public class DocDecryptingState implements SystemState {

    @Override
    public void handleInput(Dvk3System dvk3System, KeyStroke key, GameController controller, Screen screen) throws Exception {
        String fileName = dvk3System.getDocReader().getOpenedFile().getFileName();

        long currentTime = System.currentTimeMillis();
        Dvk3DocReader docReader = dvk3System.getDocReader();
        boolean idle = true;
        boolean keyWorked;

        if (docReader.getLastKeyInteracted().equals("coarse")) {
            idle = currentTime - docReader.getLastInteractionTime() > COARSE_FREQUENCY_DELAY;
        } else if (docReader.getLastKeyInteracted().equals("fine")) {
            idle = currentTime - docReader.getLastInteractionTime() > FINE_FREQUENCY_DELAY;
        }

        if (key.getKeyType() == KeyType.ArrowLeft && idle) {
            docReader.notifyInteractor(System.currentTimeMillis());
            if (key.isShiftDown()) {
                keyWorked = dvk3System.getCryptoUtils().manualSafeTuning(dvk3System, fileName, false, false);
                if (keyWorked) docReader.setLastKeyInteracted("fine");
                else docReader.setLastKeyInteracted("");
            } else {
                keyWorked = dvk3System.getCryptoUtils().manualSafeTuning(dvk3System, fileName, true, false);
                if (keyWorked) dvk3System.getDocReader().setLastKeyInteracted("coarse");
                else docReader.setLastKeyInteracted("");
            }
        }
        if (key.getKeyType() == KeyType.ArrowRight & idle) {
            docReader.notifyInteractor(System.currentTimeMillis());
            if (key.isShiftDown()) {
                keyWorked = dvk3System.getCryptoUtils().manualSafeTuning(dvk3System, fileName, false, true);
                if (keyWorked) docReader.setLastKeyInteracted("fine");
                else docReader.setLastKeyInteracted("");
            } else {
                keyWorked = dvk3System.getCryptoUtils().manualSafeTuning(dvk3System, fileName, true, true);
                if (keyWorked) dvk3System.getDocReader().setLastKeyInteracted("coarse");
                else docReader.setLastKeyInteracted("");
            }
        }
    }
}
