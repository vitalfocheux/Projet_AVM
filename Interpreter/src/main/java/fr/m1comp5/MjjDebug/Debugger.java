package fr.m1comp5.MjjDebug;

public interface Debugger extends Runnable {
    void setDebugger(InterpreterDebugger debugger);
    void run();
}