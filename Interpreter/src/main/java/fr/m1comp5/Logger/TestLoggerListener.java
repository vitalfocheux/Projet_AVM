package fr.m1comp5.Logger;

import  java.util.ArrayList;
import java.util.List;

public class TestLoggerListener implements LoggerListener {

    private List<String> messages = new ArrayList<>();
    private List<AppLogger.TypeMessage> levels = new ArrayList<>();

    @Override
    public void onLog(String message, AppLogger.TypeMessage level) {
        messages.add(message);
        levels.add(level);
    }

    public List<String> getMessages() {
        return messages;
    }

    public List<AppLogger.TypeMessage> getLevels() {
        return levels;
    }

    public void clear() {
        messages.clear();
        levels.clear();
    }
}
