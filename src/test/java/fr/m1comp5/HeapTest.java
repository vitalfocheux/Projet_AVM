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


}
