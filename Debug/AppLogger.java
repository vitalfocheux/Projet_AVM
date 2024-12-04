package fr.m1comp5.Debug;

import java.util.HashSet;
import java.util.Set;

public class AppLogger {

    public enum TypeMessage {
        INFO,
        DEBUG,
        ERROR
    }

    private static boolean debugModeEnabled = false; // Mode de débogage actuel (par défaut : désactivé)

    /*public interface Listener {
        void receiveMessage(String message, NivMessage level);
    }*/

    private Set<LoggerListener> loggerListeners = new HashSet<>();
    private static AppLogger instance;

    private AppLogger() {}

    public static AppLogger getInstance() {
        if (instance == null) {
            instance = new AppLogger();
        }
        return instance;
    }

    public static void setDebugMode(boolean enabled) {
        debugModeEnabled = enabled;
    }

    public void addLoggerListener(LoggerListener listener) {
        loggerListeners.add(listener);
    }

    public void removeLoggerListener(LoggerListener listener) {
        loggerListeners.remove(listener);
    }

    private void notifyListeners(String message, NivMessage level) {
        for (LoggerListener listener : loggerListeners) {
            listener.onLog(message, level);
        }
    }

    private String formaTypeMessage(String message, PositionError position) {
        if (position != null) {
            return message + " at line " + position.getLine() + ", column " + position.getColumn();
        }
        return message;
    }

    public void logInfo(String message, PositionError position) {
        String formattedMessage = formaTypeMessage(message, position);
        if (debugModeEnabled) {
            notifyListeners("INFO: " + formattedMessage, NivMessage.INFO);
        }
        System.out.println("INFO: " + message);
    }

    public void logDebug(String message, PositionError position) {
        String formattedMessage = formaTypeMessage(message, position);
        if (debugModeEnabled) {
            notifyListeners("DEBUG: " + formattedMessage, NivMessage.DEBUG);
        }
        System.out.println("DEBUG: " + formattedMessage);
    }

    public void logError(String message, PositionError position) {
        String formattedMessage = formaTypeMessage(message, position);
        if (debugModeEnabled) {
            notifyListeners("ERROR: " + formattedMessage, NivMessage.ERROR);
        }
        System.err.println("ERROR: " + formattedMessage);
    }
}