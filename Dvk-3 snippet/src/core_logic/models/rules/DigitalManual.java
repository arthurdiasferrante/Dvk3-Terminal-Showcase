package core_logic.models.rules;

import java.util.*;

public class DigitalManual {

    private static final Random random = new Random();

    public static String getError(int code) {

        return switch (code) {
//          =========================================================
//          | 0 - 9 FAMILY: SYNTAX & INPUT HANDLER                  |
//          =========================================================
            case 1 -> "[ERR_01]:: " + getCurrentError(1); // erro de sintaxe
            case 5 -> "[ERR_05]:: " + getCurrentError(5); // input não numerico
            case 6 -> "[ERR_06]:: " + getCurrentError(6); // parametro fora do alcance
            case 7 -> "[ERR_07]:: " + getCurrentError(7); // parametro faltando
            case 8 -> "[ERR_08]:: " + getCurrentError(8); // parametro invalido
            case 9 -> "[ERR_09]:: " + getCurrentError(9); // muitos parametros
//          =========================================================

//          =========================================================
//          | 10 - 19 FAMILY: RUNTIME & SCHEDULING                  |
//          =========================================================
            case 12 -> "[ERR_12]:: " + getCurrentError(12); // sistema ocupado
//          =========================================================


//          =========================================================
//          | 20 - 29 FAMILY: HARDWARE & PERIPHERALS                |
//          =========================================================
            case 23 -> "[ERR_23]:: "; // erro de dispositivo quebrado
//          =========================================================
//
//          =========================================================
//          | 30 - 39 FAMILY: REFERENCE & NAVIGATION                |
//          =========================================================
            case 31 -> "[ERR_31]:: " + getCurrentError(31); // diretório / arquivo não encontrado
            case 34 -> "[ERR_34]:: " + getCurrentError(34); // conflito de tipo (lendo uma pasta)
            case 35 -> "[ERR_35]:: " + getCurrentError(35); // conflito de tipo (entrando em um arquivo)
            case 36 -> "[ERR_36]:: " + getCurrentError(36); // arquivo não é executável
            case 38 -> "[ERR_38]:: " + getCurrentError(38); // erro de navegação, impossível voltar
//          =========================================================

//          =========================================================
//          | 40 - 49 FAMILY: SECURITY & PERMISSION]                |
//          =========================================================
            case 40 -> "[ERR_40]:: " + getCurrentError(40);
//          =========================================================


//          =========================================================
//          | 80 - 89 FAMILY: LOGIC & HEURISTICS                    |
//          =========================================================
            case 82 -> "[ERR_82]:: " + getCurrentError(82); // conflito de automação
            case 87 -> "[ERR_87]:: " + getCurrentError(87); // erro delta zero
            case 88 -> "[ERR_88]:: " + getCurrentError(88); // erro de redundância
//          =========================================================

            default -> "[ERR_??] ZKZ:: ";
        };
    }

    private static String getCurrentError(int error) {
        return switch (error) {
            case 1 -> getCommonSyntaxError();
            case 5 -> getCommonNumFormatError();
            case 6 -> getCommonRangeError();
            case 7 -> getCommonNullError();
            case 8 -> getCommonInvalidParameter();
            case 9 -> getCommonInputFormatError();
            case 12 -> getCommonBusyQueueError();
            case 31 -> getCommonInvalidFileError();
            case 34 -> getCommonTypeMismatchError();
            case 35 -> getCommonInvalidDestError();
            case 36 -> getCommonNotExecutableError();
            case 38 -> getCommonRootBoundaryLimitError();
            case 40 -> getCommonDeniedPermissionError();
            case 82 -> getCommonAutoConflictError();
            case 87 -> getCommonNoDeltaError();
            case 88 -> getCommonRedundantError();
            default -> "ainda vazio";
        };
    }

    // ------------------ 0 - 9 FAMILY ------------------
    private static String getCommonSyntaxError() {
        String[] commonSyntaxErrors = {
                "0xBAD_INPUT. BUFFER FLUSHED.",
                "INPUT INVALID. CONSULT \"HELP\" FOR COMMAND LIST.",
                "COMMAND NOT RECOGNIZED BY DVK-3 KERNEL.",
                "UNKNOWN COMMAND. CHECK \"HELP\" FOR COMMAND LIST.",
                "COMMAND NOT FOUND. CHECK YOUR SPELLING.",
                "UNKNOWN COMMAND. "
        };
        return commonSyntaxErrors[random.nextInt(commonSyntaxErrors.length)];
    }



