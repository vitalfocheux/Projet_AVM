package fr.m1comp5.MjjDebug;

import fr.m1comp5.mjj.generated.Node;

/* 
* handle events
* triggered during the debugging process, a breakpoint is hit, or the interpreter is paused/finished.
*/
public interface DebugEventHandler {
    void handle(boolean isEnded, Node node);
}
