import org.junit.Before;
import org.junit.Test;

import fr.m1comp5.Memory.*;

import javax.sound.midi.MetaMessage;

import static org.junit.Assert.*;

public class StackTest {

    Stack stack;


    @Before
    public void setUp() {
        stack = new Stack();
    }

    @Test(expected = StackException.class)
    public void getTopEmptyStack() throws StackException {
        MemoryObject obj = stack.getTop();
        assertNull(obj);
    }

    @Test
    public void getTop() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.INT, ObjectType.VAR);
        stack.push(obj);
        assertEquals(obj, stack.getTop());
    }

    @Test(expected = StackException.class)
    public void getBaseEmptyStack() throws StackException {
        MemoryObject obj = stack.getBase();
        assertNull(obj);
    }

    @Test
    public void getBase() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.INT, ObjectType.VAR);
        stack.push(obj);
        assertEquals(obj, stack.getBase());
    }

    @Test
    public void getBaseSameGetTop() throws StackException{
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.INT, ObjectType.VAR);
        stack.push(obj);
        assertEquals(stack.getBase(), stack.getTop());
        assertEquals(obj, stack.getBase());
        assertEquals(obj, stack.getTop());
    }

    @Test
    public void getBaseDiffGetTop() throws StackException{
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.INT, ObjectType.VAR);
        MemoryObject obj2 = new MemoryObject("id2", 2, ObjectNature.INT, ObjectType.VAR);
        stack.push(obj);
        stack.push(obj2);
        assertNotEquals(stack.getBase(), stack.getTop());
        assertEquals(obj, stack.getBase());
        assertEquals(obj2, stack.getTop());
    }

    @Test(expected = StackException.class)
    public void pushNull() throws StackException{
        stack.push(null);
        assertNull(stack.getTop());
    }

    @Test
    public void pushMultiple() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.INT, ObjectType.VAR);
        MemoryObject obj2 = new MemoryObject("id2", 2, ObjectNature.INT, ObjectType.VAR);
        stack.push(obj);
        stack.push(obj2);
        assertEquals(obj2, stack.getTop());
        assertEquals(obj, stack.getBase());
    }

    @Test
    public void pushSameObjTwice() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.INT, ObjectType.VAR);
        stack.push(obj);
        stack.push(obj);
        assertEquals(obj, stack.pop());
        assertEquals(obj, stack.pop());
        assertTrue(stack.empty());
    }

    @Test(expected = StackException.class)
    public void popEmptyStack() throws StackException {
        MemoryObject obj = stack.pop();
        assertNull(obj);
    }

    @Test
    public void pop() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.INT, ObjectType.VAR);
        stack.push(obj);
        assertEquals(obj, stack.pop());
        assertTrue(stack.empty());
    }

    @Test(expected = StackException.class)
    public void searchVariableEmptyStack() throws StackException {
        MemoryObject obj = stack.searchVariable("id");
        assertNull(obj);
    }

    @Test
    public void searchVariable() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.INT, ObjectType.VAR);
        stack.push(obj);
        assertEquals(obj, stack.searchVariable("id"));
    }

    @Test
    public void searchVariableMultiple() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.INT, ObjectType.VAR);
        MemoryObject obj2 = new MemoryObject("id2", 2, ObjectNature.INT, ObjectType.VAR);
        MemoryObject obj3 = new MemoryObject("id3", 3, ObjectNature.INT, ObjectType.VAR);
        stack.push(obj);
        stack.push(obj2);
        stack.push(obj3);
        assertEquals(obj3, stack.searchVariable("id3"));
        assertEquals(obj2, stack.searchVariable("id2"));
        assertEquals(obj, stack.searchVariable("id"));
        assertNull(stack.searchVariable("id4"));
    }

    @Test
    public void searchVariableNotFound() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.INT, ObjectType.VAR);
        stack.push(obj);
        assertNull(stack.searchVariable("id2"));
    }

    @Test
    public void searchVariableAfterPop() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.INT, ObjectType.VAR);
        MemoryObject obj2 = new MemoryObject("id2", 2, ObjectNature.INT, ObjectType.VAR);
        stack.push(obj);
        stack.push(obj2);
        assertEquals(obj2, stack.searchVariable("id2"));
        assertEquals(obj, stack.searchVariable("id"));
        assertEquals(obj2, stack.pop());
        assertEquals(obj, stack.searchVariable("id"));
        assertNull(stack.searchVariable("id2"));
    }

    @Test
    public void searchVariableAfterMoveBaseToTop() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.INT, ObjectType.VAR);
        MemoryObject obj2 = new MemoryObject("id2", 2, ObjectNature.INT, ObjectType.VAR);
        stack.push(obj);
        stack.push(obj2);
        stack.moveBaseToTop();
        assertEquals(obj2, stack.searchVariable("id2"));
        assertNull(stack.searchVariable("id"));
    }

    @Test
    public void searchVariableAfterSetBaseFromValue() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.INT, ObjectType.VAR);
        MemoryObject obj2 = new MemoryObject("id2", 2, ObjectNature.INT, ObjectType.VAR);
        stack.push(obj);
        stack.push(obj2);
        stack.setBaseFromValue(1);
        assertEquals(obj2, stack.searchVariable("id2"));
        assertNull(stack.searchVariable("id"));
    }

    @Test
    public void moveBaseToTop() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.INT, ObjectType.VAR);
        MemoryObject obj2 = new MemoryObject("id2", 2, ObjectNature.INT, ObjectType.VAR);
        stack.push(obj);
        stack.push(obj2);
        stack.moveBaseToTop();
        assertEquals(obj2, stack.getBase());
        assertNotEquals(obj, stack.getTop());
        assertEquals(obj2, stack.getTop());
    }

    @Test(expected = StackException.class)
    public void setBaseFromValueEmptyStack() throws StackException {
        stack.setBaseFromValue(0);
    }

    @Test(expected = StackException.class)
    public void setBaseFromValueNegatif() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.INT, ObjectType.VAR);
        stack.push(obj);
        stack.setBaseFromValue(-1);
        assertEquals(obj, stack.getBase());
    }

    @Test(expected = StackException.class)
    public void setBaseFromValueTooHigh() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.INT, ObjectType.VAR);
        stack.push(obj);
        stack.setBaseFromValue(3);
        assertEquals(obj, stack.getBase());
    }

    @Test
    public void setBaseFromValue() throws StackException {
        MemoryObject obj = new MemoryObject("id", 1, ObjectNature.INT, ObjectType.VAR);
        MemoryObject obj2 = new MemoryObject("id2", 2, ObjectNature.INT, ObjectType.VAR);
        stack.push(obj);
        stack.push(obj2);
        stack.setBaseFromValue(1);
        assertEquals(obj2, stack.getBase());
    }

}
