package fr.m1comp5.jjc;

import fr.m1comp5.*;
import fr.m1comp5.Debug.InterpreterException;
import fr.m1comp5.Debug.CallStack;
import fr.m1comp5.custom.exception.VisitorException;
import fr.m1comp5.jjc.generated.*;
import fr.m1comp5.Debug.InterpreterDebugger;
import fr.m1comp5.mjj.generated.ASTBooleen;
import fr.m1comp5.mjj.generated.ASTEntier;
import fr.m1comp5.mjj.generated.ASTRien;

public class VisitorJjc implements JajaCodeVisitor {
    private String toDisplay;
    private final Memory mem;
    private final CallStack callStack;
    private int addr;
    private InterpreterDebugger debugger;
    private boolean activesDebugger;


    public VisitorJjc(Memory mem) {
        this.toDisplay = "";
        addr = 1;
        this.mem = mem;
        callStack = new CallStack();
    }

    public int getAddr() {
        return addr;
    }

    public String getToDisplay() {
        return toDisplay;
    }

    public void setDebugger(InterpreterDebugger debug) {
        this.debugger = debug;
    }

    public void ActivesDebugger(boolean flag) {
        this.activesDebugger = flag;
    }
    public void checkDebugNode(Node node)  {
        if (activesDebugger && debugger != null) {
            try {
                debugger.onNodeVisitJJC(node);
            } catch (Exception e) {
                // If an exception is thrown, we deactivate the debugger
                activesDebugger = false;
            }
        }
    }

    @Override
    public Object visit(SimpleNode node, Object data) throws VisitorException
    {
        return null;
    }

    @Override
    public Object visit(ASTRoot node, Object data) throws VisitorException
    {
        return node.jjtGetChild(0).jjtAccept(this, data);
    }

    @Override
    public Object visit(ASTJajaCode node, Object data) throws VisitorException
    {
        if (addr < 0) return null;

        int addrNode = (int) node.jjtGetChild(0).jjtAccept(this, data);

        if (addr == addrNode) return node.jjtGetChild(1).jjtAccept(this, data);
        return node.jjtGetChild(2).jjtAccept(this, data);
    }

    @Override
    public Object visit(ASTJcnil node, Object data) throws VisitorException
    {
        return null;
    }

    @Override
    public Object visit(ASTInit node, Object data) throws VisitorException
    {
        mem.getSymbolTable().newScope();
        addr++;
        return null;
    }

    @Override
    public Object visit(ASTSwap node, Object data) throws VisitorException
    {
        checkDebugNode(node);
        try
        {
            try {
                mem.getStack().swap();
            } catch (StackException ignored) {

            }
        }
        catch (Exception e)
        {
            throw new InterpreterException(e.getMessage(), node.getLine(), node.getColumn(), callStack);
        }
        addr++;
        return null;
    }

    @Override
    public Object visit(ASTNew node, Object data) throws VisitorException
    {
        String id = (String) node.jjtGetChild(0).jjtAccept(this, data);
        ObjectType type = (ObjectType) node.jjtGetChild(1).jjtAccept(this, data);
        ObjectNature nature = (ObjectNature) node.jjtGetChild(2).jjtAccept(this, data);
        int pos = (int) node.jjtGetChild(3).jjtAccept(this, data);
        checkDebugNode(node);
        switch (nature) {
            case VAR :
                try
                {
                    mem.identVal(id, type, pos);
                } catch (Exception e)
                {
                    throw new InterpreterException(e.getMessage(), node.getLine(), node.getColumn(), callStack);
                }
                 break;
            case CST :
                try
                {
                    MemoryObject mo = mem.getStack().pop();
                    mem.declCst(id, mo.getValue(), type);
                }
                catch (Exception e)
                {
                    throw new InterpreterException(e.getMessage(), node.getLine(), node.getColumn(), callStack);
                }
                break;
            case METH:
                try
                {
                    MemoryObject mo = mem.getStack().pop();
                    mem.declMeth(id, mo.getValue(), type);
                }
                catch (Exception e)
                {
                    throw new InterpreterException(e.getMessage(), node.getLine(), node.getColumn(), callStack);
                }
        }
        addr++;
        return null;
    }

