package fr.m1comp5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Test
    public void assignMeth() throws SymbolTableException, StackException, HeapException {
        mem.getSymbolTable().newScope();
        mem.declMeth("main", "methmain", ObjectType.VOID);
        Assertions.assertEquals(mem.getType("main"), ObjectType.VOID);
        Assertions.assertEquals(mem.getNature("main"), ObjectNature.METH);
        Assertions.assertEquals(mem.getVal("main"), "methmain");
        MemoryObject mo = mem.assignValue("main", "methmain2");
        Assertions.assertEquals(mo.getId(), "main");
        Assertions.assertEquals(mo.getNature(), ObjectNature.METH);
        Assertions.assertEquals(mo.getType(), ObjectType.VOID);
        Assertions.assertNotEquals(mo.getValue(), "methmain2");
        Assertions.assertEquals(mo.getValue(), "methmain");
    }

    @Test
    public void assignMethodDoesntExist() {
        mem.getSymbolTable().newScope();
        Assertions.assertThrows(StackException.class, () -> {
            mem.assignValue("main", "methmain2");
        });
    }

    @Test
    public void assignMethodNotInitialised() throws SymbolTableException, StackException, HeapException {
        mem.getSymbolTable().newScope();
        mem.declMeth("main", null, ObjectType.VOID);
        Assertions.assertThrows(RuntimeException.class, () -> {
            mem.assignValue("main", "methmain");
            Assertions.assertEquals(mem.getVal("main"), "methmain");
        });
    }

    @Test
    public void assignMethodWithNull() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        mem.declMeth("main", "methmain", ObjectType.VOID);
        Assertions.assertThrows(RuntimeException.class, () -> {
            mem.assignValue("main", null);
        });
    }

    @Test
    public void declTab() throws SymbolTableException, HeapException, StackException {
        mem.getSymbolTable().newScope();
        mem.declTab("tab", 5, ObjectType.INT);
        Assertions.assertEquals(mem.getVal("tab"), 0);
        Assertions.assertEquals(mem.getNature("tab"), ObjectNature.TAB);
        Assertions.assertEquals(mem.getType("tab"), ObjectType.INT);
    }

    @Test
    public void declTabWithNotInteger() throws SymbolTableException, HeapException, StackException {
        mem.getSymbolTable().newScope();
        Assertions.assertThrows(java.lang.ClassCastException.class, () -> {
            mem.declTab("tab", "notInteger", ObjectType.INT);
        });
    }

    @Test
    public void assignTab() throws SymbolTableException, HeapException, StackException {
        mem.getSymbolTable().newScope();
        mem.declTab("tab", 5, ObjectType.INT);
        mem.assignValue("tab", 0);
        Map<Integer, List<HeapBlock>> blocks = mem.getHeap().getBlocks();
        List<HeapBlock> block = blocks.get(8);
        Assertions.assertEquals(block.get(0).getAddress(), 0);
        Assertions.assertEquals(block.get(0).getSize(), 8);
        Assertions.assertFalse(block.get(0).isFree());
        Assertions.assertEquals(block.get(1).getAddress(), 8);
        Assertions.assertEquals(block.get(1).getSize(), 8);
        Assertions.assertTrue(block.get(1).isFree());
    }

    //TODO: revoir pour que ça lève la bonne exception surement avec Mock
    @Test
    public void assignTabWithNotInteger() throws SymbolTableException, HeapException, StackException {
        mem.getSymbolTable().newScope();
        mem.declTab("tab", 5, ObjectType.INT);
        Assertions.assertThrows(RuntimeException.class, () -> {
            mem.assignValue("tab", "notInteger");
        });
    }

    @Test
    public void removeDeclDoesntExist(){
        mem.getSymbolTable().newScope();
        Assertions.assertThrows(SymbolTableException.class, () -> {
            mem.removeDecl("x");
        });
    }

    @Test
    public void removeDecl() throws SymbolTableException, StackException, HeapException {
        mem.getSymbolTable().newScope();
        mem.declVar("x", 1, ObjectType.INT);
        Assertions.assertEquals(mem.getVal("x"), 1);
        Assertions.assertEquals(mem.getNature("x"), ObjectNature.VAR);
        Assertions.assertEquals(mem.getStack().get(0), new MemoryObject("x", 1, ObjectNature.VAR, ObjectType.INT));
        Assertions.assertEquals(mem.getType("x"), ObjectType.INT);
        mem.removeDecl("x");
        Assertions.assertThrows(SymbolTableException.class, () -> {
            mem.getVal("x");
        });
        Assertions.assertTrue(mem.getStack().empty());
    }

    @Test
    public void removeDeclTab() throws SymbolTableException, HeapException, StackException {
        mem.getSymbolTable().newScope();
        mem.declTab("tab", 5, ObjectType.INT);
        Map<Integer, List<HeapBlock>> blocks = mem.getHeap().getBlocks();
        List<HeapBlock> block = blocks.get(8);
        Assertions.assertEquals(block.get(0).getAddress(), 0);
        Assertions.assertEquals(block.get(0).getSize(), 8);
        Assertions.assertFalse(block.get(0).isFree());
        Assertions.assertEquals(block.get(1).getAddress(), 8);
        Assertions.assertEquals(block.get(1).getSize(), 8);
        Assertions.assertTrue(block.get(1).isFree());
        mem.removeDecl("tab");
        blocks = mem.getHeap().getBlocks();
        Assertions.assertTrue(blocks.get(8).isEmpty());
        Assertions.assertTrue(blocks.get(16).isEmpty());
        Assertions.assertTrue(blocks.get(32).isEmpty());
        Assertions.assertTrue(blocks.get(64).isEmpty());
        Assertions.assertTrue(blocks.get(128).isEmpty());
        Assertions.assertTrue(blocks.get(256).isEmpty());
        Assertions.assertTrue(blocks.get(512).isEmpty());
        Assertions.assertTrue(blocks.get(1024).get(0).isFree());
        Assertions.assertEquals(blocks.get(1024).get(0).getAddress(), 0);
    }

    @Test
    public void assignValueArrayNotTab() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        mem.declVar("x", 1, ObjectType.INT);
        Assertions.assertThrows(RuntimeException.class, () -> {
            mem.assignValueArray("x", 0, 1);
        });
    }

    @Test
    public void assignValueArrayWithNotIntegerForIndex() throws SymbolTableException, StackException, HeapException {
        mem.getSymbolTable().newScope();
        mem.declTab("x", 5, ObjectType.INT);
        Assertions.assertThrows(RuntimeException.class, () -> {
            mem.assignValueArray("x", "1", 1);
        });
    }

    @Test
    public void assignValueArray() throws SymbolTableException, HeapException, StackException {
        mem.getSymbolTable().newScope();
        int size = 5;
        mem.declTab("x", 5, ObjectType.INT);
        for(int i = 0; i < size; ++i){
            mem.assignValueArray("x", i, i);
            Assertions.assertEquals(mem.getHeap().accessValue(0, i), i);
        }
    }

    @Test
    public void assignTypeWithIDDoesntExistAndScopeEmpty(){
        mem.getSymbolTable().newScope();
        Assertions.assertThrows(StackException.class, () -> {
            mem.assignType("x", ObjectType.INT);
        });
    }

    @Test
    public void assignTypeWithIDDoesntExist() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        mem.declVar("x", 1, ObjectType.INT);
        mem.assignType("y", ObjectType.INT);
        Assertions.assertThrows(SymbolTableException.class, () -> {
            Assertions.assertEquals(mem.getType("y"), ObjectType.INT);
        });
        Assertions.assertEquals(mem.getStack().size(), 1);
    }

    @Test
    public void assignType() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        mem.declVar("x", 1, ObjectType.INT);
        mem.assignType("x", ObjectType.BOOLEAN);
        Assertions.assertEquals(mem.getType("x"), ObjectType.BOOLEAN);
    }

    @Test
    public void classVariable() throws StackException, SymbolTableException {
        mem.getSymbolTable().newScope();
        mem.declVar("x", 1, ObjectType.INT);
        Assertions.assertEquals(mem.classVariable(), "x");
    }

    @Test
    public void classVariableEmpty() {
        mem.getSymbolTable().newScope();
        Assertions.assertThrows(StackException.class, () -> {
            mem.classVariable();
        });
    }

    @Test
    public void getParamsVar() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        mem.declVar("x", 1, ObjectType.INT);
        Assertions.assertNull(mem.getParams("x"));
    }

    //TODO: A revoir pour insérer des entetes de fonctions
    @Test
    public void getParams() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();

    }

    @Test
    public void declVarsFromListOfParamsEmpty() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        mem.declVarsFromListOfParams(new ArrayList<>());
        Assertions.assertEquals(mem.getStack().size(), 0);
    }

    @Test
    public void declVarsFromListOfParamsNull() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        mem.declVarsFromListOfParams(null);
        Assertions.assertEquals(mem.getStack().size(), 0);
    }

    @Test
    public void declVarsFromListOfParams() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        List<MemoryObject> fctParams = new ArrayList<>();
        MemoryObject mo = new MemoryObject("x", 1, ObjectNature.VAR, ObjectType.INT);
        MemoryObject mo2 = new MemoryObject("y", 2, ObjectNature.VAR, ObjectType.INT);
        fctParams.add(mo);
        fctParams.add(mo2);
        mem.declVarsFromListOfParams(fctParams);
        Assertions.assertEquals(mem.getStack().size(), 2);
        Assertions.assertEquals(mem.getStack().get(0), mo);
        Assertions.assertEquals(mem.getStack().get(1), mo2);
    }
}
