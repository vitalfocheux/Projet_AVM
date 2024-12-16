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

    public enum LogLevel {
        ERROR_ONLY,
        INFO_ERROR_DEBUG
    }


    private Set<LoggerListener> loggerListeners = new HashSet<>();
    private static AppLogger instance;
    private PrintWriter logWriter;
    private Set<String> loggedMessages = new HashSet<>(); // Ensemble pour stocker les messages déjà logués
    private LogLevel logLevel = LogLevel.INFO_ERROR_DEBUG; 


    private AppLogger() {
        try {
            // fichier de log contient les logs de l'application
            java.io.File logDirectory = new java.io.File("logs");
            if (!logDirectory.exists()) {
                logDirectory.mkdirs(); 
            }
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
    private void log(String message, TypeMessage typeMessage) {
        if (!loggedMessages.contains(message)) {
            loggedMessages.add(message);
            if (shouldLog(typeMessage)) {
                System.out.println(message);
                writeToFile(message);
                notifyListeners(message, typeMessage);
            }
        }
    }

    private boolean shouldLog(TypeMessage typeMessage) {
        if (logLevel == LogLevel.ERROR_ONLY && typeMessage != TypeMessage.ERROR) {
            return false;
        }
        return true;
    }
    
    public void setLogLevel(LogLevel level) {
        this.logLevel = level;
    }
    
    public void logInfo(String message) {
        String formattedMessage = "INFO: " + message;
        log(formattedMessage, TypeMessage.INFO);
    }

    public void logDebug(String message) {
        String formattedMessage = "DEBUG: " + message;
        log(formattedMessage, TypeMessage.DEBUG);
    }

    public void logError(String message, int line, int column) {
        String formattedMessage = "ERROR: " + message + " (ligne " + line + ", colonne " + column + ")";
        log(formattedMessage, TypeMessage.ERROR);
    }

    public void close() {
        if (logWriter != null) {
            logWriter.close();
        }
    }
}