    @Override
    public Object visit(ASTNewArray node, Object data) throws VisitorException
    {
        String id = (String) node.jjtGetChild(0).jjtAccept(this, data);
        ObjectType arrayType = (ObjectType) node.jjtGetChild(1).jjtAccept(this, data);
        checkDebugNode(node);
        try
        {
            mem.declTab(id, mem.getStack().pop().getValue(), arrayType);
        }
        catch (Exception e)
        {
            throw new InterpreterException(e.getMessage(), node.getLine(), node.getColumn(), callStack);
        }
        addr++;
        return null;
    }

    @Override
    public Object visit(ASTInvoke node, Object data) throws VisitorException
    {
        String id = (String) node.jjtGetChild(0).jjtAccept(this, data);
        callStack.pushFunction(id, node.getLine(), node.getColumn());
        checkDebugNode(node);
        try
        {
            mem.getSymbolTable().newScope();
            mem.getStack().push(new MemoryObject(null, addr + 1, ObjectNature.CST, ObjectType.INT));
            addr = (int) mem.getVal(id);
        }
        catch (Exception e)
        {
            throw new InterpreterException(e.getMessage(), node.getLine(), node.getColumn(), callStack);
        }
        return null;
    }

    @Override
    public Object visit(ASTLength node, Object data) throws VisitorException
    {
        String id = (String) node.jjtGetChild(0).jjtAccept(this, data);
        checkDebugNode(node);
        try
        {
            return mem.getHeap().getArraySize((int) mem.getSymbolTable().get(id).getValue());
        }
        catch (Exception e)
        {
            throw new InterpreterException("The id is not for an array", node.getLine(), node.getColumn(), callStack);
        }
    }

    @Override
    public Object visit(ASTReturn node, Object data) throws VisitorException
    {
        callStack.tryPopFunction();
        checkDebugNode(node);
        try
        {
            mem.getSymbolTable().popScope();
            MemoryObject mo = mem.getStack().pop();
            addr = (int) mo.getValue();
        }
        catch (Exception e)
        {
            throw new InterpreterException(e.getMessage(), node.getLine(), node.getColumn(), callStack);
        }
        return null;
    }

    @Override
    public Object visit(ASTWrite node, Object data) throws VisitorException
    {
        StringBuilder sb = new StringBuilder();
        checkDebugNode(node);
        try
        {
            Object msg = mem.getStack().pop().getValue();
            if (msg instanceof String)
            {
                sb.append(((String) msg).replace("\"", ""));
            }
            else
            {
                sb.append(msg);
            }
        }
        catch (Exception e)
        {
            throw new InterpreterException(e.getMessage(), node.getLine(), node.getColumn(), callStack);
        }
        ++addr;
        toDisplay += sb.toString();
        return null;
    }

    @Override
    public Object visit(ASTWriteLn node, Object data) throws VisitorException
    {
        StringBuilder sb = new StringBuilder();
        checkDebugNode(node);
        try
        {
            Object msg = mem.getStack().pop().getValue();
            if (msg instanceof String)
            {
                sb.append(((String) msg).replace("\"", ""));
            }
            else
            {
                sb.append(msg);
            }
            sb.append('\n');
        }
        catch (Exception e)
        {
            throw new InterpreterException(e.getMessage(), node.getLine(), node.getColumn(), callStack);
        }
        ++addr;
        toDisplay += sb.toString();
        return null;
    }

