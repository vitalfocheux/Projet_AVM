package fr.m1comp5.Debug;


/* 
* handle events
* triggered during the debugging process, a breakpoint is hit, or the interpreter is paused/finished.
*/
public interface DebugEventHandler {
    void handle(boolean isEnded, fr.m1comp5.jjc.generated.Node node);
    void handle(boolean isEnded, fr.m1comp5.mjj.generated.Node node);
}
