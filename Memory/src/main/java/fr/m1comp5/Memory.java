package fr.m1comp5;

public class Memory {
    private SymbolTable symbolTable;
    private Stack stack;
//    private Heap heap;

    public Memory(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
        stack = new Stack();
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public Stack getStack() {
        return stack;
    }

    public void declVar(String ident, Object value, ObjectType type) throws StackException {
        MemoryObject mo = new MemoryObject(ident, value, ObjectNature.VAR, type);
        symbolTable.put(mo);
        stack.push(mo);
    }

    public void identVal(String ident, ObjectType type, int s) {

    }

    public void declCst(String ident, Object value, ObjectType type) throws StackException {
        MemoryObject mo;
        if (value == null || value == ObjectType.OMEGA) mo = new MemoryObject(ident, null, ObjectNature.CST, type);
        else mo = new MemoryObject(ident, value, ObjectNature.CST, type);
        symbolTable.put(mo);
        stack.push(mo);
    }

    public void declMeth(String ident, Object value, ObjectType type) throws StackException {
        MemoryObject mo = new MemoryObject(ident, value, ObjectNature.METH, type);
        symbolTable.put(mo);
        stack.push(mo);
    }

    public MemoryObject assignValue(String ident, Object value) throws StackException, SymbolTableException {
        MemoryObject mo = stack.searchVariable(ident);
        if (mo == null) throw new SymbolTableException("Unknown symbol");
        if (value == null) throw new RuntimeException("Cannot assign null value");
        if (!value.getClass().equals(mo.getType())) throw new RuntimeException("Cannot assign value of type " + value.getClass() + " to " + mo.getType());

        switch (mo.getNature()) {
            case METH: return mo;
            case CST: throw new RuntimeException("Cannot assign cst value");
            case VAR: mo.setValue(value); break;
            case TAB: break;
        }

        symbolTable.update(ident, value);
        return mo;
    }
    
    public Object getVal(String ident) throws SymbolTableException {
        MemoryObject mo = symbolTable.get(ident);
        if (mo == null) throw new SymbolTableException("Var "+ ident +" was not found");
        if (mo.getValue() == null || mo.getValue() == ObjectType.OMEGA) throw new SymbolTableException("Var "+ ident +" was not initialized");
        return mo.getValue();
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
