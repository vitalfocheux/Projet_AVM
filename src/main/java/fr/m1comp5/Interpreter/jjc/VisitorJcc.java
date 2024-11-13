package fr.m1comp5.Interpreter.jjc;

import fr.m1comp5.Analyzer.jjc.generated.*;
import fr.m1comp5.Memory.*;

import java.util.ArrayList;
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
        return null;
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
        addr++;
        return null;
    }

    @Override
    public Object visit(ASTSwap node, Object data) {
        try {
            MemoryObject fst = mem.getStack().pop();
            MemoryObject scd = mem.getStack().pop();
            mem.getStack().push(fst);
            mem.getStack().push(scd);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        addr++;
        return null;
    }

    @Override
    public Object visit(ASTNew node, Object data) {
        String varIdent = (String) node.jjtGetChild(0).jjtAccept(this, data);
        ObjectType type = (ObjectType) node.jjtGetChild(1).jjtAccept(this, data);
        ObjectNature nature = (ObjectNature) node.jjtGetChild(2).jjtAccept(this, data);
        int pos = (int) node.jjtGetChild(3).jjtAccept(this, data);

        switch (nature) {
            case VAR :
                try
                {
                    mem.identVal(varIdent, type, pos);
                } catch (Exception ignored)
                {
                }
                 break;
            case CST :
//                MemoryObject mo = mem.getStack().pop();
//                mem.declCst(varIdent, mo.getValue(), type);
                break;
            case TAB: break;
            case METH: break;
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
            mem.declTab(id, (Integer) mem.getStack().getTop().getValue(), arrayType);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public Object visit(ASTInvoke node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTLength node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTReturn node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTWrite node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTWriteLn node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTPush node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTPop node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTLoad node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTALoad node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTStore node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTAStore node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTIf node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTGoTo node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTInc node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTAInc node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTNop node, Object data) {
        addr++;
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
        return null;
    }

    @Override
    public Object visit(ASTNot node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTAdd node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTSub node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTMul node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTDiv node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTCmp node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTSup node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTOr node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTAnd node, Object data) {
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
        return null;
    }

    @Override
    public Object visit(ASTJcNbre node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTJcVrai node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTJcFalse node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTJcChaine node, Object data) {
        return null;
    }


    ObjectType getTypeFromString(String type)
    {
        ObjectType ftype = null;
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
}
