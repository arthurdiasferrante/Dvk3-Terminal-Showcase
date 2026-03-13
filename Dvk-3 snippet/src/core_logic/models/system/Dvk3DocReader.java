package core_logic.models.system;

import core_logic.models.filesystem.VirtualFile;
import core_logic.models.physical.Dvk3Core;
import core_logic.models.utils.CryptoUtils;
import core_logic.models.filesystem.Dvk3FileManager;

import java.util.ArrayList;
import java.util.List;

public class Dvk3DocReader {

    private boolean open = false;

    // paginação
    private int currentPage = 0;
    private final int LINES_PER_PAGE = 12;
    private int totalLines = 0;
    private List<String> formattedLines = new ArrayList<>();

    // úteis para decriptar
    private boolean tuningMode;
    private long lastInteractionTime;
    private String lastKeyInteracted = "";
    // contexto do arquivo aberto
    private VirtualFile openedFile;
    private Dvk3System systemRef;

    // --- CORE ---

    public void processTick() {
        if (open && openedFile != null && systemRef != null) {

            boolean hasSafeLayer = openedFile.getEncryptionLayers() != null &&
                    openedFile.getEncryptionLayers().contains(CryptoUtils.EncryptionType.SAFE);
            boolean outOfTune = openedFile.getCurrentFrequency() != openedFile.getIDEAL_FREQUENCY();
            if (hasSafeLayer || outOfTune) {
                String dynamicContent = systemRef.getCryptoUtils().encryptContent(openedFile);
                wordWrap(dynamicContent, 51);
            }
        }
    }

    // métoddo principal para abrir um arquivo (Chamado pelo comando CAT)
    public void chitatMethod(Dvk3System system, Dvk3FileManager fileManager, String fileName, boolean setTuningMode) {
        this.systemRef = system;
        this.currentPage = 0;
        this.tuningMode = setTuningMode;

        this.openedFile = fileManager.getCurrentFolder().getFileByName(fileName);

        if (openedFile == null) return;

        String content = system.getCryptoUtils().encryptContent(openedFile);
        wordWrap(content, 51);

        this.open = true;
    }

    // algoritmo de quebra de linha para caber na janela do terminal
    private void wordWrap(String content, int maxWidth) {
        formattedLines.clear();
        String[] words = content.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            if (currentLine.length() + word.length() + 1 <= maxWidth) {
                if (currentLine.length() > 0) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            } else {
                formattedLines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            }
        }
        if (currentLine.length() > 0) {
            formattedLines.add(currentLine.toString());
        }

        totalLines = formattedLines.size();
    }

    // --- NAVEGAÇÃO ---

    public void nextPage() {
        if ((currentPage + 1) * LINES_PER_PAGE < totalLines) {
            currentPage++;
        }
    }

    public void returnPage() {
        if (currentPage > 0) {
            currentPage--;
        }
    }

    public void closeReadMode(Dvk3System system) {
        this.open = false;
        system.getTaskManager().killTaskByName("CHITAT_PROTOKOL");
        currentPage = 0;
    }

    // --- CRIPTOGRAFIA ---

    public void notifyInteractor(long currentTime) {
        this.lastInteractionTime = currentTime;
    }

    // --- GETTERS E SETTERS ---


    public void setLastInteractionTime(long lastInteractionTime) {
        this.lastInteractionTime = lastInteractionTime;
    }

    public void setLastKeyInteracted(String lastKeyInteracted) {
        this.lastKeyInteracted = lastKeyInteracted;
    }

    public String getLastKeyInteracted() {
        return lastKeyInteracted;
    }

    public int getCurrentFrequency() {
        return openedFile != null ? openedFile.getCurrentFrequency() : 0;
    }

    public int getIdealFrequency() {
        return openedFile != null ? openedFile.getIDEAL_FREQUENCY() : 0;
    }

    public String getReadFileName() {
        return openedFile != null ? openedFile.getFileName() : "UNKNOWN";
    }

    public List<String> getFormattedLines() {
        return formattedLines;
    }

    public VirtualFile getOpenedFile() { return openedFile; }

    public int getCurrentPage() { return currentPage; }
    public int getLINES_PER_PAGE() { return LINES_PER_PAGE; }
    public int getTotalLines() { return totalLines; }

    public boolean isOpen() { return open; }

    public boolean isTuningMode() {
        return tuningMode;
    }

    public long getLastInteractionTime() {
        return lastInteractionTime;
    }

}