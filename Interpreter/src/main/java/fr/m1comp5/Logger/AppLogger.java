package fr.m1comp5.Logger;

import java.util.HashSet;
import java.util.Set;

public class AppLogger {

    public enum TypeMessage {
        INFO,
        DEBUG,
        ERROR
    }

    private static boolean debugModeEnabled = false; // Mode de débogage actuel (par défaut : désactivé)


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

    private void notifyListeners(String message, TypeMessage typeMessage) {
        for (LoggerListener listener : loggerListeners) {
            listener.onLog(message, typeMessage);
        }
    }

    public void logInfo(String message) {
        if (debugModeEnabled) {
            notifyListeners("INFO: " + message, TypeMessage.INFO);
        }
        System.out.println("INFO: " + message);
    }

    public void logDebug(String message) {
        if (debugModeEnabled) {
            notifyListeners("DEBUG: " + message, TypeMessage.DEBUG);
        }
        System.out.println("DEBUG: " + message);
    }

    public void logError(String message) {
        if (debugModeEnabled) {
            notifyListeners("ERROR: " + message, TypeMessage.ERROR);
        }
        System.err.println("ERROR: " + message);
    }
}