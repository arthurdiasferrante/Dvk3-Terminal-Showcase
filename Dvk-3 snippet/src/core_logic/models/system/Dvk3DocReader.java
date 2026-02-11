package core_logic.models.system;

import core_logic.models.filesystem.VirtualFile;
import core_logic.models.utils.CryptoUtils;
import core_logic.models.filesystem.Dvk3FileManager;

import java.util.ArrayList;
import java.util.List;

public class Dvk3DocReader {
    private boolean open = false;

    // Estado de Paginação
    private int currentPage = 0;
    private final int LINES_PER_PAGE = 12;
    private int totalLines = 0;
    private List<String> formattedLines = new ArrayList<>();

    // Contexto do Arquivo Aberto
    private VirtualFile openedFile;
    private Dvk3System systemRef;

    // --- LÓGICA CORE ---

    // Chamado a cada frame: Atualiza o texto se for um arquivo criptografado (SAFE)
    public void processTick() {
        if (open && openedFile != null && systemRef != null) {
            // Se o arquivo tiver a camada SAFE, o texto muda conforme a frequência ajustada
            if (openedFile.getEncryptionLayers().contains(CryptoUtils.EncryptionType.SAFE)) {
                String dynamicContent = systemRef.getCryptoUtils().encryptContent(openedFile);
                wordWrap(dynamicContent, 51); // 51 é a largura útil da janela
            }
        }
    }

    // Métoddo principal para abrir um arquivo (Chamado pelo comando READ)
    public void chitatMethod(Dvk3System system, Dvk3FileManager fileManager, String fileName) {
        this.systemRef = system;
        this.currentPage = 0;

        // Busca o arquivo na pasta atual
        this.openedFile = fileManager.getCurrentFolder().getFileByName(fileName);

        if (openedFile == null) return;

        // Gera o conteúdo inicial
        String content = system.getCryptoUtils().encryptContent(openedFile);
        wordWrap(content, 51);

        this.open = true;
    }

    // Algoritmo de quebra de linha para caber na janela do terminal
    private void wordWrap(String content, int maxWidth) {
        formattedLines.clear();
        String[] words = content.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            // Verifica se a palavra cabe na linha atual (+1 para o espaço)
            if (currentLine.length() + word.length() + 1 <= maxWidth) {
                if (currentLine.length() > 0) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            } else {
                // Se não cabe, salva a linha e começa uma nova
                formattedLines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            }
        }
        // Adiciona o resto do buffer
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

    // --- GETTERS E SETTERS ---


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
    public void setOpen(boolean open) { this.open = open; }

}