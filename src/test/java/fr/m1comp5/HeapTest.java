package fr.m1comp5;

import junit.framework.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import fr.m1comp5.Memory.*;


public class HeapTest {

    Heap heap;


    @BeforeEach
    public void setUp() throws HeapException
    {
        heap = new Heap();
    }

    @Test
    public void addInHeap() throws HeapException
    {
        int address = heap.allocateInHeap(20, ObjectType.INT);
        for (int i = 0; i < 20; ++i)
        {
            heap.setValue(address, i, i);
        }
        for (int i = 0; i < 20; ++i)
        {
            Assertions.assertEquals(heap.accessValue(address, i), i);
            System.out.println(heap.accessValue(address, i));
        }
    }

    @Test
    public void allocateSingleVariable() throws HeapException
    {
        int address = heap.allocateInHeap(1, ObjectType.INT);
        heap.setValue(address, 0, 1);
        Assertions.assertEquals(heap.accessValue(address, 0), 1);
        System.out.println(heap.accessValue(address, 0));
        int address2 = heap.allocateInHeap(1, ObjectType.INT);
        heap.setValue(address2, 0, 1);
        Assertions.assertEquals(heap.accessValue(address2, 0), 1);
        System.out.println(heap.accessValue(address2, 0));
        int address3 = heap.allocateInHeap(500, ObjectType.INT);
        heap.setValue(address3, 400, 400);
        Assertions.assertEquals(heap.accessValue(address3, 400), 400);
        System.out.println(heap.accessValue(address3, 400));
    }

    @Test
    public void addHeap() throws HeapException
    {
        int address = heap.allocateInHeap(2, ObjectType.INT);
        heap.setValue(address, 0, 78);
        Assertions.assertEquals(heap.accessValue(address, 0), 78);
    }

    @Test
    public void growHeap() throws HeapException
    {
        int address = heap.allocateInHeap(2048, ObjectType.INT);

    }

}
