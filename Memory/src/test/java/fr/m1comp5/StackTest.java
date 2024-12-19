package fr.m1comp5;

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
        Assertions.assertEquals(2, stack.size());
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

    @Test
    public void sizeEmpty(){
        Assertions.assertEquals(0, stack.size());
    }

    @Test
    public void searchVariableEmpty(){
        Assertions.assertThrows(StackException.class, () -> {
            stack.searchVariable("id");
        });
    }

    @Test
    public void searchVariableNotInStack() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        stack.push(obj);
        Assertions.assertNull(stack.searchVariable("id2"));
    }

    @Test
    public void searchVariableInStack() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        stack.push(obj);
        Assertions.assertEquals(obj, stack.searchVariable("id"));
    }

    @Test
    public void searchVariableFromTopEmpty(){
        Assertions.assertThrows(StackException.class, () -> {
            stack.searchVariableFromTop("id");
        });
    }

    @Test
    public void searchVariableFromTopNotInStack() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        stack.push(obj);
        Assertions.assertNull(stack.searchVariableFromTop("id2"));
    }

    @Test
    public void searchVariableFromTopInStack() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        stack.push(obj);
        Assertions.assertEquals(obj, stack.searchVariableFromTop("id"));
    }

    @Test
    public void getObjectFromTopEmpty() throws StackException {
        Assertions.assertThrows(StackException.class, () -> {
            stack.getObjectFromTheTop(1);
        });
    }

    @Test
    public void getObjectFromTopNeg() throws StackException {
        stack.push(new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT));
         Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
             MemoryObject mo = stack.getObjectFromTheTop(-1);
         });
    }

    @Test
    public void getObjectFromTop() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        stack.push(obj);
        MemoryObject obj2 = stack.getObjectFromTheTop(0);
        Assertions.assertEquals(obj, obj2);
    }

    @Test
    public void getEmpty(){
        Assertions.assertThrows(StackException.class, () -> {
           stack.get(0);
        });
    }

    @Test
    public void getNeg() throws StackException {
        stack.push(new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT));
        Assertions.assertThrows(StackException.class, () -> {
            MemoryObject mo = stack.get(-1);
        });
    }

    @Test
    public void getSupSize() throws StackException {
        stack.push(new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT));
        Assertions.assertThrows(StackException.class, () -> {
            MemoryObject mo = stack.get(1);
        });
    }

    @Test
    public void get() throws StackException {
        stack.push(new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT));
        MemoryObject mo = stack.get(0);
        Assertions.assertEquals("id", mo.getId());
        Assertions.assertEquals(1, mo.getValue());
        Assertions.assertEquals(ObjectNature.VAR, mo.getNature());
        Assertions.assertEquals(ObjectType.INT, mo.getType());
    }

    @Test
    public void swapEmpty(){
        Assertions.assertThrows(StackException.class, () -> {
           stack.swap();
        });
    }

    @Test
    public void swapButStackSizeIsOne() throws StackException {
        stack.push(new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT));
        Assertions.assertThrows(StackException.class, () -> {
            stack.swap();
        });
    }

    @Test
    public void swap() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        MemoryObject obj2 = new MemoryObject("id2", 2, ObjectNature.VAR, ObjectType.INT);
        stack.push(obj);
        stack.push(obj2);
        stack.swap();
        Assertions.assertEquals(obj, stack.getTop());
        Assertions.assertEquals(obj2, stack.getBase());
    }

    @Test
    public void eraseStackEmpty(){
        Assertions.assertThrows(StackException.class, () -> {
           stack.eraseVariable("id");
        });
    }

    @Test
    public void eraseNotInStack() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        stack.push(obj);
        stack.eraseVariable("id2");
        Assertions.assertEquals(obj, stack.getTop());
        Assertions.assertEquals(stack.size(), 1);
    }

    @Test
    public void eraseInStack() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.VAR, ObjectType.INT);
        stack.push(obj);
        stack.eraseVariable("id");
        Assertions.assertEquals(stack.size(), 0);
    }

}
