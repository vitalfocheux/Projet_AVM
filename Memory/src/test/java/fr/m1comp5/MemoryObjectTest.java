package fr.m1comp5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class MemoryObjectTest {

    MemoryObject mo;

    @BeforeEach
    public void setUp() {
        mo = new MemoryObject("x", 1, ObjectNature.VAR, ObjectType.INT);
    }

    @Test
    public void getIdTest() {
        Assertions.assertEquals("x", mo.getId());
        mo.setId("y");
        Assertions.assertEquals("y", mo.getId());
    }

    @Test
    public void getNature(){
        Assertions.assertEquals(ObjectNature.VAR, mo.getNature());
        mo.setNature(ObjectNature.CST);
        Assertions.assertEquals(ObjectNature.CST, mo.getNature());
    }

    @Test
    public void getType(){
        Assertions.assertEquals(ObjectType.INT, mo.getType());
        mo.setType(ObjectType.BOOLEAN);
        Assertions.assertEquals(ObjectType.BOOLEAN, mo.getType());
    }

    @Test
    public void getParamsNoParams(){
        Assertions.assertNull(mo.getParamTypes());
    }

    @Test
    public void getParams(){
        List<ObjectType> paramTypes = new ArrayList<>();
        paramTypes.add(ObjectType.INT);
        paramTypes.add(ObjectType.BOOLEAN);
        paramTypes.add(ObjectType.OMEGA);
        paramTypes.add(ObjectType.VOID);
        MemoryObject mo2 = new MemoryObject("x", 1, ObjectNature.VAR, ObjectType.INT, paramTypes);
        Assertions.assertEquals(paramTypes, mo2.getParamTypes());
    }

    @Test
    public void testEqualsSame(){
        Assertions.assertEquals(mo, mo);
    }

    @Test
    public void testEqualsDifferentSameId(){
        MemoryObject mo2 = new MemoryObject("x", 1, ObjectNature.VAR, ObjectType.INT);
        Assertions.assertEquals(mo, mo2);
    }

    @Test
    public void testEqualsDifferent(){
        MemoryObject mo2 = new MemoryObject("y", 1, ObjectNature.VAR, ObjectType.INT);
        Assertions.assertNotEquals(mo, mo2);
    }

    @Test
    public void testEqualsMemoryObjectInt(){
        Assertions.assertNotEquals(mo, 1);
    }

    @Test
    public void testEqualsMemoryObjectNull(){
        Assertions.assertNotEquals(mo, null);
    }

}
