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

    @Test
    public void assignValueVar() throws SymbolTableException, StackException, HeapException {
        mem.getSymbolTable().newScope();
        mem.declVar("x", 1, ObjectType.INT);
        mem.assignValue("x", 2);
        Assertions.assertEquals(mem.getVal("x"), 2);
    }

    @Test
    public void assignValueNoneVarWasDecl(){
        mem.getSymbolTable().newScope();
        Assertions.assertThrows(StackException.class, () -> {
            mem.assignValue("x", 2);
        });
    }

    @Test
    public void assignValueDoesntExist() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        mem.declVar("y", 1, ObjectType.INT);
        Assertions.assertThrows(SymbolTableException.class, () -> {
            mem.assignValue("x", 2);
        });
    }

    @Test
    public void assignValueNotInitialised() throws SymbolTableException, StackException, HeapException {
        mem.getSymbolTable().newScope();
        mem.declVar("x", null, ObjectType.OMEGA);
        mem.assignValue("x", 2);
    }

    @Test
    public void assignValueWithNull() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        mem.declVar("x", null, ObjectType.OMEGA);
        Assertions.assertThrows(RuntimeException.class, () -> {
            mem.assignValue("x", null);
        });
    }

    @Test
    public void assignValueAnIntToBool() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        mem.declVar("x", 1, ObjectType.INT);
        Assertions.assertThrows(RuntimeException.class, () -> {
            mem.assignValue("x", true);
        });
    }

    //TODO: Pas sur que ça sois normal de pouvoir faire ça à revoir
//    @Test
//    public void assignValueAnIntToBoolOmega() throws SymbolTableException, StackException, HeapException {
//        mem.getSymbolTable().newScope();
//        mem.declVar("x", "OMEGA", ObjectType.INT);
//        mem.assignValue("x", true);
//        System.err.println(mem.getVal("x").getClass());
//    }

    @Test
    public void getValCst() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        mem.declCst("x", 1, ObjectType.INT);
        Assertions.assertEquals(mem.getVal("x"), 1);
    }

    @Test
    public void assignCst() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        mem.declCst("x", 1, ObjectType.INT);
        Assertions.assertThrows(RuntimeException.class, () -> {
            mem.assignValue("x", 2);
        });
    }

    @Test
    public void assignCstDoesntExist() {
        mem.getSymbolTable().newScope();
        Assertions.assertThrows(StackException.class, () -> {
            mem.assignValue("x", 2);
        });
    }

    @Test
    public void assignCstNotInitialised() throws SymbolTableException, StackException, HeapException {
        mem.getSymbolTable().newScope();
        mem.declCst("x", null, ObjectType.OMEGA);
        mem.assignValue("x", 2);
        Assertions.assertEquals(mem.getVal("x"), 2);
        Assertions.assertEquals(mem.getType("x"), ObjectType.OMEGA);
    }

    @Test
    public void assignCstWithNull() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        mem.declCst("x", 1, ObjectType.INT);
        Assertions.assertThrows(RuntimeException.class, () -> {
            mem.assignValue("x", null);
        });
    }

}
