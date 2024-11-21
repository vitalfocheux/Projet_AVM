package fr.m1comp5.Memory;
import java.util.ArrayList;
import java.util.List;

public class Stack {
    private final List<MemoryObject> stackList; // Stack representation
    private int base; // Base pointer
    private int top; // Top pointer

    public Stack() {
        stackList = new ArrayList<>();
        base = -1;
        top = -1;
    }

    public MemoryObject getTop() throws StackException {
        if (stackList.isEmpty()) {
            throw new StackException("Can't get the top pointer on an empty stack");
        }
        return stackList.get(top);
    }

    public MemoryObject getBase() throws StackException {
        if (stackList.isEmpty()) {
            throw new StackException("Can't get the base pointer on an empty stack");
        }
        return stackList.get(base);
    }

    public MemoryObject push(MemoryObject object) throws StackException {
        if (object == null) {
            throw new StackException("Can't push invalid object if the stack");
        }
        if (base == -1) {
            base = 0;
        }
        stackList.add(++top, object);
        return stackList.get(top);
    }

    public MemoryObject pop() throws StackException {
        if (stackList.isEmpty()) {
            throw new StackException("Can't pop object on an empty stack");
        }
        MemoryObject obj = stackList.remove(top--);
        if (stackList.isEmpty()) {
            base = -1;
            top = -1;
        }
        return obj;
    }

    public boolean empty(){
        return stackList.isEmpty();
    }

    public int size() {
        return stackList.size();
    } 
    public MemoryObject get(int index) throws StackException {
        if (index < 0 || index >= stackList.size()) {
            throw new StackException("Index out of bounds");
        }
        return stackList.get(index);
    }

    /**
     * Search variable from the base pointer useful in case of a function
     * @param id The id of the variable
     * @throws Exception
     * @return Null if the variable is not found and the variable otherwise
     */
    public MemoryObject searchVariable(String id) throws StackException {
        if (stackList.isEmpty()) {
            throw new StackException("Can't search the object because the stack is empty");
        }
        for (int i = base; i < top+1; ++i) {
            if (stackList.get(i).getId().equals(id)) {
                return stackList.get(i);
            }
        }
        return null;
    }

    public void moveBaseToTop() {
        base = top;
    }

    public void setBaseFromValue(int baseValue) throws StackException {
        if (stackList.isEmpty()) {
            throw new StackException("Can't change the base pointer of an empty stack");
        }
        if (baseValue < 0 || baseValue > top) {
            throw new StackException("The base of the stack must be a positive number and must be less than the top of the stack");
        }
        base = baseValue;
    }
}
