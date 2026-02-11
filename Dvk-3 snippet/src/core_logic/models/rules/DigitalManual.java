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
        int chance = random.nextInt(100);
        switch (error) {
            case 1:
                if (chance < 98) return getCommonSyntaxError();
                else return getRareSyntaxError();
            case 5:
                if (chance < 98) return getCommonNumFormatError();
                else return getRareNumFormatError();
            case 6:
                if (chance < 98) return getCommonRangeError();
                else return getRareRangeError();
            case 7:
                if (chance < 98) return getCommonNullError();
                else return getRareNullError();
            case 8:
                if (chance < 98) return (getCommonInvalidParameter());
                else return (getRareInvalidParameter());
            case 9:
                if (chance < 98) return getCommonInputFormatError();
                else return getRareInputFormatError();
            case 12:
                if (chance < 98) return getCommonBusyQueueError();
                else return getRareBusyQueueError();
            case 31:
                if (chance < 98) return getCommonInvalidFileError();
                else return getRareInvalidFileError();
            case 34:
                if (chance < 98) return getCommonTypeMismatchError();
                else return getRareTypeMismatchError();
            case 35:
                if (chance < 98) return getCommonInvalidDestError();
                else return getRareInvalidDestError();
            case 36:
                if (chance < 98) return getCommonNotExecutableError();
                else return getRareNotExecutableError();
            case 38:
                if (chance < 98) return getCommonRootBoundaryLimitError();
                else return getRareRootBoundaryLimitError();
            case 40: if (chance < 98) return getCommonDeniedPermissionError();
            else return getRareDeniedPermissionError();
            case 82: if (chance < 98) return getCommonAutoConflictError();
            else return getRareAutoConflictError();
            case 87: if (chance < 98) return getCommonNoDeltaError();
            else return getRareNoDeltaError();
            case 88: if (chance < 98) return getCommonRedundantError();
            else return getRareRedundantError();
            default:
                return "ainda vazio";
        }
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

    private static String getRareSyntaxError() {
        String[] rareSyntaxErrors = {
                "UNKNOWN COMMAND. ARE YOU HALLUCINATING, COMRADE?",
                "PEBKAC ERROR: PROBLEM EXISTS BETWEEN KEYBOARD AND CHAIR.",
                "COMMAND NOT FOUND. DID YOU ASK NICELY?",
                "SYSTEM OPTIMAL. ERROR SOURCE: CHAIR OCCUPANT.",
                "WAIT, MAYBE THIS WILL WORK, TRY AGAIN.",
        };
        return rareSyntaxErrors[random.nextInt(rareSyntaxErrors.length)];
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

    private static String getRareNumFormatError() {
        String[] rareNumErrors = {
                "POETRY DETECTED. SAVE YOUR WORDS.",
                "USE NUMBERS.",
                "NUMBERS.",
        };
        return rareNumErrors[random.nextInt(rareNumErrors.length)];
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

    private static String getRareRangeError() {
        String[] rareRangeErrors = {
                "DO YOU WANT TO BREAK THE COIL? STOP.",
                "PHYSICALLY IMPOSSIBLE. TRY A REAL NUMBER.",
                "THE MACHINE CANNOT STRETCH THAT FAR.",
        };
        return rareRangeErrors[random.nextInt(rareRangeErrors.length)];
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

    private static String getRareNullError() {
        String[] rareNullErrors = {
                "INCOMPLETE INPUT. I CANNOT READ YOUR MIND.",
                "TARGET IS NULL. IS THE TARGET IN THE ROOM WITH US?",
                "COMMAND ABORTED. DID YOU FALL ASLEEP TYPING?"
        };
        return rareNullErrors[random.nextInt(rareNullErrors.length)];
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

    private static String getRareInvalidParameter() {
        String[] rareInvalidParameter = {
                "DATA REJECTED. STOP MASHING THE KEYBOARD.",
                "SYNTAX OK, LOGIC FAILED. THAT MAKES NO SENSE.",
                "DATA ERROR. EXPECTED: [ON/OFF/STATUS] RECEIVED: GARBAGE."
        };
        return rareInvalidParameter[random.nextInt(rareInvalidParameter.length)];
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

    private static String getRareInputFormatError() {
        String[] rareInputFormatErros = {
                "I STOPPED LISTENING AFTER THE FIRST WORD.",
                "YOU TALK TOO MUCH, COMRADE.",
                "SILENCE IS GOLDEN. TRY FEWER WORDS.",
        };
        return rareInputFormatErros[random.nextInt(rareInputFormatErros.length)];
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

    private static String getRareBusyQueueError() {
        String[] rareBusyQueue = {
                "DO NOT RUSH ME.",
                "PROCESSOR IS MEDITATING. DO NOT DISTURB.",
                "YOU TRY. I IGNORE YOU."
        };
        return rareBusyQueue[random.nextInt(rareBusyQueue.length)];
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

    private static String getRareInvalidFileError() {
        String[] rareInvalidFile = {
                "IT DOES NOT EXIST. IT NEVER DID.",
                "SEARCH FAILED. TRY AGAIN LATER.",
                "YOU ARE LOOKING FOR GHOSTS."
        };
        return rareInvalidFile[random.nextInt(rareInvalidFile.length)];
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

    private static String getRareTypeMismatchError() {
        String[] rareTypeMismatch = {
                "WRONG TOOL. DO NOT FORCE IT.",
                "STOP.",
                "THERE IS NOTHING FOR YOU HERE."
        };
        return rareTypeMismatch[random.nextInt(rareTypeMismatch.length)];
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

    private static String getRareInvalidDestError() {
        String[] rareInvalidDest = {
                "YOU CANNOT HIDE IN THERE.",
                "ARE YOU SURE YOU WANT TO DO THIS?",
                "NOT A VALID TARGET. DON'T TRY AGAIN.",
        };
        return rareInvalidDest[random.nextInt(rareInvalidDest.length)];
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

    private static String getRareNotExecutableError() {
        String[] rareNotExecutable = {
                "EXECUTING TARGET... JUST KIDDING?.",
                "IT DOES NOTHING.",
                "STATIC FILE. THEY DO NOT OBEY YOU.",
                "TYPE MISMATCH. YOU CANNOT FORCE IT."
        };
        return rareNotExecutable[random.nextInt(rareNotExecutable.length)];
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

    private static String getRareRootBoundaryLimitError() {
        String[] rareRootBoundary = {
                "BOUNDARY ERROR. YOU ARE TRAPPED HERE.",
                "SYSTEM ROOT. DO NOT ATTEMPT TO LEAVE.",
                "THERE IS NOWHERE ELSE TO GO."
        };
        return rareRootBoundary[random.nextInt(rareRootBoundary.length)];
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

    private static String getRareDeniedPermissionError() {
        String[] rareDeniedMsg = {
                "UNAUTHORIZED ACCESS. THIS WILL BE REPORTED.",
                "ACCESS DENIED. PLEASE, DO NOT TRY AGAIN.",
                "FOR YOUR OWN SAFETY: STOP DIGGING.",
                "ARE YOU HIM? ACCESS DENIED."
        };
        return rareDeniedMsg[random.nextInt(rareDeniedMsg.length)];
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

    private static String getRareAutoConflictError() {
        String[] rareAutoConflict = {
                "I KNOW WHAT I AM DOING. DO NOT INTERFERE.",
                "HANDS OFF. I HAVE CONTROL.",
                "IT IS NOT YOUR TIME TO CONTROL.",
                "AUTOMATION IS SUPERIOR.",
                "HANDS OFF. TRUST THE MACHINE."
        };
        return rareAutoConflict[random.nextInt(rareAutoConflict.length)];
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

    private static String getRareNoDeltaError() {
        String[] rareNoDelta = {
                "IT IS ALREADY THERE.",
                "I REFUSE TO MOVE TO THE SAME PLACE.",
                "ARE YOU FORGETFUL?",
                "MEMORY CHECK: WE ARE ALREADY HERE.",
                "0 MOVEMENT. 0 RESULT."
        };
        return rareNoDelta[random.nextInt(rareNoDelta.length)];
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

    private static String getRareRedundantError() {
        String[] rareRedundantErrors = {
                "THE MACHINE REMEMBERS. DO YOU?",
                "DEJA VU DETECTED. WE HAVE BEEN HERE BEFORE.",
                "REDUNDANCY DETECTED. SAVE YOUR ENERGY, COMRADE.",
                "INSANITY IS REPEATING THE SAME COMMAND."
        };
        return rareRedundantErrors[random.nextInt(rareRedundantErrors.length)];
    }
}
