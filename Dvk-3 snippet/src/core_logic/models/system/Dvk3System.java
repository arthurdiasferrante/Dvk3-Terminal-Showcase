package core_logic.models.system;

import core_logic.models.utils.CryptoUtils;
import core_logic.models.filesystem.Dvk3FileManager;
import core_logic.models.physical.Dvk3Core;

import java.util.ArrayList;
import java.util.List;

import static core_logic.models.rules.Dvk3Config.*;

public class Dvk3System {
    // Gerenciadores Essenciais
    private final Dvk3SystemLogger logger = new Dvk3SystemLogger();
    private final Dvk3TaskManager taskManager = new Dvk3TaskManager();
    private final Dvk3SoftwareManager softwareManager = new Dvk3SoftwareManager();
    private final Dvk3FileManager fileManager = new Dvk3FileManager();

    // Módulos de Funcionalidade
    private final Dvk3DocReader docReader = new Dvk3DocReader();
    private final CryptoUtils cryptoUtils = new CryptoUtils();

    // Buffer de Digitação
    public StringBuilder inputBuffer = new StringBuilder();

    // Histórico de Mensagens
    private List<String> commandHistory = new ArrayList<>();
    private String lastCommand = " ";

    // Estado Básico do Sistema
    private boolean confirmationMode = false;
    private boolean isOn = true;
    private boolean safeHaltScreen = false;
    private long lastTypingTime = 0;
    private int tick = 0;



    public void notifyTyping() {
        lastTypingTime = System.currentTimeMillis();
    }

    public long getLastTypingTime() {
        return lastTypingTime;
    }

    // Processa a fila de tarefas e software (loading bars, delays de comandos)
    public void processQueue() {
        softwareManager.updateSoftwareState(this);
        taskManager.updateTasks();
        logger.processDisplayQueue(0); // 0 = Sem delay de aquecimento
    }

    public void processTick(Dvk3Core core) {
        this.tick++;
        logger.processDisplayQueue(0);
    }

    public void welcomeMessages() {
        int messageDelay = LOG_SLOW;
        String username = System.getProperty("user.name");
        String[] messages = {
                "DVK-3 OS ONLINE.",
                "Hello " + username + "!",
                "Welcome to the interactive portfolio simulation.",
                "Type [HELP] to begin navigation.",
                "Made by Arthur Dias Ferrante",
                "Waiting input..."
        };
        for (String message : messages) {
            getLogger().sysLog(Dvk3SystemLogger.LogType.INFO, message, messageDelay, false);
            messageDelay += 20;
        }
    }

    // Reseta o sistema
    public void killEverything(Dvk3Core core) {
        taskManager.killAllTasks();
        inputBuffer.setLength(0);
        docReader.closeReadMode(this);
        fileManager.resetFolders();
        logger.clearLog();
    }

    // --- Getters e Setters Essenciais ---

    public void setIsOn(boolean bool) {

        safeHaltScreen = false;
        isOn = bool;
    }

    public boolean isOn() {
        return isOn;
    }

    public String getFormattedHour() {
        // Relógio fictício baseado nos ticks
        int hours = (tick / 60) % 24;
        int mins = tick % 60;
        return String.format("%02d:%02d", hours, mins);
    }

    public void addCommandToHistory(String validCommand) {
        if (commandHistory.contains(validCommand)) {
            commandHistory.remove(validCommand);
        }

        if (commandHistory.size() >= MAX_HISTORY_COMMANDS) {
            commandHistory.remove(0);
        }
        commandHistory.add(validCommand);
    }

    public int getCommandHistoryMessageSize() {
        return commandHistory.size();
    }

    public String getCommandHistoryMessage(int index) {
        if (index < 0 || index >= commandHistory.size()) {
            return "";
        }

        return commandHistory.get(index);
    }

    public CryptoUtils getCryptoUtils() {
        return cryptoUtils;
    }

    public Dvk3FileManager getFileManager() {
        return fileManager;
    }

    public Dvk3SystemLogger getLogger() {
        return this.logger;
    }

    public Dvk3TaskManager getTaskManager() {
        return this.taskManager;
    }

    public Dvk3DocReader getDocReader() {
        return docReader;
    }

    public Dvk3SoftwareManager getSoftwareManager() {
        return softwareManager;
    }

    public int getHeatingDelay() {
        // Heating delay é sempre 0 nesta versão :p
        return 0;
    }

    public boolean isConfirmationMode() {
        return confirmationMode;
    }

    public void enterConfirmationMode() {
        this.confirmationMode = true;
    }
    public void leaveConfirmationMode() {
        this.confirmationMode = false;
    }

    public String getLastCommand() {
        return lastCommand;
    }

    public void setLastCommand() {
        if (!isConfirmationMode()) {
            lastCommand = commandHistory.getLast();
        }
    }

    public boolean isSafeHalt() {
        return safeHaltScreen;
    }

    public void triggerSafeHalt() {
        this.safeHaltScreen = true;
    }
}