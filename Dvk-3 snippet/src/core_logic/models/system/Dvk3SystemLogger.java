package core_logic.models.system;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Dvk3SystemLogger {
    private int clearTimer = -1;
    private final int MAX_HISTORY = 40;
    private boolean isBusy = false;
    int heatingDelay;

    private List<LogEntry> messageHistory = new ArrayList<>();
    private List<PendingMessage> pendingMessage = new ArrayList<>();


    public void processDisplayQueue(int heat) {
        heatingDelay = heat;

        if (clearTimer > 0) {
            clearTimer--;
        }
        if (clearTimer == 0) {
            clearLog();
            clearTimer = -1;
        }
        if (pendingMessage.isEmpty()) {
            isBusy = false;
            return;
        }

        Iterator<PendingMessage> iterator = pendingMessage.iterator();
        while (iterator.hasNext()) {
            PendingMessage pm = iterator.next();
            pm.delayTicks--;

            if (pm.delayTicks <= 0) {
                sysLog(pm.type, pm.message);
                iterator.remove();
            }
        }
    }

    public void userLog(String message, String formattedTime) { // logger
        String prefix = ">> [" + formattedTime + "] ";

        messageHistory.add(new LogEntry(message, prefix, true));

        if (messageHistory.size() > MAX_HISTORY) {
            messageHistory.removeFirst();
        }
    }

    public static enum LogType {
        INFO,
        WARN,
        ERROR,
        CRITICAL,
        SUCCESS
    }

    public void sysLog(LogType type, String message, int delay)  {
        isBusy = true;
        pendingMessage.add(new PendingMessage(type, message,delay + heatingDelay));
    }

    public void sysLog(LogType type, String message) {
        String prefix = switch (type) {
            case INFO -> ">> SYS.OP  :: ";
            case WARN -> "!! WARNING :: ";
            case ERROR -> "** ";
            case CRITICAL -> "## KERNEL  :: ";
            case SUCCESS -> ">> OK      :: ";
        };

        boolean isInstant = (type == LogType.CRITICAL || type == LogType.ERROR || type == LogType.WARN);

        messageHistory.add(new LogEntry(message, prefix, isInstant));
        if (messageHistory.size() > MAX_HISTORY) {
            messageHistory.removeFirst();
        }
    }

    public void callClearLog(int time) {
        clearTimer = time + heatingDelay;
    }

    public void clearLog() {
        messageHistory.clear();
    }

    public List<LogEntry> getHistory() {
        return messageHistory;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public static class PendingMessage {
        LogType type;
        String message;
        int delayTicks;

        public PendingMessage(LogType type,String message, int delayTicks) {
            this.type = type;
            this.message = message;
            this.delayTicks = delayTicks;
        }
    }

    public static class LogEntry {
        private String message;
        private String prefix;
        private long timestamp;
        private boolean instant;

        public LogEntry(String message, String prefix, boolean instant) {
            this.message = message;
            this.prefix = prefix;
            this.timestamp = System.currentTimeMillis();
            this.instant = instant;
        }


        public String getPrefix() {
            return prefix;
        }

        public String getMessage() {
            return message;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public boolean isInstant() {
            return instant;
        }
    }
}
