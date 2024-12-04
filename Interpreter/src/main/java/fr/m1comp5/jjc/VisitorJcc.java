package fr.m1comp5.jjc;

import fr.m1comp5.jjc.generated.*;
import fr.m1comp5.Memory;
import fr.m1comp5.MemoryObject;
import fr.m1comp5.ObjectNature;
import fr.m1comp5.ObjectType;

import java.util.List;


public class VisitorJcc implements JajaCodeVisitor {
    private String toDisplay;
    private Memory mem;
    private int addr;

    public VisitorJcc(Memory mem) {
        this.toDisplay = "";
        addr = 1;
        this.mem = mem;
    }

    public int getAddr() {
        return addr;
    }

    public void setAddr(int addr) {
        this.addr = addr;
    }

    public String getToDisplay() {
        return toDisplay;
    }

    @Override
    public Object visit(SimpleNode node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTRoot node, Object data) {
        return node.jjtGetChild(0).jjtAccept(this, data);
    }

    @Override
    public Object visit(ASTJajaCode node, Object data) {
        if (addr < 0) return null;

        int addrNode = (int) node.jjtGetChild(0).jjtAccept(this, data);

        if (addr == addrNode) return node.jjtGetChild(1).jjtAccept(this, data);
        return node.jjtGetChild(2).jjtAccept(this, data);
    }

    @Override
    public Object visit(ASTJcnil node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTInit node, Object data) {
        mem.getSymbolTable().newScope();
        addr++;
        return null;
    }

