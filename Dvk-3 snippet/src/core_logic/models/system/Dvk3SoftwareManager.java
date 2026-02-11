package core_logic.models.system;

import core_logic.models.utils.CryptoUtils;
import core_logic.models.filesystem.VirtualFile;

import static core_logic.models.rules.Dvk3Config.*;
import static core_logic.models.system.Dvk3SystemLogger.LogType.*;
import static core_logic.models.rules.DigitalManual.*;

public class Dvk3SoftwareManager {
    private final CryptoUtils cryptoUtils = new CryptoUtils();
    private String pendingAction = null;
    private int executionTimer;


    public void scheduleProtocol(Dvk3System system, String command, int time) {

        if (pendingAction != null) {
            system.getLogger().sysLog(ERROR, getError(12), LOG_FAST);
            return;
        }
        boolean protocolScheduled = false;
        if (command.startsWith("STABILIZE")) {
            protocolScheduled = safeDecryptorProtocol(system, command, time);
        }
        if (command.startsWith("CHITAT")) {
            protocolScheduled = chitatProtocol(system, command, time);
        }
        if (protocolScheduled) {
            pendingAction = command;
        }
    }

    public void updateSoftwareState(Dvk3System system) {

        if (executionTimer > 0) {
            executionTimer--;
        } else if (executionTimer == 0 && pendingAction != null) {
            String mainCommand = pendingAction.split(" ")[0];
            String file = pendingAction.split(" ")[1];
            switch (mainCommand) {
                case "STABILIZE":
                    int frequency = Integer.parseInt(pendingAction.split(" ")[2]);
                    system.getCryptoUtils().safeDecryptor(system, file, frequency);
                    break;
                case "CHITAT":
                    Dvk3TaskManager.Task docVisualizerTask = new Dvk3TaskManager.Task("CHITAT_PROTOKOL", TIME_INFINITE);
                    system.getTaskManager().addTask(docVisualizerTask);
                    system.getDocReader().chitatMethod(system, system.getFileManager(), file);
                    break;
            }
            pendingAction = null;
        }
    }

    private boolean chitatProtocol(Dvk3System system, String command, int time) {
        String[] commandParts = command.split("\\s+");
        String fileString = commandParts[1];

        if (system.getFileManager().getCurrentFolder().getFolderByName(fileString) != null) {
            system.getLogger().sysLog(ERROR, getError(34), LOG_FAST);
            return false;
        } else if (system.getFileManager().getCurrentFolder().getFileByName(fileString) == null) {
            system.getLogger().sysLog(ERROR, getError(31), LOG_FAST);
            return false;
        }

        VirtualFile fileTarget = system.getFileManager().getCurrentFolder().getFileByName(fileString);

        system.getLogger().sysLog(INFO, ">>> LOADING BUFFER: " + fileTarget.getFileName(), LOG_INSTANT);
        this.executionTimer = time + system.getHeatingDelay();
        return true;
    }



    private boolean safeDecryptorProtocol(Dvk3System system, String command, int time) {

        String[] commandParts = command.split("\\s+");
        String fileString = commandParts[1];
        int newFrequency;
        try {
            newFrequency = Integer.parseInt(commandParts[2]);
            while (newFrequency < 1000 && newFrequency > 0) {
                newFrequency *= 10;
            }
        } catch (NumberFormatException e) {
            system.getLogger().sysLog(ERROR, getError(5), LOG_FAST);
            return false;
        }
        if (newFrequency < 4000 || newFrequency > 16000) {
            system.getLogger().sysLog(ERROR, getError(6), LOG_FAST);
            return false;
        }
        if (system.getFileManager().getCurrentFolder().getFileByName(fileString) == null) {
            system.getLogger().sysLog(ERROR, getError(31), LOG_FAST);
            return false;
        }
        VirtualFile currentFile = system.getFileManager().getCurrentFolder().getFileByName(fileString);

        if (currentFile.getCurrentFrequency() == newFrequency) {
            system.getLogger().sysLog(ERROR, getError(87), LOG_FAST);
            return false;
        }

        String display = String.format(java.util.Locale.GERMANY, "%,d", newFrequency);
        system.getLogger().sysLog(INFO, "... SYNCHRONIZING WAVEFORM TO " + display + "Hz ...", LOG_FAST);
        Dvk3TaskManager.Task safeTask = new Dvk3TaskManager.Task("SAFE_DECRYPTOR", TIME_FAST);
        system.getTaskManager().addTask(safeTask);
        this.executionTimer = time + system.getHeatingDelay();
        return true;
    }
}
