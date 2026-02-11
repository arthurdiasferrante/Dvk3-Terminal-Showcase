package core_logic.models.filesystem;

import core_logic.models.system.Dvk3System;
import core_logic.models.utils.CryptoUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.stream.Collectors;

import static core_logic.models.filesystem.VirtualFile.FileType.*;
import static core_logic.models.filesystem.VirtualFolder.accessRequired.*;
import static core_logic.models.rules.DigitalManual.*;
import static core_logic.models.system.Dvk3SystemLogger.LogType.*;
import static core_logic.models.rules.Dvk3Config.*;
import static core_logic.models.utils.CryptoUtils.EncryptionType.*;


public class Dvk3FileManager {
    private final VirtualFolder rootFolder;
    private VirtualFolder currentFolder;


    public Dvk3FileManager() {
        this.rootFolder = new VirtualFolder(STANDART,"ROOT", true, null);
        this.currentFolder = this.rootFolder;

        initializeSystem();

    }

    private String loadTextResource(String fileName) {
        try {
            String path = "assets/data/" + fileName;
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
            if (inputStream == null) {
                return "ERROR: File content missing for " + fileName;
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "SYSTEM ERROR: Corrupted Data Block.";
        }
    }

    public void initializeSystem() {
        String readmeContent = loadTextResource("readme.txt");
        String contactContent = loadTextResource("contacts.txt");
        String contactContent2 = loadTextResource("contacts2.txt");
        String projectsContent = loadTextResource("projects.txt");
        String secretLore = loadTextResource("secret.txt");

        // 1. ROOT (/)
//        new VirtualFile(TEXT, "README.TXT", rootFolder, readmeContent);

        // 2. BIN (/BIN)
        VirtualFolder binFolder = new VirtualFolder(STANDART, "BIN", true, rootFolder);
        new VirtualFile(EXEC, "SYSTEM_CHECK", binFolder, "");
        new VirtualFile(EXEC, "MEM_TEST", binFolder, "");

        // 3. HOME (/HOME)
        VirtualFolder homeFolder = new VirtualFolder(STANDART, "HOME", true, rootFolder);
        new VirtualFile(TEXT, "CONTACT1.TXT", homeFolder, contactContent);
        new VirtualFile(TEXT, "CONTACT2.TXT", homeFolder, contactContent2);
        new VirtualFile(TEXT, "PROJECTS.TXT", homeFolder, projectsContent);

        //
        VirtualFolder secretFolder = new VirtualFolder(STANDART, "PRIVATE", true, homeFolder);
        new VirtualFile(TEXT, "DIARY.LOG", secretFolder, secretLore).addEncryption(SAFE);

        // 4. SYS
        VirtualFolder sysFolder = new VirtualFolder(STANDART, "SYS", true, rootFolder);
        new VirtualFile(SYSTEM, "KERNEL.DAT", sysFolder, "BINARY DATA...");
        new VirtualFile(EVENT_LOG, "BOOT.LOG", sysFolder, "System booted successfully at 08:00.");
    }


    public void dostupMethod(String rawCommand, Dvk3System system) {
        String[] parts = rawCommand.trim().split("\\s+");
        String folderName = parts[1];

        if (currentFolder.getFolderByName(folderName) != null) {
            system.getLogger().sysLog(INFO, ">> ACCESSING DIRECTORY " + folderName + "...", LOG_INSTANT);
            currentFolder = currentFolder.getFolderByName(folderName);
            spisokProtocol(system);
        } else if (currentFolder.getFileByName(folderName) != null) {
            system.getLogger().sysLog(ERROR, getError(35), LOG_FAST);
        } else {
            system.getLogger().sysLog(ERROR, getError(31), LOG_FAST);
        }
    }

    public void nazadMethod(Dvk3System system) {
        if (currentFolder.getParentFolder() != null) {
            system.getLogger().sysLog(INFO, ">> RETURNING TO PARENT DIRECTORY...", LOG_INSTANT);
            currentFolder = currentFolder.getParentFolder();
            spisokProtocol(system);
        } else {
            system.getLogger().sysLog(ERROR, getError(38), LOG_FAST);
        }
    }

    public void spisokProtocol(Dvk3System system) {

        String path = getCurrentPath();
        if (path.isEmpty()) path = "/";

        String headerPath  = String.format("| DIRECTORY: %-36s |", path);
        String borderTop   = "+------------------------+----------+-------------+";
        String header =      "| CATALOG NAME           | TIP      | STATUS      |";
        String borderSplit = "+------------------------+----------+-------------+";

        String formatLine = "| %-22s | %-8s | %-11s |";

        system.getLogger().sysLog(INFO, borderTop, LOG_NORMAL);
        system.getLogger().sysLog(INFO, headerPath, LOG_NORMAL);
        system.getLogger().sysLog(INFO, borderSplit, LOG_NORMAL);
        system.getLogger().sysLog(INFO, header,LOG_NORMAL);
        system.getLogger().sysLog(INFO, borderSplit, LOG_NORMAL);

        for (VirtualFolder folder : currentFolder.getFoldersList()) {
            String name = folder.getFolderName();

            if (name.length() > 22) {
                name = name.substring(0, 22);
            }

            String type = "<DIR>";
            String state = "-----";

            String line = String.format(formatLine, name, type, state);
            system.getLogger().sysLog(INFO, line, LOG_NORMAL);
        }

        for (VirtualFile file : currentFolder.getFilesList()) {
            String name = file.getFileName();

            if (name.length() > 22) {
                name = name.substring(0, 22);
            }

            String type = getDisplayType(file);
            String state = "-----";

            if (false) {
                state = "BLOCKED";
            } else if (!file.getEncryptionLayers().isEmpty()) {
                state = "ENCRYPTED";
            } else if (file.getType() == SYSTEM) {
                state = "SISTEMA";
            }

            String line = String.format(formatLine, name, type, state);
            system.getLogger().sysLog(INFO, line, LOG_NORMAL);
        }

        system.getLogger().sysLog(INFO, borderSplit, LOG_NORMAL);

        int total = currentFolder.getFoldersList().size() + currentFolder.getFilesList().size();
        String footer = String.format("| IN TOTAL: %d CATALOGS                            |", total);
        if (total == 1) {
            footer = String.format("| IN TOTAL: %d CATALOG                             |", total);
        }
        if (total >= 10) {
            footer = String.format("| IN TOTAL: %d CATALOGS                           |", total);
        }

        system.getLogger().sysLog(INFO, footer, LOG_NORMAL);
        system.getLogger().sysLog(INFO, borderSplit, LOG_NORMAL);
    }

    public void rootMethod(Dvk3System system) {
        if (currentFolder.getParentFolder() != null) {
            resetFolders();
            system.getLogger().sysLog(INFO, ">> RETURNING TO ROOT DIRECTORY...", LOG_INSTANT);
            spisokProtocol(system);
        } else {
            system.getLogger().sysLog(ERROR, getError(38), LOG_FAST);
        }
    }

    public void resetFolders() {
        currentFolder = rootFolder;
    }

    public VirtualFile checkExecutableFiles(String command, Dvk3System system) {
        VirtualFile currentFile = currentFolder.getFileByName(command);
        if (currentFile != null) {
            if (currentFile.getType() == EXEC) {
                system.getLogger().sysLog(SUCCESS, "Don't touch me!", LOG_FAST);
                return currentFile;
            }
            else {
                system.getLogger().sysLog(ERROR, getError(36), LOG_FAST);
            }
        }
        return null;
    }

    private String getCurrentPath() {
        VirtualFolder temp = this.currentFolder;
        String fullPath = "";

        while (temp != null) {
            fullPath = "/" + temp.getFolderName() + fullPath;

            temp = temp.getParentFolder();
        }

        return fullPath.toUpperCase();
    }


    private String getDisplayType(VirtualFile file) {
        return switch (file.getType()) {
            case TEXT -> "(DOC)";
            case SYSTEM -> "SYS_CORE";
            case MESSAGE -> "(COMM)";
            case EVENT_LOG -> "(LOG)";
            case TRASH -> {
                String extension = file.getFileName();
                if (extension.endsWith(".TMP")) yield "(CACHE)";
                if (extension.endsWith(".CHK")) yield "(FRAG)";

                if (extension.endsWith(".DMP")) yield "!LOG!";
                if (extension.endsWith(".ERR")) yield "!EXEC!";

                if (extension.endsWith(".~~~")) yield "#FAULT#";
                yield "! ERR ! ";
            }
            default -> "UNKNOWN";
        };
    }


    public VirtualFolder getCurrentFolder() {
        return currentFolder;
    }

    public void getFrequency(String file, Dvk3System system) {
        DecimalFormat df = new DecimalFormat("#,##0,000");
        if (currentFolder.getFileByName(file) != null) {
            VirtualFile currentFile = currentFolder.getFileByName(file);
            system.getLogger().sysLog(INFO, df.format(currentFile.getCurrentFrequency()), LOG_FAST);
            system.getLogger().sysLog(WARN, currentFile.getIDEAL_FREQUENCY() + "", LOG_NORMAL);
        } else {
            system.getLogger().sysLog(ERROR, "ERRO", LOG_INSTANT);
        }
    }

}