    private static String getCommonNumFormatError() {
        String[] commonNumErrors = {
                "DATA TYPE ERROR. EXPECTED NUMERIC VALUE.",
                "INVALID INPUT. ARGUMENT MUST BE A NUMBER.",
                "PARSING FAILED. NAN DETECTED.",
                "FORMAT ERROR. ALPHABETIC CHARACTERS NOT ALLOWED HERE.",
        };
        return commonNumErrors[random.nextInt(commonNumErrors.length)];
    }

    private static String getCommonRangeError() {
        String[] commonRangeErrors = {
                "VALUE OUT OF RANGE. CHECK FREQUENCY LIMITS.",
                "INPUT REJECTED. FREQUENCY BAND NOT SUPPORTED.",
                "OUT OF BOUNDS. COIL CANNOT RESONATE AT THIS LEVEL.",
                "INVALID FREQUENCY. SIGNAL OUT OF OPERATIONAL SPECS."
        };
        return commonRangeErrors[random.nextInt(commonRangeErrors.length)];
    }


    private static String getCommonNullError() {
        String[] commonNullErrors = {
                "MISSING PARAMETER. SPECIFY TARGET.",
                "INCOMPLETE COMMAND. DATA REQUIRED.",
                "MISSING POINTER. ARGUMENT NOT DEFINED.",
                "ARGUMENT REQUIRED. CHECK THE MANUAL FOR MORE INFORMATION.",
                "ERROR: COMMAND EXPECTS [ON/OFF/STATUS...]."
        };
        return commonNullErrors[random.nextInt(commonNullErrors.length)];
    }

    private static String getCommonInvalidParameter() {
        String[] commonInvalidParameter = {
                "INVALID PARAMETER VALUE. CHECK MANUAL.",
                "ARGUMENT REJECTED. VALUE NOT RECOGNIZED.",
                "OPTION NOT SUPPORTED. INVALID INPUT DATA.",
                "INPUT ERROR. UNKNOWN ARGUMENT TYPE.",
                "ARGUMENT REJECTED. PARAMETER NOT VALID."
        };
        return commonInvalidParameter[random.nextInt(commonInvalidParameter.length)];
    }


    private static String getCommonInputFormatError() {
        String[] commonInputFormatErros = {
                "INPUT BUFFER OVERFLOW. PARAMETER LIMIT EXCEEDED.",
                "SYNTAX ERROR: TOO MANY ARGUMENTS FOR THIS COMMAND.",
                "PARSING FAILED. COMMAND EXPECTS FEWER PARAMETERS.",
                "FORMAT VIOLATION. REDUCE ARGUMENT COUNT.",
                "DATA REJECTED. INPUT STRING TOO LONG."
        };
        return commonInputFormatErros[random.nextInt(commonInputFormatErros.length)];
    }


    // ------------------ 10 - 19 FAMILY ------------------
    private static String getCommonBusyQueueError() {
        String[] commonBusyQueue = {
                "RESOURCE BUSY. TASK ALREADY IN QUEUE.",
                "PROCESS LOCK: TIMER IS CURRENTLY RUNNING.",
                "CONFLICT: CANNOT INTERRUPT ACTIVE CYCLE.",
                "SYSTEM BUSY. PLEASE STAND BY."
        };
        return commonBusyQueue[random.nextInt(commonBusyQueue.length)];
    }


    // ------------------ 30 - 39 FAMILY ------------------

    private static String getCommonInvalidFileError() {
        String[] commonInvalidFile = {
                "PATH INVALID. TARGET DIRECTORY NOT FOUND.",
                "REFERENCE ERROR: OBJECT DOES NOT EXIST.",
                "PATH INVALID. CHECK FILE NAME.",
                "LOOKUP FAILED. SECTOR IS EMPTY.",
                "INVALID PATH. DESTINATION UNREACHABLE."
        };
        return commonInvalidFile[random.nextInt(commonInvalidFile.length)];
    }


    private static String getCommonTypeMismatchError() {
        String[] commonTypeMismatch = {
                "TYPE MISMATCH. TARGET IS A DIRECTORY.",
                "METHOD FAILED. TARGET IS NOT A FILE.",
                "INVALID TARGET TYPE. USE \"DOSTUP\" INSTEAD.",
                "TYPE MISMATCH. USE \"DOSTUP\" FOR DIRECTORIES.",
                "PROTOCOL VIOLATION. OBJECT IS A DIRECTORY."
        };
        return commonTypeMismatch[random.nextInt(commonTypeMismatch.length)];
    }


