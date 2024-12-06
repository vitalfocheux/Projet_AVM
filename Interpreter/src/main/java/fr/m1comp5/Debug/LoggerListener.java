package fr.m1comp5.Debug;

public interface LoggerListener {
    void onLog(String message, AppLogger.TypeMessage typeMessage);
}