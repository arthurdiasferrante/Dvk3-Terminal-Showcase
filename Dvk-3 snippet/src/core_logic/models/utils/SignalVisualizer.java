package core_logic.models.utils;

public class SignalVisualizer {

    public static String getWaveForm(int current, int ideal, int width) {
        int barSize = width;
        StringBuilder wave = new StringBuilder(barSize);
        int distance = Math.abs(current - ideal);

        double strength = 4.0 - (4.0 * (distance - 49) / 1949.0);
        int clampedStrength = (int) Math.round(Math.max(0, Math.min(4, strength)));

        char[] waveBar = {
                '▁', '▂', '▃', '▅', '▇'
        };

        if (strength <= 0) {
            return String.valueOf(waveBar[0]).repeat(width);
        } else if (strength >= 4) {
            return String.valueOf(waveBar[4]).repeat(width);
        }

        for (int i = 0; i < barSize; i++) {
            int r = (int)(Math.random() * 3) - 1;
            int strengthJitter = clampedStrength + r;
            int finalJitter = Math.max(0, Math.min(4, strengthJitter));
            wave.append(waveBar[finalJitter]);
        }

        return wave.toString();
    }
}

