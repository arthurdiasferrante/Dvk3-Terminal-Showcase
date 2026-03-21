package core_logic.models.filesystem;

import java.util.ArrayList;
import java.util.List;

public class VirtualFolder {
    private String folderName;
    private boolean fixed; // algumas pastas do sistema são permanentes enquanto outras eu randomizo aleatoriamente no sistema
    private accessRequired accessLevel;

    private List<VirtualFolder> foldersList = new ArrayList<>();
    private List<VirtualFile> filesList = new ArrayList<>();
    private VirtualFolder parentFolder;

    public static enum accessRequired {
        STANDART,
        RABOCHIY_I,
        RABOCHIY_II,
        ADMIN
    }

    public VirtualFolder(accessRequired access, String folderName, boolean fixed, VirtualFolder parentFolder) {
        this.folderName = folderName;
        this.fixed = fixed;
        this.accessLevel = access;
        this.parentFolder = parentFolder;

        if (parentFolder != null) {
            parentFolder.addSubFolder(this);
        }
    }

    public void addSubFolder(VirtualFolder folder) {
        foldersList.add(folder);
    }

    public List<VirtualFolder> getFoldersList() {
        return foldersList;
    }

    public String getFolderName() {
        return folderName;
    }

    public VirtualFolder getFolderByName(String name) {
        String formattedName = name.replace("\\", " ");
        for (VirtualFolder folder : foldersList) {
            if (folder.getFolderName().equals(formattedName)) {
                return folder;
            }
        }
        return null;
    }

    public VirtualFile getFileByName(String name) {
        String formattedName = name.replace("\\", " ").toUpperCase();

        for (VirtualFile file : this.getFilesList()) {
            if (file.getFileName().equals(formattedName) || file.getFileName().startsWith(formattedName + ".")) {
                return file;
            }
        }
        return null;
    }

    public accessRequired getAccessLevel() {
        return accessLevel;
    }

    public VirtualFolder getParentFolder() {
        return parentFolder;
    }

    public List<VirtualFile> getFilesList() {
        return filesList;
    }
}
