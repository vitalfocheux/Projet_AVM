package fr.m1comp5;

import fr.m1comp5.Memory.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SymbolTableTest {

    SymbolTable symbolTable;

    @BeforeEach
    public void setup(){
        symbolTable = new SymbolTable();
    }

    @Test
    public void testGetBucketsEmpty(){
        List<List<MemoryObject>> buckets = symbolTable.getBuckets();
        for (List<MemoryObject> bucket : buckets) {
            Assertions.assertNull(bucket);
        }
    }

    @Test
    public void testPutNull() throws SymbolTableException
    {
        Assertions.assertFalse(symbolTable.put(null));
        Assertions.assertEquals(symbolTable.getCount(), 0);
        Assertions.assertEquals(symbolTable.getSize(), 32);
    }

    @Test
    public void testPut() throws SymbolTableException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        Assertions.assertTrue(symbolTable.put(obj));
        Assertions.assertEquals(symbolTable.getCount(), 1);
        Assertions.assertEquals(symbolTable.getSize(), 32);
        Assertions.assertEquals(obj, symbolTable.get("id"));
    }

    @Test
    public void testPutMany() throws SymbolTableException {
        int size = symbolTable.getSize();
        for(int i = 0; i < size; ++i){
            MemoryObject obj = new MemoryObject("id"+i, i, ObjectNature.VAR, ObjectType.INT);
            Assertions.assertTrue(symbolTable.put(obj));
            Assertions.assertEquals(symbolTable.getCount(), i+1);
            Assertions.assertEquals(obj, symbolTable.get("id"+i));
        }
        Assertions.assertNotEquals(size, symbolTable.getSize());
    }

    @Test
    public void testPutSameTwice() throws SymbolTableException{
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        Assertions.assertTrue(symbolTable.put(obj));
        Assertions.assertThrows(SymbolTableException.class, () -> {symbolTable.put(obj);});
        Assertions.assertEquals(symbolTable.getCount(), 1);
        Assertions.assertEquals(obj, symbolTable.get("id"));
    }

    @Test
    public void testGetEmpty(){
        Assertions.assertNull(symbolTable.get("id"));
    }

    @Test
    public void testGetNotEmptyReturnNull() throws SymbolTableException {
        int size = symbolTable.getSize();
        for(int i = 0; i < size; ++i){
            MemoryObject obj = new MemoryObject("id"+i, i, ObjectNature.VAR, ObjectType.INT);
            Assertions.assertTrue(symbolTable.put(obj));
            Assertions.assertEquals(symbolTable.getCount(), i+1);
            Assertions.assertEquals(obj, symbolTable.get("id"+i));
            if(symbolTable.getSize() == size){
                Assertions.assertNull(symbolTable.get("id"+(i+1)));
            }
        }
    }

    @Test
    public void testRemoveNull(){
        Assertions.assertFalse(symbolTable.remove(null));
    }

    @Test
    public void testRemoveEmpty(){
        Assertions.assertFalse(symbolTable.remove(new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT)));
    }

    @Test
    public void testRemove() throws SymbolTableException {
        int size = symbolTable.getSize();
        for(int i = 0; i < size; ++i){
            MemoryObject obj = new MemoryObject("id"+i, i, ObjectNature.VAR, ObjectType.INT);
            Assertions.assertTrue(symbolTable.put(obj));
            Assertions.assertEquals(symbolTable.getCount(), i+1);
        }

        Assertions.assertFalse(symbolTable.remove(new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT)));

        for(int i = 0; i < size; ++i){
            MemoryObject obj = new MemoryObject("id"+i, i, ObjectNature.VAR, ObjectType.INT);
            Assertions.assertTrue(symbolTable.remove(obj));
            Assertions.assertEquals(symbolTable.getCount(), size-i-1);
            Assertions.assertNull(symbolTable.get("id"+i));
        }
    }

    @Test
    public void testStressed() throws SymbolTableException {
        for(int i = 0; i < 10000; ++i){
            MemoryObject obj = new MemoryObject("id"+i, i, ObjectNature.VAR, ObjectType.INT);
            Assertions.assertTrue(symbolTable.put(obj));
        }

        for(int i = 0; i < 10000; ++i){
            MemoryObject obj = symbolTable.get("id"+i);
            Assertions.assertEquals(obj.getId(), "id"+i);
            Assertions.assertEquals(obj.getValue(), i);
        }

        for(int i = 0; i < 10000; ++i){
            MemoryObject obj = new MemoryObject("id"+i, i, ObjectNature.VAR, ObjectType.INT);
            Assertions.assertTrue(symbolTable.remove(obj));
        }
    }
}
