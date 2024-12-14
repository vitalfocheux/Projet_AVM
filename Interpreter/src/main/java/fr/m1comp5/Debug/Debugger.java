package fr.m1comp5.Debug;

public interface Debugger extends Runnable {
    void setDebugger(InterpreterDebugger debugger);
    void run();
}