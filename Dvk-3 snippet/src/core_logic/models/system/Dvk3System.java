package core_logic.models.system;

import core_logic.models.utils.CryptoUtils;
import core_logic.models.filesystem.Dvk3FileManager;
import core_logic.models.physical.Dvk3Core;

import java.util.Random;

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

    // Estado Básico do Sistema
    private boolean isOn = true;
    private long lastTypingTime = 0;
    private int tick = 0;


    public Dvk3System() {
        // Construtor limpo, sem dependência de BunkerState
    }

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

    // Processa o ciclo básico
    public void processTick(Dvk3Core core) {
        this.tick++;
        // Foi removido os diagnósticos e aquecimento, só atualiza o log visual
        logger.processDisplayQueue(0);
    }

    // Reseta o sistema (usado ao reiniciar)
    public void killEverything(Dvk3Core core) {
        taskManager.killAllTasks();
        inputBuffer.setLength(0);
        docReader.setOpen(false);
        fileManager.resetFolders();
        logger.clearLog();
    }

    // --- Getters e Setters Essenciais ---

    public void setIsOn(boolean bool) {
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
        // Heating delay é sempre 0 nesta versão
        return 0;
    }
}