    @Override
    public Object visit(ASTSwap node, Object data) {
        try
        {
            mem.getStack().swap();
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        addr++;
        return null;
    }

    @Override
    public Object visit(ASTNew node, Object data) {
        String id = (String) node.jjtGetChild(0).jjtAccept(this, data);
        ObjectType type = (ObjectType) node.jjtGetChild(1).jjtAccept(this, data);
        ObjectNature nature = (ObjectNature) node.jjtGetChild(2).jjtAccept(this, data);
        int pos = (int) node.jjtGetChild(3).jjtAccept(this, data);

        switch (nature) {
            case VAR :
                try
                {
                    mem.identVal(id, type, pos);
                } catch (Exception e)
                {
                    System.err.println(e.getMessage());
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
                    System.err.println(e.getMessage());
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
                    System.err.println(e.getMessage());
                }
        }
        addr++;
        return null;
    }

    @Override
    public Object visit(ASTNewArray node, Object data)
    {
        String id = (String) node.jjtGetChild(0).jjtAccept(this, data);
        ObjectType arrayType = (ObjectType) node.jjtGetChild(1).jjtAccept(this, data);
        try
        {
            mem.declTab(id, mem.getStack().getTop().getValue(), arrayType);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public Object visit(ASTInvoke node, Object data) {
        String id = (String) node.jjtGetChild(0).jjtAccept(this, data);
        try
        {
            mem.getStack().push(new MemoryObject(null, addr + 1, ObjectNature.CST, ObjectType.INT));
            addr = (int) mem.getVal(id);
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Object visit(ASTLength node, Object data) {
        String id = (String) node.jjtGetChild(0).jjtAccept(this, data);
        try
        {
            return mem.getHeap().getArraySize((int) mem.getSymbolTable().get(id).getValue());
        }
        catch (Exception e)
        {
            throw new RuntimeException("The ident is not an ident for an array");
        }
    }

    @Override
    public Object visit(ASTReturn node, Object data) {
        try
        {
            MemoryObject mo = mem.getStack().pop();
            addr = (int) mo.getValue();
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Object visit(ASTWrite node, Object data) {
        StringBuilder sb = new StringBuilder();
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
            System.err.println(e.getMessage());
        }
        ++addr;
        toDisplay += sb.toString();
        return null;
    }

    @Override
    public Object visit(ASTWriteLn node, Object data) {
        StringBuilder sb = new StringBuilder();
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
            System.err.println(e.getMessage());
        }
        ++addr;
        toDisplay += sb.toString();
        return null;
    }

    @Override
    public Object visit(ASTPush node, Object data) {
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
            System.err.println(e.getMessage());
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTPop node, Object data) {
        try
        {
            MemoryObject mo = mem.getStack().pop();
            mem.getSymbolTable().removeObjectFromCurrentScope(mo);
            if (mo.getNature() == ObjectNature.TAB)
            {
                mem.getHeap().decrementReference((int) mo.getValue());
            }
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTLoad node, Object data) {
        String id = (String) node.jjtGetChild(0).jjtAccept(this, data);
        try
        {
            MemoryObject mo = mem.getSymbolTable().get(id);
            mem.getStack().push(new MemoryObject(null, mo.getValue(), ObjectNature.CST, mo.getType()));
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTALoad node, Object data) {
        String id = (String) node.jjtGetChild(0).jjtAccept(this, data);
        try
        {
            MemoryObject mo = mem.getSymbolTable().get(id);
            int index = (int) mem.getStack().pop().getValue();
            mem.getStack().push(new MemoryObject(null, mem.getHeap().accessValue((int) mo.getValue(), index), ObjectNature.CST, mo.getType()));
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTStore node, Object data) {
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
            System.err.println(e.getMessage());
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTAStore node, Object data) {
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
            System.err.println(e.getMessage());
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTIf node, Object data) {
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
            System.err.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Object visit(ASTGoTo node, Object data) {
        addr = (int) node.jjtGetChild(0).jjtAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTInc node, Object data) {
        String id = (String) node.jjtGetChild(0).jjtAccept(this, data);
        try
        {
            MemoryObject last = mem.getStack().pop();
            mem.assignValue(id, (int) last.getValue() + (int) mem.getSymbolTable().get(id).getValue());
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTAInc node, Object data) {
        String id = (String) node.jjtGetChild(0).jjtAccept(this, data);
        try
        {
            MemoryObject value = mem.getStack().pop();
            MemoryObject index = mem.getStack().pop();
            mem.assignValueArray(id, index.getValue(), (int) mem.getHeap().accessValue((int) mem.getSymbolTable().get(id).getValue(), (int) index.getValue()) + (int) value.getValue());
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTNop node, Object data) {
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTJcStop node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTJcIdent node, Object data)
    {
        return node.jjtGetValue();
    }

    @Override
    public Object visit(ASTNeg node, Object data) {
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
            System.err.println(e.getMessage());
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTNot node, Object data) {
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
            System.err.println(e.getMessage());
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTAdd node, Object data) {
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
            System.err.println(e.getMessage());
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTSub node, Object data) {
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
            System.err.println(e.getMessage());
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTMul node, Object data) {
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
            System.err.println(e.getMessage());
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTDiv node, Object data) {
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
            System.err.println(e.getMessage());
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTCmp node, Object data) {
        try
        {
            MemoryObject secondOperand = mem.getStack().pop();
            MemoryObject firstOperand = mem.getStack().pop();
            if (secondOperand.getType() == firstOperand.getType())
            {
                mem.getStack().push(new MemoryObject(null, firstOperand.equals(secondOperand), ObjectNature.CST, ObjectType.BOOLEAN));
            }
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTSup node, Object data) {
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
            System.err.println(e.getMessage());
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTOr node, Object data) {
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
            System.err.println(e.getMessage());
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTAnd node, Object data) {
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
            System.err.println(e.getMessage());
        }
        ++addr;
        return null;
    }

    @Override
    public Object visit(ASTType node, Object data)
    {
        ObjectType ftype = null;
        String type = (String) node.jjtGetValue();
        if (type.equals("booleen"))
        {
            ftype = ObjectType.BOOLEAN;
        }
        else if (type.equals("entier"))
        {
            ftype = ObjectType.INT;
        }
        else if (type.equals("void"))
        {
            ftype = ObjectType.VOID;
        }
        return ftype;
    }

    @Override
    public Object visit(ASTSorte node, Object data) {
        ObjectNature nature = null;
        String type = (String) node.jjtGetValue();
        if (type.equals("var"))
        {
            nature = ObjectNature.VAR;
        }
        else if (type.equals("cst"))
        {
            nature = ObjectNature.CST;
        }
        else if (type.equals("meth"))
        {
            nature = ObjectNature.METH;
        }
        return nature;
    }

    @Override
    public Object visit(ASTJcNbre node, Object data) {
        return node.jjtGetValue();
    }

    @Override
    public Object visit(ASTJcVrai node, Object data) {
        return node.jjtGetValue();
    }

    @Override
    public Object visit(ASTJcFalse node, Object data) {
        return node.jjtGetValue();
    }

    @Override
    public Object visit(ASTJcChaine node, Object data) {
        return node.jjtGetValue();
    }

}
