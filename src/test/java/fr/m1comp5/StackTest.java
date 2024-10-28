package fr.m1comp5;

import fr.m1comp5.Memory.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StackTest {

    Stack stack;


    @BeforeEach
    public void setUp() {
        stack = new Stack();
    }

    @Test
    public void getTopEmptyStack() throws StackException {
        Assertions.assertThrows(StackException.class, () -> {
            stack.getTop();
        });
    }

    @Test
    public void getTop() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        stack.push(obj);
        Assertions.assertEquals(obj, stack.getTop());
    }

    @Test
    public void getBaseEmptyStack() throws StackException {
        Assertions.assertThrows(StackException.class, () -> {
            stack.getBase();
        });
    }

    @Test
    public void getBase() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        stack.push(obj);
        Assertions.assertEquals(obj, stack.getBase());
    }

    @Test
    public void getBaseSameGetTop() throws StackException{
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        stack.push(obj);
        Assertions.assertEquals(stack.getBase(), stack.getTop());
        Assertions.assertEquals(obj, stack.getBase());
        Assertions.assertEquals(obj, stack.getTop());
    }

    @Test
    public void getBaseDiffGetTop() throws StackException{
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        MemoryObject obj2 = new MemoryObject("id2", 2, ObjectNature.VAR, ObjectType.INT);
        stack.push(obj);
        stack.push(obj2);
        Assertions.assertNotEquals(stack.getBase(), stack.getTop());
        Assertions.assertEquals(obj, stack.getBase());
        Assertions.assertEquals(obj2, stack.getTop());
    }

    @Test
    public void pushNull() throws StackException{
        Assertions.assertThrows(StackException.class, () -> {
            stack.push(null);
        });
        Assertions.assertThrows(StackException.class, () -> {
            stack.getTop();
        });
    }

    @Test
    public void pushMultiple() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        MemoryObject obj2 = new MemoryObject("id2", 2, ObjectNature.VAR, ObjectType.INT);
        stack.push(obj);
        stack.push(obj2);
        Assertions.assertEquals(obj2, stack.getTop());
        Assertions.assertEquals(obj, stack.getBase());
    }

    @Test
    public void pushSameObjTwice() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        stack.push(obj);
        stack.push(obj);
        Assertions.assertEquals(obj, stack.pop());
        Assertions.assertEquals(obj, stack.pop());
        Assertions.assertTrue(stack.empty());
    }

    @Test
    public void popEmptyStack() throws StackException {
        Assertions.assertThrows(StackException.class, () -> {
            stack.pop();
        });
    }

    @Test
    public void pop() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        stack.push(obj);
        Assertions.assertEquals(obj, stack.pop());
        Assertions.assertTrue(stack.empty());
    }

    @Test
    public void searchVariableEmptyStack() throws StackException {
        Assertions.assertThrows(StackException.class, () -> {
            stack.searchVariable("id");
        });
    }

    @Test
    public void searchVariable() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        stack.push(obj);
        Assertions.assertEquals(obj, stack.searchVariable("id"));
    }

    @Test
    public void searchVariableMultiple() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        MemoryObject obj2 = new MemoryObject("id2", 2, ObjectNature.VAR, ObjectType.INT);
        MemoryObject obj3 = new MemoryObject("id3", 3, ObjectNature.VAR, ObjectType.INT);
        stack.push(obj);
        stack.push(obj2);
        stack.push(obj3);
        Assertions.assertEquals(obj3, stack.searchVariable("id3"));
        Assertions.assertEquals(obj2, stack.searchVariable("id2"));
        Assertions.assertEquals(obj, stack.searchVariable("id"));
        Assertions.assertNull(stack.searchVariable("id4"));
    }

    @Test
    public void searchVariableNotFound() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        stack.push(obj);
        Assertions.assertNull(stack.searchVariable("id2"));
    }

    @Test
    public void searchVariableAfterPop() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        MemoryObject obj2 = new MemoryObject("id2", 2, ObjectNature.VAR, ObjectType.INT);
        stack.push(obj);
        stack.push(obj2);
        Assertions.assertEquals(obj2, stack.searchVariable("id2"));
        Assertions.assertEquals(obj, stack.searchVariable("id"));
        Assertions.assertEquals(obj2, stack.pop());
        Assertions.assertEquals(obj, stack.searchVariable("id"));
        Assertions.assertNull(stack.searchVariable("id2"));
    }

    @Test
    public void searchVariableAfterMoveBaseToTop() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        MemoryObject obj2 = new MemoryObject("id2", 2, ObjectNature.VAR, ObjectType.INT);
        stack.push(obj);
        stack.push(obj2);
        stack.moveBaseToTop();
        Assertions.assertEquals(obj2, stack.searchVariable("id2"));
        Assertions.assertNull(stack.searchVariable("id"));
    }

    @Test
    public void searchVariableAfterSetBaseFromValue() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        MemoryObject obj2 = new MemoryObject("id2", 2, ObjectNature.VAR, ObjectType.INT);
        stack.push(obj);
        stack.push(obj2);
        stack.setBaseFromValue(1);
        Assertions.assertEquals(obj2, stack.searchVariable("id2"));
        Assertions.assertNull(stack.searchVariable("id"));
    }

    @Test
    public void moveBaseToTop() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        MemoryObject obj2 = new MemoryObject("id2", 2, ObjectNature.VAR, ObjectType.INT);
        stack.push(obj);
        stack.push(obj2);
        stack.moveBaseToTop();
        Assertions.assertEquals(obj2, stack.getBase());
        Assertions.assertNotEquals(obj, stack.getTop());
        Assertions.assertEquals(obj2, stack.getTop());
    }

    @Test
    public void setBaseFromValueEmptyStack() throws StackException {
        Assertions.assertThrows(StackException.class, () -> {
            stack.setBaseFromValue(0);
        });
    }

    @Test
    public void setBaseFromValueNegatif() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        stack.push(obj);
        Assertions.assertThrows(StackException.class, () -> {
            stack.setBaseFromValue(-1);
        });
        Assertions.assertEquals(obj, stack.getBase());
    }

    @Test
    public void setBaseFromValueTooHigh() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        stack.push(obj);
        Assertions.assertThrows(StackException.class, () -> {
            stack.setBaseFromValue(3);
        });
        Assertions.assertEquals(obj, stack.getBase());
    }

    @Test
    public void setBaseFromValue() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        MemoryObject obj2 = new MemoryObject("id2", 2, ObjectNature.VAR, ObjectType.INT);
        stack.push(obj);
        stack.push(obj2);
        stack.setBaseFromValue(1);
        Assertions.assertEquals(obj2, stack.getBase());
    }

}