    @Override
    public Object visit(ASTPush node, Object data) throws VisitorException
    {
        checkDebugNode(node);
        Object value = node.jjtGetChild(0).jjtAccept(this, data);
        ObjectType valType = null;
        if (value instanceof  Integer)
        {
            valType = ObjectType.INT;
        }
        else if (value instanceof Boolean)
        {
            valType = ObjectType.BOOLEAN;
        }
        else if (value instanceof String)
        {
            valType = ObjectType.OMEGA;
        }
        try
        {
            mem.getStack().push(new MemoryObject(null, value, ObjectNature.CST ,valType));
        }
        catch (Exception e)
        {
            throw new InterpreterException(e.getMessage(), node.getLine(), node.getColumn(), callStack);
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTPop node, Object data) throws VisitorException
    {
        checkDebugNode(node);
        try
        {
            try {
                MemoryObject mo = mem.getStack().pop();
                mem.getSymbolTable().removeObjectFromCurrentScope(mo);
                if (mo.getNature() == ObjectNature.TAB)
                {
                    mem.getHeap().decrementReference((int) mo.getValue());
                }
            } catch (StackException ignored) {

            }
        }
        catch (Exception e)
        {
            throw new InterpreterException(e.getMessage(), node.getLine(), node.getColumn(), callStack);
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTLoad node, Object data) throws VisitorException
    {
        checkDebugNode(node);
        String id = (String) node.jjtGetChild(0).jjtAccept(this, data);
        try
        {
            MemoryObject mo = mem.getSymbolTable().get(id);
            MemoryObject loadedObject = new MemoryObject(null, mo.getValue(), ObjectNature.CST, mo.getType());
            setOmegaValueFor(loadedObject);
            mem.getStack().push(loadedObject);
        }
        catch (Exception e)
        {
            throw new InterpreterException(e.getMessage(), node.getLine(), node.getColumn(), callStack);
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTALoad node, Object data) throws VisitorException
    {
        checkDebugNode(node);
        String id = (String) node.jjtGetChild(0).jjtAccept(this, data);
        try
        {
            MemoryObject mo = mem.getSymbolTable().get(id);
            int index = (int) mem.getStack().pop().getValue();
            mem.getStack().push(new MemoryObject(null, mem.getHeap().accessValue((int) mo.getValue(), index), ObjectNature.CST, mo.getType()));
        }
        catch (Exception e)
        {
            throw new InterpreterException(e.getMessage(), node.getLine(), node.getColumn(), callStack);
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTStore node, Object data) throws VisitorException
    {
        checkDebugNode(node);
        String id = (String) node.jjtGetChild(0).jjtAccept(this, data);
        try
        {
            MemoryObject last = mem.getStack().pop();
            MemoryObject mo = mem.getSymbolTable().get(id);
            if (last.getType() == mo.getType())
            {
                mem.assignValue(mo.getId(), last.getValue());
            }
        }
        catch (Exception e)
        {
            throw new InterpreterException(e.getMessage(), node.getLine(), node.getColumn(), callStack);
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTAStore node, Object data) throws VisitorException
    {
        checkDebugNode(node);
        String id = (String) node.jjtGetChild(0).jjtAccept(this, data);
        try
        {
            MemoryObject val = mem.getStack().pop();
            MemoryObject idx = mem.getStack().pop();
            MemoryObject mo = mem.getSymbolTable().get(id);
            if (val.getType() == mo.getType())
            {
                mem.assignValueArray(mo.getId(), idx.getValue(), val.getValue());
            }
        }
        catch (Exception e)
        {
            throw new InterpreterException(e.getMessage(), node.getLine(), node.getColumn(), callStack);
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTIf node, Object data) throws VisitorException
    {
        checkDebugNode(node);
        int address = (int) node.jjtGetChild(0).jjtAccept(this, data);
        try
        {
            MemoryObject last = mem.getStack().pop();
            if (last.getType() == ObjectType.BOOLEAN)
            {
                if ((boolean) last.getValue())
                {
                    addr = address;
                }
                else
                {
                    ++addr;
                }
            }
        }
        catch (Exception e)
        {
            throw new InterpreterException(e.getMessage(), node.getLine(), node.getColumn(), callStack);
        }
        return null;
    }

    @Override
    public Object visit(ASTGoTo node, Object data) throws VisitorException
    {
        checkDebugNode(node);
        addr = (int) node.jjtGetChild(0).jjtAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTInc node, Object data) throws VisitorException
    {
        checkDebugNode(node);
        String id = (String) node.jjtGetChild(0).jjtAccept(this, data);
        try
        {
            MemoryObject last = mem.getStack().pop();
            mem.assignValue(id, (int) last.getValue() + (int) mem.getSymbolTable().get(id).getValue());
        }
        catch (Exception e)
        {
            throw new InterpreterException(e.getMessage(), node.getLine(), node.getColumn(), callStack);
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTAInc node, Object data) throws VisitorException
    {
        checkDebugNode(node);
        String id = (String) node.jjtGetChild(0).jjtAccept(this, data);
        try
        {
            MemoryObject value = mem.getStack().pop();
            MemoryObject index = mem.getStack().pop();
            mem.assignValueArray(id, index.getValue(), (int) mem.getHeap().accessValue((int) mem.getSymbolTable().get(id).getValue(), (int) index.getValue()) + (int) value.getValue());
        }
        catch (Exception e)
        {
            throw new InterpreterException(e.getMessage(), node.getLine(), node.getColumn(), callStack);
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTNop node, Object data) throws VisitorException
    {
        checkDebugNode(node);
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTJcStop node, Object data) throws VisitorException
    {
        checkDebugNode(node);
        return null;
    }

    @Override
    public Object visit(ASTJcIdent node, Object data) throws VisitorException
    {
        return node.jjtGetValue();
    }

    @Override
    public Object visit(ASTNeg node, Object data) throws VisitorException
    {
        checkDebugNode(node);
        try
        {
            MemoryObject last = mem.getStack().pop();
            if (last.getType() == ObjectType.INT)
            {
                mem.getStack().push(new MemoryObject(null, -((int)last.getValue()), ObjectNature.CST, ObjectType.INT));
            }
        }
        catch (Exception e)
        {
            throw new InterpreterException(e.getMessage(), node.getLine(), node.getColumn(), callStack);
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTNot node, Object data) throws VisitorException
    {
        checkDebugNode(node);
        try
        {
            MemoryObject last = mem.getStack().pop();
            if (last.getType() == ObjectType.BOOLEAN)
            {
                mem.getStack().push(new MemoryObject(null, !((boolean) last.getValue()), ObjectNature.CST, ObjectType.BOOLEAN));
            }
        }
        catch (Exception e)
        {
            throw new InterpreterException(e.getMessage(), node.getLine(), node.getColumn(), callStack);
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTAdd node, Object data) throws VisitorException
    {
        checkDebugNode(node);
        try
        {
            MemoryObject secondOperand = mem.getStack().pop();
            MemoryObject firstOperand = mem.getStack().pop();
            if (secondOperand.getType() == firstOperand.getType() && firstOperand.getType() == ObjectType.INT)
            {
                mem.getStack().push(new MemoryObject(null, (int) firstOperand.getValue() + (int) secondOperand.getValue(), ObjectNature.CST, ObjectType.INT));
            }
        }
        catch (Exception e)
        {
            throw new InterpreterException(e.getMessage(), node.getLine(), node.getColumn(), callStack);
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTSub node, Object data) throws VisitorException
    {
        checkDebugNode(node);
        try
        {
            MemoryObject secondOperand = mem.getStack().pop();
            MemoryObject firstOperand = mem.getStack().pop();
            if (secondOperand.getType() == firstOperand.getType() && firstOperand.getType() == ObjectType.INT)
            {
                mem.getStack().push(new MemoryObject(null, (int) firstOperand.getValue() - (int) secondOperand.getValue(), ObjectNature.CST, ObjectType.INT));
            }
        }
        catch (Exception e)
        {
            throw new InterpreterException(e.getMessage(), node.getLine(), node.getColumn(), callStack);
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTMul node, Object data) throws VisitorException
    {
        checkDebugNode(node);
        try
        {
            MemoryObject secondOperand = mem.getStack().pop();
            MemoryObject firstOperand = mem.getStack().pop();
            if (secondOperand.getType() == firstOperand.getType() && firstOperand.getType() == ObjectType.INT)
            {
                mem.getStack().push(new MemoryObject(null, (int) firstOperand.getValue() * (int) secondOperand.getValue(), ObjectNature.CST, ObjectType.INT));
            }
        }
        catch (Exception e)
        {
            throw new InterpreterException(e.getMessage(), node.getLine(), node.getColumn(), callStack);
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTDiv node, Object data) throws VisitorException
    {
        checkDebugNode(node);
        try
        {
            MemoryObject secondOperand = mem.getStack().pop();
            MemoryObject firstOperand = mem.getStack().pop();
            if ((int) secondOperand.getValue() == 0)
            {
                throw new Exception("Can't divide by zero");
            }
            if (secondOperand.getType() == firstOperand.getType() && firstOperand.getType() == ObjectType.INT)
            {
                mem.getStack().push(new MemoryObject(null, (int) firstOperand.getValue() / (int) secondOperand.getValue(), ObjectNature.CST, ObjectType.INT));
            }
        }
        catch (Exception e)
        {
            throw new InterpreterException(e.getMessage(), node.getLine(), node.getColumn(), callStack);
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTCmp node, Object data) throws VisitorException
    {
        checkDebugNode(node);
        try
        {
            MemoryObject secondOperand = mem.getStack().pop();
            MemoryObject firstOperand = mem.getStack().pop();
            if (secondOperand.getType() == firstOperand.getType())
            {
                mem.getStack().push(new MemoryObject(null, firstOperand.getValue().equals(secondOperand.getValue()), ObjectNature.CST, ObjectType.BOOLEAN));
            }
        }
        catch (Exception e)
        {
            throw new InterpreterException(e.getMessage(), node.getLine(), node.getColumn(), callStack);
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTSup node, Object data) throws VisitorException
    {
        checkDebugNode(node);
        try
        {
            MemoryObject secondOperand = mem.getStack().pop();
            MemoryObject firstOperand = mem.getStack().pop();
            if (secondOperand.getType() == firstOperand.getType() && firstOperand.getType() == ObjectType.INT)
            {
                mem.getStack().push(new MemoryObject(null, (int) firstOperand.getValue() > (int) secondOperand.getValue(), ObjectNature.CST, ObjectType.BOOLEAN));
            }
        }
        catch (Exception e)
        {
            throw new InterpreterException(e.getMessage(), node.getLine(), node.getColumn(), callStack);
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTOr node, Object data) throws VisitorException
    {
        checkDebugNode(node);
        try
        {
            MemoryObject secondOperand = mem.getStack().pop();
            MemoryObject firstOperand = mem.getStack().pop();
            if (secondOperand.getType() == firstOperand.getType() && firstOperand.getType() == ObjectType.BOOLEAN)
            {
                mem.getStack().push(new MemoryObject(null, (boolean) firstOperand.getValue() || (boolean) secondOperand.getValue(), ObjectNature.CST, ObjectType.BOOLEAN));
            }
        }
        catch (Exception e)
        {
            throw new InterpreterException(e.getMessage(), node.getLine(), node.getColumn(), callStack);
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTAnd node, Object data) throws VisitorException
    {
        checkDebugNode(node);
        try
        {
            MemoryObject secondOperand = mem.getStack().pop();
            MemoryObject firstOperand = mem.getStack().pop();
            if (secondOperand.getType() == firstOperand.getType() && firstOperand.getType() == ObjectType.BOOLEAN)
            {
                mem.getStack().push(new MemoryObject(null, (boolean) firstOperand.getValue() && (boolean) secondOperand.getValue(), ObjectNature.CST, ObjectType.BOOLEAN));
            }
        }
        catch (Exception e)
        {
            throw new InterpreterException(e.getMessage(), node.getLine(), node.getColumn(), callStack);
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTType node, Object data) throws VisitorException
    {
        ObjectType ftype = null;
        Object type = node.jjtGetValue();
        if (type instanceof String s)
        {
            ftype = switch (s) {
                case "booleen" -> ObjectType.BOOLEAN;
                case "entier" -> ObjectType.INT;
                default -> ftype;
            };
        }
        else
        {
            ftype = getTypeForMethod(type);
        }
        return ftype;
    }

    public ObjectType getTypeForMethod(Object type)
    {
        ObjectType ftype = null;
        if (type instanceof ASTRien)
        {
            ftype = ObjectType.VOID;
        }
        else if (type instanceof ASTBooleen)
        {
            ftype = ObjectType.BOOLEAN;
        }
        else if (type instanceof ASTEntier)
        {
            ftype = ObjectType.INT;
        }
        return ftype;
    }

    @Override
    public Object visit(ASTSorte node, Object data) throws VisitorException
    {
        ObjectNature nature = null;
        String type = (String) node.jjtGetValue();
        nature = switch (type) {
            case "var" -> ObjectNature.VAR;
            case "cst" -> ObjectNature.CST;
            case "meth" -> ObjectNature.METH;
            default -> nature;
        };
        return nature;
    }

    @Override
    public Object visit(ASTJcNbre node, Object data) throws VisitorException
    {
        return node.jjtGetValue();
    }

    @Override
    public Object visit(ASTJcVrai node, Object data) throws VisitorException
    {
        return true;
    }

    @Override
    public Object visit(ASTJcFalse node, Object data) throws VisitorException
    {
        return false;
    }

    @Override
    public Object visit(ASTJcChaine node, Object data) throws VisitorException
    {
        return node.jjtGetValue();
    }

    public String toString()
    {
        return toDisplay;
    }

    private void setOmegaValue(MemoryObject mo)
    {
        if (mo.getValue() instanceof ObjectType ot && ot == ObjectType.OMEGA)
        {
            if (mo.getType() == ObjectType.INT)
            {
                mo.setValue(0);
            }
            else if (mo.getType() == ObjectType.BOOLEAN)
            {
                mo.setValue(false);
            }
        }
    }

    private void setOmegaValueFor(MemoryObject... memoryObjArray)
    {
        for (MemoryObject mo : memoryObjArray)
        {
            setOmegaValue(mo);
        }
    }

}
