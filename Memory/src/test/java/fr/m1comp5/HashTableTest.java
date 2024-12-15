package fr.m1comp5;

import fr.m1comp5.Memory.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class HashTableTest {

    HashTable hashTable;

    @BeforeEach
    public void setup(){
        hashTable = new HashTable();
    }

    @Test
    public void testGetBucketsEmpty(){
        List<List<MemoryObject>> buckets = hashTable.getBuckets();
        for (List<MemoryObject> bucket : buckets) {
            Assertions.assertNull(bucket);
        }
    }

    @Test
    public void testPutNull() throws SymbolTableException
    {
        Assertions.assertFalse(hashTable.put(null));
        Assertions.assertEquals(hashTable.getCount(), 0);
        Assertions.assertEquals(hashTable.getSize(), 32);
    }

    @Test
    public void testPut() throws SymbolTableException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        Assertions.assertTrue(hashTable.put(obj));
        Assertions.assertEquals(hashTable.getCount(), 1);
        Assertions.assertEquals(hashTable.getSize(), 32);
        Assertions.assertEquals(obj, hashTable.get("id"));
    }

    @Test
    public void testPutMany() throws SymbolTableException {
        int size = hashTable.getSize();
        for(int i = 0; i < size; ++i){
            MemoryObject obj = new MemoryObject("id"+i, i, ObjectNature.VAR, ObjectType.INT);
            Assertions.assertTrue(hashTable.put(obj));
            Assertions.assertEquals(hashTable.getCount(), i+1);
            Assertions.assertEquals(obj, hashTable.get("id"+i));
        }
        Assertions.assertNotEquals(size, hashTable.getSize());
    }

    @Test
    public void testPutSameTwice() throws SymbolTableException{
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        MemoryObject obj2 = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        Assertions.assertTrue(hashTable.put(obj));
        Assertions.assertThrows(SymbolTableException.class, () -> {hashTable.put(obj2);});
        Assertions.assertEquals(hashTable.getCount(), 1);
        Assertions.assertEquals(obj, hashTable.get("id"));
    }

    @Test
    public void testPutSameIdTwice() throws SymbolTableException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        MemoryObject obj2 = new MemoryObject("id", 2, ObjectNature.VAR, ObjectType.INT);
        Assertions.assertTrue(hashTable.put(obj));
        Assertions.assertThrows(SymbolTableException.class, () -> {hashTable.put(obj2);});
        Assertions.assertEquals(hashTable.getCount(), 1);
        Assertions.assertEquals(obj, hashTable.get("id"));
    }

    @Test
    public void testGetEmpty(){
        Assertions.assertNull(hashTable.get("id"));
    }

    @Test
    public void testGetNotEmptyReturnNull() throws SymbolTableException {
        int size = hashTable.getSize();
        for(int i = 0; i < size; ++i){
            MemoryObject obj = new MemoryObject("id"+i, i, ObjectNature.VAR, ObjectType.INT);
            Assertions.assertTrue(hashTable.put(obj));
            Assertions.assertEquals(hashTable.getCount(), i+1);
            Assertions.assertEquals(obj, hashTable.get("id"+i));
            if(hashTable.getSize() == size){
                Assertions.assertNull(hashTable.get("id"+(i+1)));
            }
        }
    }

    @Test
    public void testRemoveNull(){
        Assertions.assertFalse(hashTable.remove(null));
    }

    @Test
    public void testRemoveEmpty(){
        Assertions.assertFalse(hashTable.remove(new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT)));
    }

    @Test
    public void testRemove() throws SymbolTableException {
        int size = hashTable.getSize();
        for(int i = 0; i < size; ++i){
            MemoryObject obj = new MemoryObject("id"+i, i, ObjectNature.VAR, ObjectType.INT);
            Assertions.assertTrue(hashTable.put(obj));
            Assertions.assertEquals(hashTable.getCount(), i+1);
        }

        Assertions.assertFalse(hashTable.remove(new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT)));

        for(int i = 0; i < size; ++i){
            MemoryObject obj = new MemoryObject("id"+i, i, ObjectNature.VAR, ObjectType.INT);
            Assertions.assertTrue(hashTable.remove(obj));
            Assertions.assertEquals(hashTable.getCount(), size-i-1);
            Assertions.assertNull(hashTable.get("id"+i));
        }
    }

    @Test
    public void testUpdateNullId(){
        Assertions.assertThrows(SymbolTableException.class, () -> {hashTable.update(null, 1);});
    }

    @Test
    public void testUpdateNullValue(){
        Assertions.assertThrows(SymbolTableException.class, () -> {hashTable.update("id", null);});
    }

    @Test
    public void testUpdateNullIdNullValue(){
        Assertions.assertThrows(SymbolTableException.class, () -> {hashTable.update(null, null);});
    }

    @Test
    public void testStressed() throws SymbolTableException {
        for(int i = 0; i < 10000; ++i){
            MemoryObject obj = new MemoryObject("id"+i, i, ObjectNature.VAR, ObjectType.INT);
            Assertions.assertTrue(hashTable.put(obj));
        }

        for(int i = 0; i < 10000; ++i){
            MemoryObject obj = hashTable.get("id"+i);
            Assertions.assertEquals(obj.getId(), "id"+i);
            Assertions.assertEquals(obj.getValue(), i);
        }

        for(int i = 0; i < 10000; ++i){
            MemoryObject obj = new MemoryObject("id"+i, i, ObjectNature.VAR, ObjectType.INT);
            Assertions.assertTrue(hashTable.remove(obj));
        }
    }
}
