package fr.m1comp5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class SymbolTableTest {

    SymbolTable symbolTable;

    @BeforeEach
    public void setUp() {
        symbolTable = new SymbolTable();
    }

    @Test
    public void popScopeEmpty(){
        Assertions.assertThrows(SymbolTableException.class, () -> {
            symbolTable.popScope();
        });
    }

    @Test
    public void popScope() {
        symbolTable.newScope();
        Assertions.assertDoesNotThrow(() -> {
            symbolTable.popScope();
        });
    }

    @Test
    public void putObjectInEmptyScopes(){
        MemoryObject mo = new MemoryObject("x", null, ObjectNature.VAR, ObjectType.INT);
        Assertions.assertThrows(SymbolTableException.class, () -> {
            symbolTable.putObjectInCurrentScope(mo);
        });
    }

    @Test
    public void putObjectInScope(){
        symbolTable.newScope();
        MemoryObject mo = new MemoryObject("x", null, ObjectNature.VAR, ObjectType.INT);
        Assertions.assertDoesNotThrow(() -> {
            symbolTable.putObjectInCurrentScope(mo);
        });
    }

    @Test
    public void putObjectAlreadyInScope(){
        symbolTable.newScope();
        MemoryObject mo = new MemoryObject("x", null, ObjectNature.VAR, ObjectType.INT);
        Assertions.assertDoesNotThrow(() -> {
            symbolTable.putObjectInCurrentScope(mo);
        });
        Assertions.assertThrows(SymbolTableException.class, () -> {
            symbolTable.putObjectInCurrentScope(mo);
        });
    }

    @Test
    public void removeObjectInEmptyScopes(){
        MemoryObject mo = new MemoryObject("x", null, ObjectNature.VAR, ObjectType.INT);
        Assertions.assertThrows(SymbolTableException.class, () -> {
            symbolTable.removeObjectFromCurrentScope(mo);
        });
    }

    @Test
    public void removeObjectInScope(){
        symbolTable.newScope();
        MemoryObject mo = new MemoryObject("x", null, ObjectNature.VAR, ObjectType.INT);
        Assertions.assertDoesNotThrow(() -> {
            symbolTable.putObjectInCurrentScope(mo);
        });
        Assertions.assertDoesNotThrow(() -> {
            symbolTable.removeObjectFromCurrentScope(mo);
        });
    }

    @Test
    public void getEmptyScopes(){
        Assertions.assertThrows(SymbolTableException.class, () -> {
            MemoryObject mo = symbolTable.get("a");
        });
    }

    @Test
    public void getWithNoObjectInScopes(){
        symbolTable.newScope();
        Assertions.assertThrows(SymbolTableException.class, () -> {
            MemoryObject mo = symbolTable.get("a");
        });
    }

    @Test
    public void getObject() throws SymbolTableException {
        symbolTable.newScope();
        MemoryObject mo = new MemoryObject("a", null, ObjectNature.VAR, ObjectType.INT);
        Assertions.assertDoesNotThrow(() -> {
            symbolTable.putObjectInCurrentScope(mo);
        });
        Assertions.assertEquals(mo, symbolTable.get("a"));
    }

    @Test
    public void updateEmptyScopes(){
        Assertions.assertThrows(SymbolTableException.class, () -> {
            MemoryObject mo = new MemoryObject("a", null, ObjectNature.VAR, ObjectType.INT);
            symbolTable.updateObjInCurrentScope("a", 1);
        });
    }

    @Test
    public void updateObjectInScopeWithNoPutBefore(){
        symbolTable.newScope();
        Assertions.assertDoesNotThrow(() -> {
            symbolTable.updateObjInCurrentScope("a", 1);
        });
    }

    @Test
    public void updateObjectInScopeWithObjectPutBefore(){
        symbolTable.newScope();
        Assertions.assertDoesNotThrow(() -> {
            MemoryObject mo = new MemoryObject("a", null, ObjectNature.VAR, ObjectType.INT);
            symbolTable.putObjectInCurrentScope(mo);
            symbolTable.updateObjInCurrentScope("a", 1);
        });
    }

    @Test
    public void newScopeFromListOfObject() throws SymbolTableException {
        List<MemoryObject> lmo = new ArrayList<>();
        MemoryObject mo = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        MemoryObject mo1 = new MemoryObject("id1", 1, ObjectNature.VAR, ObjectType.INT);
        MemoryObject mo2 = new MemoryObject("id2", 1, ObjectNature.VAR, ObjectType.INT);
        lmo.add(mo);
        lmo.add(mo1);
        lmo.add(mo2);
        symbolTable.newScopeFromListOfObject(lmo);
        List<HashTable> scopes = symbolTable.getScopes();
        Assertions.assertEquals(scopes.size(), 1);
        for (int i = 0; i < lmo.size(); ++i) {
            Assertions.assertEquals(lmo.get(i), scopes.get(0).get(lmo.get(i).getId()));
        }
    }

    //TODO: demander à quoi sert la fonction getMainScopeSymboles

}
