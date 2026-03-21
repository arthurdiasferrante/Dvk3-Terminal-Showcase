package core_logic.models.filesystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VirtualFolderTest {

    @Test
    void getFilesByNameFindsFileIgnoringCase() {
        // Cria uma nova pasta, sem pasta pai
        VirtualFolder testFolder = new VirtualFolder(VirtualFolder.accessRequired.STANDART, "HOME", true, null);
        // Cria um novo arquivo sendo a sua pasta pai a pasta HOME criada
        VirtualFile virtualFile = new VirtualFile(VirtualFile.FileType.TEXT, "README", testFolder, "");

        // Testando se o métoddo aceita maiúsculas e minúsculas
        assertNotNull(testFolder.getFileByName("README"), "Method should return not null");
        assertNotNull(testFolder.getFileByName("readme"), "Method should return not null");
    }

    @Test
    void getFileByNameFindsFileByShortNameWithoutExtension() {
        // Cria uma nova pasta, sem pasta pai
        VirtualFolder testFolder = new VirtualFolder(VirtualFolder.accessRequired.STANDART, "HOME", true, null);
        // Cria um novo arquivo sendo a sua pasta pai a pasta HOME criada
        VirtualFile virtualFile = new VirtualFile(VirtualFile.FileType.TEXT, "README", testFolder, "");

        // Testando se o métoddo aceita textos sem a extensão
        assertNotNull(testFolder.getFileByName("README.TXT"), "Method should return not null");
        assertEquals("README.TXT", testFolder.getFileByName("readme").getFileName());
    }

    @Test
    void getFileByNameReturnsNullWhenMissing() {
        // Cria uma nova pasta, sem pasta pai
        VirtualFolder testFolder = new VirtualFolder(VirtualFolder.accessRequired.STANDART, "HOME", true, null);
        // Cria uma nova pasta, com a pasta pai HOME
         VirtualFolder newTestFolder = new VirtualFolder(VirtualFolder.accessRequired.STANDART, "CONTACTS", true, null);

        // Cria um novo arquivo sendo a sua pasta pai a past CONTACTS
        VirtualFile virtualFile = new VirtualFile(VirtualFile.FileType.TEXT, "CONTACTS", newTestFolder, "");

        assertNull(testFolder.getFileByName("CONTACTS"), "This file is in contacts folder");
    }

    @Test
    void getFileByNameHandlesBackslashAsPathSeparator() {
        // Cria uma nova pasta, sem pasta pai
        VirtualFolder testFolder = new VirtualFolder(VirtualFolder.accessRequired.STANDART, "HOME", true, null);
        // Cria um novo arquivo com nome dividido,  sendo a sua pasta pai a pasta HOME criada
        VirtualFile virtualFile = new VirtualFile(VirtualFile.FileType.TEXT, "MY FILE", testFolder, "");

        assertNotNull(testFolder.getFileByName("MY\\FILE"));
    }
}