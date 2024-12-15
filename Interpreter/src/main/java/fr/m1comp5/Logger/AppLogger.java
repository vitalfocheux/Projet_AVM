package fr.m1comp5.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
    private PrintWriter logWriter;

    private AppLogger() {
        try {
            // Vérifie que le répertoire parent existe, sinon le crée
            java.io.File logDirectory = new java.io.File("logs");
            if (!logDirectory.exists()) {
                logDirectory.mkdirs(); // Crée le répertoire, y compris les répertoires parents manquants
            }
    
            // Initialise le fichier de log
            logWriter = new PrintWriter(new FileWriter("logs/app.log", false), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    private void writeToFile(String message) {
        if (logWriter != null) {
            logWriter.println(message);
        }
    }


    public void logInfo(String message) {
        String formattedMessage = "INFO: " + message;
        System.out.println(formattedMessage);
        writeToFile(formattedMessage);
        notifyListeners(formattedMessage, TypeMessage.INFO);
    }

    public void logDebug(String message) {
        String formattedMessage = "DEBUG: " + message;
        System.out.println(formattedMessage);
        writeToFile(formattedMessage);
        notifyListeners(formattedMessage, TypeMessage.DEBUG);
    }

    public void logError(String message, int line, int column) {
        String formattedMessage = "ERROR: " + message + " (ligne " + line + ", colonne " + column + ")";
        System.out.println(formattedMessage);
        writeToFile(formattedMessage);
        notifyListeners(formattedMessage, TypeMessage.ERROR);
    }

    public void close() {
        if (logWriter != null) {
            logWriter.close();
        }
    }
}