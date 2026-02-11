package core_logic.models.physical;


import core_logic.models.BunkerState;
import core_logic.models.system.Dvk3System;

import java.util.Random;

public class Dvk3Core {

    private double currentTemperature;
    private boolean standBy = false;
    private final double TEMPERATURE_ESCAPE_FACTOR = 0.05;
    private final Random random = new Random();

    public Dvk3Core(int currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public void processTick(Dvk3System system, BunkerState state) {
        double ambientTemp = state.getBunkerTemperature();

        if (!system.isOn()) {
            processCooldown(ambientTemp);
        } else if (standBy) {
            processStandBy(ambientTemp);
        } else {
            processIdle(ambientTemp);
        }
    }

    private void processCooldown(double ambientTemp) {
        double distance = (currentTemperature - ambientTemp);
        double temperatureFactor = distance * TEMPERATURE_ESCAPE_FACTOR;
        currentTemperature -= temperatureFactor;
    }

    private void processStandBy(double ambientTemp) {
        double distance = (currentTemperature - ambientTemp);
        double temperatureFactor = distance * TEMPERATURE_ESCAPE_FACTOR;
        double standByHeatGeneration = 15.0 * TEMPERATURE_ESCAPE_FACTOR;
        double jitter = (random.nextDouble() * 0.2) - 0.1;
        currentTemperature = currentTemperature - temperatureFactor + standByHeatGeneration + jitter;
    }


    private void processIdle(double ambientTemp) {
        double distance = (currentTemperature - ambientTemp);
        double temperatureFactor = distance * TEMPERATURE_ESCAPE_FACTOR;
        double standByHeatGeneration = 22.0 * TEMPERATURE_ESCAPE_FACTOR;
        double jitter = (random.nextDouble()) - 0.5;
        currentTemperature = currentTemperature - temperatureFactor + standByHeatGeneration + jitter;
    }

    public void turnOff(Object genericDevice) {
        if (genericDevice instanceof Dvk3System) {
            ((Dvk3System) genericDevice).killEverything(this);
            ((Dvk3System) genericDevice).setIsOn(false);
        }
    }

    public void turnOn(Object genericDevice) {
        if (genericDevice instanceof Dvk3System) {
            ((Dvk3System) genericDevice).setIsOn(true);
        }
    }


    public void setStandBy(boolean bool) {
        standBy = bool;
    }

    public boolean getStandBy() {
        return standBy;
    }

}
