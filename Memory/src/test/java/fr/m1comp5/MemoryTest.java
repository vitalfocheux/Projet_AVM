package fr.m1comp5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MemoryTest {

    Memory mem;

    @BeforeEach
    public void setup() throws HeapException {
        mem = new Memory();
    }

    @Test
    public void declVar() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        mem.declVar("x", 1, ObjectType.INT);
        Assertions.assertEquals(mem.getVal("x"), 1);
        Assertions.assertEquals(mem.getNature("x"), ObjectNature.VAR);
        Assertions.assertEquals(mem.getStack().get(0), new MemoryObject("x", 1, ObjectNature.VAR, ObjectType.INT));
        Assertions.assertEquals(mem.getType("x"), ObjectType.INT);
    }

    @Test
    public void declVarNotInitialisedWithNull() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        mem.declVar("x", null, ObjectType.OMEGA);
        Assertions.assertThrows(SymbolTableException.class, () -> {
            Assertions.assertNull(mem.getVal("x"));
        });
    }

    @Test
    public void declVarNotInitialisedWithOmega() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        mem.declVar("x", ObjectType.OMEGA, ObjectType.OMEGA);
        Assertions.assertThrows(SymbolTableException.class, () -> {
           Assertions.assertEquals(mem.getVal("x"), ObjectType.OMEGA);
        });
    }

    @Test
    public void getValDoesntExist(){
        mem.getSymbolTable().newScope();
        Assertions.assertThrows(SymbolTableException.class, () -> {
           Assertions.assertEquals(mem.getVal("x"), 1);
        });
    }



}
