package fr.m1comp5.Logger;

public interface LoggerListener {
    void onLog(String message, AppLogger.TypeMessage t);
}