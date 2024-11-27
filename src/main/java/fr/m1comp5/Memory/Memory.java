package fr.m1comp5.Memory;

import fr.m1comp5.Analyzer.mjj.generated.*;
import fr.m1comp5.Interpreter.mjj.MjjInterpreterMode;
import fr.m1comp5.Interpreter.mjj.VisitorMjj;

public class Memory {
    private SymbolTable symbolTable;
    private Stack stack;
    private Heap heap;

    public Memory(SymbolTable symbolTable) throws HeapException {
        this.symbolTable = symbolTable;
        stack = new Stack();
        heap = new Heap();

    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public Stack getStack() {
        return stack;
    }

    public Heap getHeap()
    {
        return heap;
    }

    public void declVar(String ident, Object value, ObjectType type) throws StackException, SymbolTableException {
        MemoryObject mo = new MemoryObject(ident, value, ObjectNature.VAR, type);
        symbolTable.put(mo);
        stack.push(mo);
    }

    public void identVal(String ident, ObjectType type, int s) throws StackException, SymbolTableException, Exception {
        MemoryObject mo = stack.getObjectFromTheTop(s);
        if (mo.getType() != ObjectType.OMEGA)
        {
            throw new Exception("IdentVal must happen on an object with ");
        }
        symbolTable.remove(mo);
        mo.setId(ident);
        mo.setType(type);
        mo.setNature(ObjectNature.VAR);
        symbolTable.put(mo);
    }

    public void declCst(String ident, Object value, ObjectType type) throws StackException, SymbolTableException {
        MemoryObject mo;
        if (value == null || value == ObjectType.OMEGA) mo = new MemoryObject(ident, null, ObjectNature.VCST, type);
        else mo = new MemoryObject(ident, value, ObjectNature.CST, type);
        symbolTable.put(mo);
        stack.push(mo);
    }

    public void declMeth(String ident, Object value, ObjectType type) throws StackException, SymbolTableException {
        MemoryObject mo = new MemoryObject(ident, value, ObjectNature.METH, type);
        symbolTable.put(mo);
        stack.push(mo);
    }

    public void declTab(String id, Object value, ObjectType type) throws HeapException, StackException, SymbolTableException
    {
        int address = heap.allocateInHeap((Integer) value, type);
        MemoryObject mo = new MemoryObject(id, address, ObjectNature.TAB, type);
        symbolTable.put(mo);
        stack.push(mo);
    }

    public void removeDecl(String id) throws StackException, HeapException
    {
        MemoryObject mo = symbolTable.get(id);
        if (mo.getNature() == ObjectNature.TAB)
        {
            heap.decrementReference((int) mo.getValue());
        }
        stack.eraseVariable(id);
        symbolTable.remove(mo);
    }

    public MemoryObject assignValue(String ident, Object value) throws StackException, SymbolTableException, HeapException {
        MemoryObject mo = stack.searchVariableFromTop(ident);
        if (mo == null) throw new SymbolTableException("Unknown symbol");
        if (value == null) throw new RuntimeException("Cannot assign null value");
        if (mo.getType() != ObjectType.OMEGA && !(value.getClass().equals(mo.getValue().getClass())))
        {
            throw new RuntimeException("Cannot assign value of type " + value.getClass() + " to " + mo.getType());
        }

        switch (mo.getNature()) {
            case METH:
                return mo;
            case CST:
                throw new RuntimeException("Cannot assign value to a constant");
            case VCST:
                mo.setValue(value);
                mo.setNature(ObjectNature.CST);
                break;
            case VAR:
                mo.setValue(value);
                break;
            case TAB:
                if (!(value instanceof Integer))
                {
                    throw new RuntimeException("Can't access the heap with this type of object");
                }
                heap.incrementReference((Integer) value);
                heap.decrementReference((Integer) mo.getValue());
                mo.setValue(value);
                break;

        }
        symbolTable.update(ident, value);
        return mo;
    }

    public void assignValueArray(String id, Object index, Object value) throws StackException, HeapException
    {
        MemoryObject mo = stack.searchVariableFromTop(id);
        if (mo.getNature() != ObjectNature.TAB)
        {
            throw new RuntimeException("Can't perform array operation on an object which is not an array");
        }
        if (!(index instanceof Integer))
        {
            throw new RuntimeException("The index must be an integer");
        }
        heap.setValue((Integer) mo.getValue(), (Integer) index, value);
    }

    public void assignType(String id, ObjectType type) throws StackException
    {
        MemoryObject mo = stack.searchVariableFromTop(id);
        if (mo == null)
        {
            return;
        }
        mo.setType(type);
    }

    public ASTEntetes getParams(String id) throws StackException
    {
        MemoryObject mo = stack.searchVariableFromTop(id);
        if (mo.getNature() != ObjectNature.METH || !(mo.getValue() instanceof ASTMethode method))
        {
            return null;
        }
        return (ASTEntetes) method.jjtGetChild(2);
    }

    public ASTVars getDecls(String id) throws StackException
    {
        MemoryObject mo = stack.searchVariableFromTop(id);
        if (mo.getNature() != ObjectNature.METH || !(mo.getValue() instanceof ASTMethode method))
        {
            return null;
        }
        return (ASTVars) method.jjtGetChild(3);
    }

    public ASTInstrs getBody(String id) throws StackException
    {
        MemoryObject mo = stack.searchVariableFromTop(id);
        if (mo.getNature() != ObjectNature.METH || !(mo.getValue() instanceof ASTMethode method))
        {
            return null;
        }
        return (ASTInstrs) method.jjtGetChild(4);
    }


    public void expParam(ASTListExp lexp, ASTEntetes ent, VisitorMjj visitor) throws SymbolTableException, StackException
    {
        if (lexp == null && ent == null)
        {
            return;
        }
        Object currEntetes = ent;
        Object currListExp = lexp;
        while(!(currEntetes instanceof ASTEnil) && !(currListExp instanceof ASTExnil))
        {
            ASTEntete entete = (ASTEntete) ((ASTEntetes) currEntetes).jjtGetChild(0);
            String id = (String) ((ASTIdent) entete.jjtGetChild(1)).jjtGetValue();
            ObjectType type = (ObjectType) entete.jjtGetChild(0).jjtAccept(visitor, MjjInterpreterMode.DEFAULT);
            Object value = ((ASTListExp) currListExp).jjtGetChild(0).jjtAccept(visitor, MjjInterpreterMode.DEFAULT);
            declVar(id, value, type);
            currEntetes = ((ASTEntetes) currEntetes).jjtGetChild(1);
            currListExp = ((ASTListExp) currListExp).jjtGetChild(1);
        }
    }
    
    public Object getVal(String ident) throws SymbolTableException {
        MemoryObject mo = symbolTable.get(ident);
        if (mo == null) throw new SymbolTableException("Var "+ ident +" was not found");
        if (mo.getValue() == null || mo.getValue() == ObjectType.OMEGA) throw new SymbolTableException("Var "+ ident +" was not initialized");
        return mo.getValue();
    }

    public String classVariable() throws StackException
    {
        return stack.getBase().getId();
    }

    public ObjectNature getNature(String ident) throws SymbolTableException {
        MemoryObject mo = symbolTable.get(ident);
        if (mo == null || mo.getType() == ObjectType.OMEGA) throw new SymbolTableException("Var "+ ident +" was not found");
        return mo.getNature();
    }

    public ObjectType getType(String ident) throws SymbolTableException {
        MemoryObject mo = symbolTable.get(ident);
        if (mo == null) throw new SymbolTableException("Var "+ ident +" was not found");
        return mo.getType();
    }

}
