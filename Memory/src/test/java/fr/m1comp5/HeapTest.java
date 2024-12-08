package fr.m1comp5;

import fr.m1comp5.Memory.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class HeapTest {

    private boolean isPowerOf(int n){
        if(n == 0){
            return false;
        }
        double a = Math.log(n) / Math.log(2);
        return Math.ceil(a) == Math.floor(a);
    }

    Heap heap;

    @BeforeEach
    public void setup() throws HeapException {
        heap = new Heap();
    }

    @Test
    public void initHeapZero(){
        Assertions.assertThrows(HeapException.class, () -> {
            Heap heap = new Heap(0);
        });
    }

    @Test
    public void init(){
        Assertions.assertTrue(isPowerOf(heap.getHeapSize()));
    }

    @Test
    public void getHeapElementEmpty(){
        Assertions.assertThrows(HeapException.class, () -> {
            heap.accessValue(0, 0);
        });
    }

    @Test
    public void allocatePowerOfTwo() throws HeapException {
        int address = heap.allocateInHeap(2, ObjectType.INT, 1);
        Object element = heap.accessValue(address, 0);
        Assertions.assertEquals(1, element);
        Assertions.assertEquals(2, heap.getSizeOfElement(address));
        Assertions.assertEquals(ObjectType.INT, heap.getTypeOfElement(address));
    }

    @Test
    public void allocateNotPowerOfTwo() throws HeapException {
        int address = heap.allocateInHeap(1025, ObjectType.INT, 42);
        Object element = heap.accessValue(address, 0);
        Assertions.assertEquals(42, element);
        Assertions.assertEquals(1025, heap.getSizeOfElement(address));
        Assertions.assertEquals(ObjectType.INT, heap.getTypeOfElement(address));
        Assertions.assertTrue(isPowerOf(heap.getHeapSize()));
    }

    @Test
    public void allocateStressed() throws HeapException {
        int size = heap.getHeapSize();
        for(int i = 1; i < size; ++i){
            int address = heap.allocateInHeap(i, ObjectType.INT, i);
            Object element = heap.accessValue(address, 0);
            Assertions.assertEquals(i, element);
            Assertions.assertEquals(i, heap.getSizeOfElement(address));
            Assertions.assertEquals(ObjectType.INT, heap.getTypeOfElement(address));
        }
        Assertions.assertTrue(isPowerOf(heap.getHeapSize()));
        Assertions.assertTrue(heap.getHeapSize() > size);
    }

    @Test
    public void allocateNegSize() throws HeapException {
        int address = heap.allocateInHeap(-1, ObjectType.INT, 42);
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            Assertions.assertEquals(42, heap.accessValue(address, 0));
            Assertions.assertEquals(-1, heap.getSizeOfElement(address));
            Assertions.assertEquals(ObjectType.INT, heap.getTypeOfElement(address));
        });
    }

    @Test
    public void allocateINT() throws HeapException {
        int address = heap.allocateInHeap(1, ObjectType.INT);
        Assertions.assertEquals(0, heap.accessValue(address, 0));
        Assertions.assertEquals(1, heap.getSizeOfElement(address));
        Assertions.assertEquals(ObjectType.INT, heap.getTypeOfElement(address));
    }

    @Test
    public void allocateBOOL() throws HeapException {
        int address = heap.allocateInHeap(1, ObjectType.BOOLEAN);
        Assertions.assertEquals(false, heap.accessValue(address, 0));
        Assertions.assertEquals(1, heap.getSizeOfElement(address));
        Assertions.assertEquals(ObjectType.BOOLEAN, heap.getTypeOfElement(address));
    }

    @Test
    public void allocateVOID() throws HeapException {
        int address = heap.allocateInHeap(1, ObjectType.VOID);
        Assertions.assertEquals(null, heap.accessValue(address, 0));
        Assertions.assertEquals(1, heap.getSizeOfElement(address));
        Assertions.assertEquals(ObjectType.VOID, heap.getTypeOfElement(address));
    }

    @Test
    public void allocateOMEGA() throws HeapException {
        int address = heap.allocateInHeap(1, ObjectType.OMEGA);
        Assertions.assertEquals(null, heap.accessValue(address, 0));
        Assertions.assertEquals(1, heap.getSizeOfElement(address));
        Assertions.assertEquals(ObjectType.OMEGA, heap.getTypeOfElement(address));
    }

    @Test
    public void decrementReference() throws HeapException {
        int address = heap.allocateInHeap(2, ObjectType.INT, 1);
        heap.decrementReference(address);
        Assertions.assertThrows(HeapException.class, () -> {
            Assertions.assertEquals(2, heap.getSizeOfElement(address));
            Assertions.assertEquals(ObjectType.INT, heap.getTypeOfElement(address));
            Assertions.assertEquals(1, heap.accessValue(address, 0));
        });
    }

    @Test
    public void decrementReferenceTwice() throws HeapException {
        int address = heap.allocateInHeap(2, ObjectType.INT, 1);
        heap.decrementReference(address);
        Assertions.assertThrows(HeapException.class, () -> {
            heap.decrementReference(address);
        });
    }

    @Test
    public void incrementReference() throws HeapException {
        int address = heap.allocateInHeap(2, ObjectType.INT, 1);
        heap.incrementReference(address);
        Assertions.assertEquals(2, heap.getSizeOfElement(address));
        Assertions.assertEquals(ObjectType.INT, heap.getTypeOfElement(address));
        Assertions.assertEquals(1, heap.accessValue(address, 0));
    }

    @Test
    public void incrementReferenceTwice() throws HeapException {
        int address = heap.allocateInHeap(2, ObjectType.INT, 1);
        heap.incrementReference(address);
        Assertions.assertEquals(2, heap.getSizeOfElement(address));
        Assertions.assertEquals(ObjectType.INT, heap.getTypeOfElement(address));
        Assertions.assertEquals(1, heap.accessValue(address, 0));
        heap.incrementReference(address);
        Assertions.assertEquals(2, heap.getSizeOfElement(address));
        Assertions.assertEquals(ObjectType.INT, heap.getTypeOfElement(address));
        Assertions.assertEquals(1, heap.accessValue(address, 0));
    }

    @Test
    public void decrementBeforeIncrement() throws HeapException{
        int address = heap.allocateInHeap(2, ObjectType.INT, 1);
        heap.incrementReference(address);
        heap.decrementReference(address);
        Assertions.assertEquals(2, heap.getSizeOfElement(address));
        Assertions.assertEquals(ObjectType.INT, heap.getTypeOfElement(address));
        Assertions.assertEquals(1, heap.accessValue(address, 0));
        heap.incrementReference(address);
        Assertions.assertEquals(2, heap.getSizeOfElement(address));
        Assertions.assertEquals(ObjectType.INT, heap.getTypeOfElement(address));
        Assertions.assertEquals(1, heap.accessValue(address, 0));
    }

    @Test
    public void setValue() throws HeapException {
        int address = heap.allocateInHeap(2, ObjectType.INT, 1);
        heap.setValue(address, 0, 42);
        Assertions.assertEquals(42, heap.accessValue(address, 0));
    }

    @Test
    public void setValueKO1() throws HeapException{
        int address = heap.allocateInHeap(2, ObjectType.INT, 1);
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            heap.setValue(address, 2, 42);
        });
    }

    @Test
    public void setValueKO2() throws HeapException{
        int address = heap.allocateInHeap(2, ObjectType.INT, 1);
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            heap.setValue(address, -1, 42);
        });
    }

    @Test
    public void decrementStressed() throws HeapException {
        int size = heap.getHeapSize();
        for(int i = 1; i < size; ++i){
            int address = heap.allocateInHeap(i, ObjectType.INT, i);
            heap.decrementReference(address);
        }
        for(int i = 1; i < size; ++i){
            int address = heap.allocateInHeap(i, ObjectType.INT, i);
            Object element = heap.accessValue(address, 0);
            Assertions.assertEquals(i, element);
            Assertions.assertEquals(i, heap.getSizeOfElement(address));
            Assertions.assertEquals(ObjectType.INT, heap.getTypeOfElement(address));
        }
        Assertions.assertTrue(isPowerOf(heap.getHeapSize()));
        Assertions.assertTrue(heap.getHeapSize() > size);
    }

    @Test
    public void to_string() throws HeapException {
        int address = heap.allocateInHeap(1, ObjectType.INT, 1);
        int size = heap.getHeapSize();
        String expected = "{1=[Adress : 0\nSize : 1\nIsFree : false, Adress : 1\nSize : 1";
        for(int i = 2; i < size; i *= 2){
            expected += "\nIsFree : true], " + i + "=[Adress : "+i+"\nSize : " + i;
        }
        expected += "\nIsFree : true], "+size+"=[]}";
        Map<Integer, List<HeapBlock>> blocks = heap.getBlocks();
        Assertions.assertEquals(expected, blocks.toString());
    }

    @Test
    public void getArraySizeHeapEmpty(){
        Assertions.assertThrows(HeapException.class, () -> {
            heap.getSizeOfElement(0);
        });
    }

    @Test
    public void getArraySize() throws HeapException {
        int address = heap.allocateInHeap(1, ObjectType.INT, 42);
        Assertions.assertEquals(1, heap.getArraySize(address));
    }
}
