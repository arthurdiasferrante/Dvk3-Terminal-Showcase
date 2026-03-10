package core_logic.controller;

import static core_logic.models.rules.DigitalManual.getError;
import static core_logic.models.rules.Dvk3Config.*;
import static core_logic.models.system.Dvk3SystemLogger.LogType.*;

import core_logic.models.*;
import core_logic.models.filesystem.VirtualFile;
import core_logic.models.physical.Dvk3Core;
import core_logic.models.system.Dvk3System;

import java.io.BufferedReader;

public class ProcessCommands {

    public void executeCommand(String rawCommand, Dvk3System system, Dvk3Core core, BunkerState state) {
        String cleanCommand = rawCommand.trim().toUpperCase();
        String[] parts = cleanCommand.split("\\s+");
        if (parts.length == 0) {
            return;
        }
        String mainCommand = parts[0];

        system.setLastCommand();

        if (system.isConfirmationMode()) {
            if (mainCommand.equals("Y")) {
                cleanCommand = (system.getLastCommand()).toUpperCase() + " Y";
                parts = cleanCommand.split("\\s+");
                mainCommand = parts[0];
                system.leaveConfirmationMode();
            } else if (mainCommand.equals("N")) {
                mainCommand = "n";
                system.leaveConfirmationMode();
            } else {
                mainCommand = "syntaxerror";
            }
        }

        switch (mainCommand) {
            case "HELP":
            case "?":
                system.getLogger().sysLog(INFO, "=== DVK-3 SHOWCASE OS v1.0 ===", LOG_INSTANT);
                system.getLogger().sysLog(INFO, "COMMAND LIST:", LOG_NORMAL);
                system.getLogger().sysLog(INFO, "> LS          : List files", LOG_NORMAL);
                system.getLogger().sysLog(INFO, "> CD [DIR]    : Change directory (.. to go back)", LOG_NORMAL);
                system.getLogger().sysLog(INFO, "> CAT [FILE]  : Read file content", LOG_NORMAL);
                system.getLogger().sysLog(INFO, "> DECRYPT     : Decrypt secure files", LOG_NORMAL);
                system.getLogger().sysLog(INFO, "> CLEAR       : Clear screen", LOG_NORMAL);
                system.getLogger().sysLog(INFO, "> SHUTDOWN    : Power off system", LOG_NORMAL);
                break;
            case "CLEAR": // Limpar tela
                system.getLogger().sysLog(INFO, "CLEARING BUFFER...", LOG_INSTANT);
                system.getLogger().callClearLog(LOG_NORMAL);
                break;

            case "SHUTDOWN": // Antigo HALT
                if (parts.length == 1) {
                    system.getLogger().sysLog(INFO, "ARE YOU CERTAIN YOU WANT TO END CURRENT SESSION? [Y/N]", LOG_FAST);
                    system.enterConfirmationMode();
                }
                if (parts.length >= 2) {
                    if (parts[1].equals("Y")) {
                        system.getSoftwareManager().scheduleProtocol(system, cleanCommand, TIME_STANDARD);
                    } else {
                        system.getLogger().sysLog(ERROR, getError(9), LOG_FAST);
                    }
                }
                break;
            case "n":
                system.getLogger().sysLog(INFO, "OPERATION CANCELLED", LOG_INSTANT);
                break;
            case "syntaxerror":
                system.getLogger().sysLog(ERROR,"SYNTAX ERROR. TERMINATE SESSION? [Y/N]", LOG_INSTANT);
                break;

//                   ------------------- COMANDOS DE CRIPTOGRAFIA --------------------

            case "DECRYPT":
            case "FIX":
                if (parts.length < 3) { // STABILIZE ARQUIVO FREQUENCIA
                    system.getLogger().sysLog(ERROR, getError(7), LOG_FAST); // Syntax Error 7
                } else if (parts.length > 3) {
                    system.getLogger().sysLog(ERROR, getError(9), LOG_FAST); // Invalid Args 9
                } else {
                    system.getSoftwareManager().scheduleProtocol(system, cleanCommand, TIME_FAST);
                }
                break;


            case "CHECK":
                if (parts.length > 1) {
                    system.getFileManager().getFrequency(parts[1], system);
                } else {
                    system.getLogger().sysLog(ERROR, getError(7), LOG_FAST);
                }
                break;

//                   --------------------- COMANDOS DE ARQUIVOS ----------------------

            case "LS":
            case "LIST":
            case "DIR": // Listar arquivos
                if (parts.length > 1) {
                    system.getLogger().sysLog(ERROR, getError(9), LOG_FAST);
                } else {
                    system.getFileManager().spisokProtocol(system);
                }
                break;

            case "CD": // Acessar pasta
                if (parts.length > 2) {
                    system.getLogger().sysLog(ERROR, getError(9), LOG_FAST);
                } else if (parts.length == 1) {
                    system.getFileManager().nazadMethod(system);
                } else {
                    system.getFileManager().dostupMethod(cleanCommand, system);
                }
                break;

            case "ROOT":
            case "/": // Voltar para raiz
                if (parts.length > 1) {
                    system.getLogger().sysLog(ERROR, getError(9), LOG_FAST);
                } else {
                    system.getFileManager().rootMethod(system);
                }
                break;

            case "OPEN":
            case "READ":
            case "CAT":
                if (parts.length == 1) {
                    system.getLogger().sysLog(ERROR, getError(7), LOG_FAST);
                } else if (parts.length > 2) {
                    system.getLogger().sysLog(ERROR, getError(9), LOG_FAST);
                } else {
                    system.getSoftwareManager().scheduleProtocol(system, cleanCommand, LOG_NORMAL);
                }
                break;
            default:
                // Tenta executar arquivo direto pelo nome (ex: NOTES.TXT ou apenas NOTES)
                VirtualFile executableFile = system.getFileManager().checkExecutableFiles(mainCommand, system);

                if (executableFile != null) {
                    system.getLogger().sysLog(SUCCESS, "EXECUTING " + executableFile.getFileName(), LOG_FAST);
                } else {
                    // Se não for comando nem arquivo, erro padrão
                    if (system.getFileManager().getCurrentFolder().getFileByName(mainCommand) == null) {
                        system.getLogger().sysLog(ERROR, getError(1), LOG_FAST); // Command not found
                    }
                }
                break;
        }
    }
}