    private static String getCommonInvalidDestError() {
        String[] commonInvalidDest = {
                "NAVIGATION FAILED. TARGET IS A DOCUMENT.",
                "INVALID DIRECTORY. TARGET IS NOT A CATALOG.",
                "TYPE MISMATCH. CANNOT ENTER A DOCUMENT.",
                "PATH ERROR. TARGET IS NOT A DIRECTORY.",
                "NAVIGATION FAILED. OBJECT HAS NO DEPTH."
        };
        return commonInvalidDest[random.nextInt(commonInvalidDest.length)];
    }


    private static String getCommonNotExecutableError() {
        String[] commonNotExecutable = {
                "EXECUTION FAILED. TARGET IS A PASSIVE FILE.",
                "INVALID FORMAT. FILE HEADER MISSING EXECUTABLE FLAG.",
                "TYPE MISMATCH. TARGET IS STATIC.",
                "LAUNCH ABORTED. CANNOT EXECUTE STATIC FILES.",
                "EXECUTION FAILED: TARGET DOES NOT CONTAIN MACHINE CODE."
        };
        return commonNotExecutable[random.nextInt(commonNotExecutable.length)];
    }


    private static String getCommonRootBoundaryLimitError() {
        String[] commonRootBoundary = {
                "NAVIGATION ERROR: ROOT DIRECTORY REACHED.",
                "BOUNDARY LIMIT. CANNOT MOVE UP.",
                "SYSTEM ROOT. NO PARENT DIRECTORY.",
                "NAVIGATION HALTED. TOP LEVEL REACHED.",
                "NAVIGATION ERROR: NO PARENT DIRECTORY.",
        };
        return commonRootBoundary[random.nextInt(commonRootBoundary.length)];
    }


    // ------------------ 40 - 49 FAMILY ------------------

    private static String getCommonDeniedPermissionError() {
        String[] commonDeniedMsg = {
                "ACCESS DENIED. INSUFFICIENT SECURITY CLEARANCE.",
                "READ ERROR: PROTECTED MEMORY SECTOR.",
                "UNAUTHORIZED ACCESS. EVENT LOGGED IN SECURITY TAPE.",
                "ACCESS DENIED. YOU'RE NOT ALLOWED TO READ THIS FILE.",
                "UNAUTHORIZED ACCESS. YOU NEED PERMISSION TO PERFORM THIS ACTION.",
        };
        return commonDeniedMsg[random.nextInt(commonDeniedMsg.length)];
    }

    // ------------------ 80 - 89 FAMILY ------------------

    private static String getCommonAutoConflictError() {
        String[] commonAutoConflict = {
                "CONFLICT: AUTOMATIC MODE IS ACTIVE. MANUAL INPUT IGNORED.",
                "ACCESS DENIED: SYSTEM IS UNDER AUTOMATED CONTROL.",
                "OVERRIDE FAILED. DISABLE 'AUTO' MODE FIRST.",
                "LOGIC ERROR: CANNOT MIX MANUAL AND AUTOMATIC COMMANDS."
        };
        return commonAutoConflict[random.nextInt(commonAutoConflict.length)];
    }

    private static String getCommonNoDeltaError() {
        String[] commonNoDelta = {
                "INPUT IGNORED. VALUE UNCHANGED.",
                "DELTA ZERO DETECTED. NO FREQUENCY SHIFT.",
                "REDUNDANT PARAMETER. CURRENT VALUE MATCHES INPUT.",
                "COMMAND SKIPPED. SYSTEM ALREADY AT REQUESTED FREQUENCY.",
                "NULL OPERATION. ALREADY IN THIS POSITION."
        };
        return commonNoDelta[random.nextInt(commonNoDelta.length)];
    }


    private static String getCommonRedundantError() {
        String[] commonRedundantErrors = {
                "COMMAND IGNORED. DEVICE ALREADY IN TARGET STATE.",
                "REDUNDANT COMMAND. SYSTEM STATUS REMAINS THE SAME.",
                "REDUNDANT INSTRUCTION. SKIPPING CYCLE.",
                "INPUT DISCARDED. DUPLICATE REQUEST.",
                "REDUNDANT ERROR: COMMAND SKIPPED FOR OPTIMIZATION."
        };
        return commonRedundantErrors[random.nextInt(commonRedundantErrors.length)];
    }

}
