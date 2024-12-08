package fr.m1comp5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HeapElementTest {

    @Test
    public void testEqualsSame(){
        HeapElement he = new HeapElement(1, 1024, 1, ObjectType.INT);
        Assertions.assertEquals(he, he);
    }

    @Test
    public void testEqualsHeapElementInt(){
        HeapElement he = new HeapElement(1, 1024, 1, ObjectType.INT);
        Assertions.assertNotEquals(he, 1);
    }

    @Test
    public void testEqualsHeapElementNull(){
        HeapElement he = new HeapElement(1, 1024, 1, ObjectType.INT);
        Assertions.assertNotEquals(he, null);
    }

    @Test
    public void testEqualsSameAddress(){
        HeapElement he = new HeapElement(1, 1024, 1, ObjectType.INT);
        HeapElement he2 = new HeapElement(1, 512, 2, ObjectType.INT);
        Assertions.assertNotEquals(he, he2);
    }

    @Test
    public void testEqualsSameSize(){
        HeapElement he = new HeapElement(1, 1024, 1, ObjectType.INT);
        HeapElement he2 = new HeapElement(2, 1024, 2, ObjectType.INT);
        Assertions.assertNotEquals(he, he2);
    }

    @Test
    public void testEqualsSameRef(){
        HeapElement he = new HeapElement(1, 1024, 1, ObjectType.INT);
        HeapElement he2 = new HeapElement(2, 512, 1, ObjectType.INT);
        Assertions.assertNotEquals(he, he2);
    }

    @Test
    public void testEqualsSameAddressSameSize(){
        HeapElement he = new HeapElement(1, 1024, 1, ObjectType.INT);
        HeapElement he2 = new HeapElement(1, 1024, 2, ObjectType.INT);
        Assertions.assertNotEquals(he, he2);
    }

    @Test
    public void testEqualsSameAddressSameRef(){
        HeapElement he = new HeapElement(1, 1024, 1, ObjectType.INT);
        HeapElement he2 = new HeapElement(1, 512, 1, ObjectType.INT);
        Assertions.assertNotEquals(he, he2);
    }

    @Test
    public void testEqualsSameSizeSameRef(){
        HeapElement he = new HeapElement(1, 1024, 1, ObjectType.INT);
        HeapElement he2 = new HeapElement(2, 1024, 1, ObjectType.INT);
        Assertions.assertNotEquals(he, he2);
    }

    @Test
    public void testEqualsSameAddressSameSizeSameRef(){
        HeapElement he = new HeapElement(1, 1024, 1, ObjectType.INT);
        HeapElement he2 = new HeapElement(1, 1024, 1, ObjectType.INT);
        Assertions.assertEquals(he, he2);
    }

}
