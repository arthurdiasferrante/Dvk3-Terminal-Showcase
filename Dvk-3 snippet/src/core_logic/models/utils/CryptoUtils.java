package core_logic.models.utils;

import core_logic.models.filesystem.VirtualFile;
import core_logic.models.system.Dvk3System;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import static core_logic.models.system.Dvk3SystemLogger.LogType.*;
import static core_logic.models.rules.Dvk3Config.*;


public class CryptoUtils {
    private final Random random = new Random();
    private final int MAX_RANGE = 2000;
    private final int TOLERANCE = 40;


    public static enum EncryptionType {
        REDACT,
        SAFE,
        HEX
    }



    public String encryptContent(VirtualFile file) {
        String currentText = file.getFileContent();
        if (file.getEncryptionLayers() == null) {
            return currentText;
        }

        int currentFrequency = file.getCurrentFrequency();
        int idealFrequency = file.getIDEAL_FREQUENCY();

        if (idealFrequency != currentFrequency) {
            file.setSafeCrypted(true);
            file.addEncryption(EncryptionType.SAFE);

        }

        List<EncryptionType> layers = file.getEncryptionLayers();

        for (EncryptionType layer : layers) {
            if (layer.equals(EncryptionType.SAFE)) {
                currentText = safeAlgorithm(currentText, file.getCurrentFrequency(), file.getIDEAL_FREQUENCY());
            }
        }
        return currentText;
    }

    public void safeDecryptor(Dvk3System system, String file, int frequencyInput) {

        VirtualFile currentFile = system.getFileManager().getCurrentFolder().getFileByName(file);
        int idealFrequency = currentFile.getIDEAL_FREQUENCY();
        currentFile.setCurrentFrequency(frequencyInput);

        if (!currentFile.getEncryptionLayers().contains(EncryptionType.SAFE)) {
            if (idealFrequency != frequencyInput) {
                currentFile.setSafeCrypted(true);
                currentFile.addEncryption(EncryptionType.SAFE);
                system.getLogger().sysLog(WARN, "SIGNAL WEAK. RE-CALIBRATION REQUIRED.");
            }
            return;
        }

        int distance = idealFrequency - frequencyInput;
        distance = Math.abs(distance);
        if (distance < TOLERANCE && currentFile.isSafeCrypted()) {
            system.getLogger().sysLog(SUCCESS, "INTEGRITY VERIFIED. 0% PACKET LOSS.");
            currentFile.setCurrentFrequency(idealFrequency);
            currentFile.setSafeCrypted(false);
            currentFile.removeEncryption(EncryptionType.SAFE);
        } else {
            system.getLogger().sysLog(WARN, "SIGNAL WEAK. RE-CALIBRATION REQUIRED.", LOG_FAST);
        }
    }

    // SECURE AND FAST ENCRYPTION TYPE

    public String safeAlgorithm(String content, int currentFrequency, int idealFrequency) {

        Random seedRandom = new Random(idealFrequency);
        Random random = new Random();
        int distance = idealFrequency - currentFrequency;
        distance = Math.abs(distance);
        double noiseChance = 1;

        if (distance <= MAX_RANGE) {
            noiseChance = (double) distance / MAX_RANGE;
        }

        StringBuilder safeContent = new StringBuilder();
        char[] messyChars = {
                '!','@','#','&','¨','%','*','-','_','=','?',
                ';','/','+','"','~','^','´', '⊖', '≸','ↅ','⁂',
                'ɇ','⎷','⎃','⋙','≶','⌱','⎇','⌬','⌭','█'
        };

        for (char c : content.toCharArray()) {
            if (c == ' ' || c == '\n') {
                safeContent.append(c);
            } else {
                if (seedRandom.nextDouble() < noiseChance) {
                    char randomChar = messyChars[random.nextInt(messyChars.length)];
                    safeContent.append(randomChar);
                } else {
                    safeContent.append(c);
                }
            }
        }
        return safeContent.toString();
    }

}
