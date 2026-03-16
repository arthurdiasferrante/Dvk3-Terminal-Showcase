package core_logic.models;

import java.util.Random;
import static core_logic.models.rules.Dvk3Config.*;

public class BunkerState {

    private double bunkerTemperature = 20;
    private final Random random = new Random();
    private final int NATURAL_BUNKER_TEMPERATURE = 5;


    public void processTick() {
        temperatureOscillation();

    }

    private void temperatureOscillation() {
        if (bunkerTemperature > NATURAL_BUNKER_TEMPERATURE + 2) {
            bunkerTemperature -= random.nextDouble() * 0.2;
        } else if (bunkerTemperature > NATURAL_BUNKER_TEMPERATURE) {
            bunkerTemperature += random.nextDouble() * 0.2;
        }
    }


    public double getBunkerTemperature() {
        return bunkerTemperature;
    }

}

