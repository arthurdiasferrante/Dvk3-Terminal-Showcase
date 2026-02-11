package core_logic.models.filesystem;

import static core_logic.models.utils.CryptoUtils.*;
import static core_logic.models.filesystem.VirtualFolder.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VirtualFile {
    private String fileName;
    private FileType type;
    private String fileContent;
    private final Random random = new Random();
    private VirtualFolder parentFolder;
    private List<EncryptionType> encryptionLayers = new ArrayList<>();
    String[] trashExtensions = {".TMP", ".CHK", ".DMP", ".ERR", ".~~~"};
    private int currentFrequency;
    private final int IDEAL_FREQUENCY;
    private boolean safeCrypted = false;


    public static enum FileType {
        TEXT,
        EXEC,
        TRASH,
        SYSTEM,
        MESSAGE,
        EVENT_LOG,
    }

    public VirtualFile(FileType type, String fileName, VirtualFolder parentFolder, String fileContent) {
        IDEAL_FREQUENCY = random.nextInt(16000 - 4000 + 1) + 4000;
        currentFrequency = IDEAL_FREQUENCY;
        this.type = type;
        this.parentFolder = parentFolder;
        this.fileContent = fileContent;

        if (parentFolder != null) {
            parentFolder.getFilesList().add(this);
        }

        applyExtension(fileName);
    }

    private void applyExtension(String rawName) {

        if (rawName.contains(".")) {
            fileName = rawName;
            return;
        }

        if (this.type == FileType.SYSTEM) fileName = rawName + ".SYS";
        else if (this.type == FileType.TEXT) fileName = rawName + ".TXT";
        else if (this.type == FileType.EXEC) fileName = rawName + ".EXE";
        else if (this.type == FileType.TRASH) fileName = rawName + trashExtensions[random.nextInt(trashExtensions.length)];
        else this.fileName = rawName;
    }


    public void addEncryption(EncryptionType... types) {

        for (EncryptionType t : types) {
            if (this.encryptionLayers.contains(t)) {
                continue;
            }
            if (encryptionLayers.size() >= 3) {
                break;
            }
            this.encryptionLayers.add(t);
        }

        if (encryptionLayers.contains(EncryptionType.SAFE)) {
            if (!safeCrypted) {
                this.currentFrequency = random.nextInt(16000 - 4000 + 1) + 4000;
            }
            safeCrypted = true;
        }

        if (!this.fileName.endsWith(".KEY")) {
            fileName = this.fileName + ".KEY";
        }

    }

    public void removeEncryption(EncryptionType... types) {
        for (EncryptionType t : types) {
            this.encryptionLayers.remove(t);
        }
        if (encryptionLayers.isEmpty()) {
            fileName = this.fileName.replace(".KEY", "");
        }
    }


    public int getCurrentFrequency() {
        return currentFrequency;
    }
    public void setCurrentFrequency(int newFrequency) {
        this.currentFrequency = newFrequency;
    }

    public int getIDEAL_FREQUENCY() {
        return IDEAL_FREQUENCY;
    }

    public boolean isSafeCrypted() {
        return safeCrypted;
    }

    public void setSafeCrypted(boolean safeCrypted) {
        this.safeCrypted = safeCrypted;
    }

    public VirtualFolder getParentFolder() {
        return parentFolder;
    }
    public FileType getType() {
        return type;
    }
    public String getFileContent() {
        return fileContent;
    }
    public List<EncryptionType> getEncryptionLayers() {
        return encryptionLayers;
    }
    public String getFileName() {
        return fileName;
    }
}

