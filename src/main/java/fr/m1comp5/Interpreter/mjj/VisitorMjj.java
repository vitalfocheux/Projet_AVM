package fr.m1comp5.Interpreter.mjj;

import fr.m1comp5.Analyzer.mjj.generated.*;
import fr.m1comp5.Memory.ObjectType;
import fr.m1comp5.Memory.MemoryObject;
import fr.m1comp5.Memory.ObjectNature;
import fr.m1comp5.Memory.SymbolTable;

public class VisitorMjj implements MiniJajaVisitor {
    private String toDisplay;
    private SymbolTable symbolTable;

    public VisitorMjj() {
        this.symbolTable = new SymbolTable();
        this.toDisplay = "";
    }

    public String toString() {
        return this.toDisplay;
    }

    @Override
    public Object visit(SimpleNode node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTroot node, Object data) {
        node.jjtGetChild(0).jjtAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTclasse node, Object data) {
        String ident = (String) ((ASTident) node.jjtGetChild(0)).jjtGetValue();
        try {
            //symbolTable.put(new MemoryObject(ident, ObjectType.EPSILON, ObjectNature.VAR, ObjectType.EPSILON));
            node.jjtGetChild(1).jjtAccept(this, data); //Visit vars or vnil
            node.jjtGetChild(2).jjtAccept(this, data); //Visit main
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Object visit(ASTident node, Object data) {
        try {
            if (symbolTable.get((String) node.jjtGetValue()).getType() == ObjectType.EPSILON) {
                throw new Exception();
            }
            System.out.println("ASTident -> "+(String)node.jjtGetValue() + " = " + symbolTable.get((String) node.jjtGetValue()).getValue());
            return symbolTable.get((String) node.jjtGetValue()).getValue();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object visit(ASTvnil node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTcst node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTdecls node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTmethode node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTvars node, Object data) {
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            node.jjtGetChild(i).jjtAccept(this, data);
        }
        return null;
    }

    @Override
    public Object visit(ASTvar node, Object data) {
        ObjectType varType = (ObjectType) node.jjtGetChild(0).jjtAccept(this, data); //Var type
        String varIdent = (String) ((ASTident) node.jjtGetChild(1)).jjtGetValue();
        System.out.println("ASTvar -> "+varIdent);

        if (varIdent == null) {
            throw new RuntimeException("Variable name cannot be null.");
        }
        Object value = null;
        if (node.jjtGetNumChildren() > 2) {
            value = node.jjtGetChild(2).jjtAccept(this, data); // Retrieve the initialized value
        }

        MemoryObject mo = new MemoryObject(varIdent,value, ObjectNature.VAR, varType);
        symbolTable.put(mo);

        return null;
    }

    @Override
    public Object visit(ASTtableau node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTomega node, Object data) {
        return "EMPTY";
    }

    @Override
    public Object visit(ASTmain node, Object data) {
        node.jjtGetChild(0).jjtAccept(this, data); // Visit vars
        node.jjtGetChild(1).jjtAccept(this, data); // Visit instrs
        return null;
    }

    @Override
    public Object visit(ASTenil node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTentetes node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTentete node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTinil node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTinstrs node, Object data) {
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            node.jjtGetChild(i).jjtAccept(this, data);
        }
        return null;
    }

    @Override
    public Object visit(ASTretour node, Object data) {
        Object returnValue = node.jjtGetChild(0).jjtAccept(this, data);
        toDisplay += returnValue;
        return returnValue;
    }

    @Override
    public Object visit(ASTecrire node, Object data) {
        Object value = node.jjtGetChild(0).jjtAccept(this, data);
        try {
            toDisplay += value;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Object visit(ASTecrireln node, Object data) {
        Object value = node.jjtGetChild(0).jjtAccept(this, data);
        try {
            toDisplay += value+"\n";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Object visit(ASTsi node, Object data) {
        Object value = node.jjtGetChild(0).jjtAccept(this, data);
        try {
            if ((boolean) value) {
                node.jjtGetChild(1).jjtAccept(this, data);
            } else {
                node.jjtGetChild(2).jjtAccept(this, data);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Object visit(ASTtantque node, Object data) {
        Object value = node.jjtGetChild(0).jjtAccept(this, data);
        try {
            if ((boolean) value) {
                node.jjtGetChild(1).jjtAccept(this, data);
                node.jjtAccept(this, data);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Object visit(ASTaffectation node, Object data) {
        try {
            Object value = node.jjtGetChild(1).jjtAccept(this, data);
            String varIdent = (String) node.jjtGetChild(0).jjtAccept(this, data);
            MemoryObject mo = symbolTable.get(varIdent);
            mo = new MemoryObject(varIdent,value,mo.getNature(),mo.getType());
            System.out.println("ASTaffectation -> " + value + " = " + varIdent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Object visit(ASTsomme node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTincrement node, Object data) {
        String varIdent = (String) ((ASTident) node.jjtGetChild(0)).jjtGetValue();
        MemoryObject mo = symbolTable.get(varIdent);
        int val = (int) mo.getValue() + 1;
        symbolTable.put(new MemoryObject(varIdent,val,mo.getNature(),mo.getType()));
        return val;
    }

    @Override
    public Object visit(ASTappelI node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTlistexp node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTexnil node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTnot node, Object data) {
        boolean exp = (boolean) node.jjtGetChild(0).jjtAccept(this, data);
        return !exp;
    }

    @Override
    public Object visit(ASTneg node, Object data) {
        int nb = (Integer) node.jjtGetChild(0).jjtAccept(this, data);
        return -nb;
    }

    @Override
    public Object visit(ASTet node, Object data) {
        boolean exp1 = (boolean) node.jjtGetChild(0).jjtAccept(this, data);
        boolean exp2 = (boolean) node.jjtGetChild(1).jjtAccept(this, data);
        System.out.println("ASTet -> " + exp1 + " && " + exp2);
        return exp1 && exp2;
    }

    @Override
    public Object visit(ASTou node, Object data) {
        boolean exp1 = (boolean) node.jjtGetChild(0).jjtAccept(this, data);
        boolean exp2 = (boolean) node.jjtGetChild(1).jjtAccept(this, data);
        System.out.println("ASTou -> " + exp1 + " || " + exp2);
        return exp1 || exp2;
    }

    @Override
    public Object visit(ASTeq node, Object data) {
        return node.jjtGetChild(0).jjtAccept(this, data) == node.jjtGetChild(1).jjtAccept(this, data);
    }

    @Override
    public Object visit(ASTsup node, Object data) {
        int nb1 = (int) node.jjtGetChild(0).jjtAccept(this, data);
        int nb2 = (int) node.jjtGetChild(1).jjtAccept(this, data);
        System.out.println("ASTsup -> " + nb1 + " > " + nb2);
        return nb1 > nb2;
    }

    @Override
    public Object visit(ASTadd node, Object data) {
        int nb1 = (int) node.jjtGetChild(0).jjtAccept(this, data);
        int nb2 = (int) node.jjtGetChild(1).jjtAccept(this, data);
        System.out.println("ASTadd -> " + nb1 + " + " + nb2);
        return nb1 + nb2;
    }

    @Override
    public Object visit(ASTsub node, Object data) {
        int nb1 = (int) node.jjtGetChild(0).jjtAccept(this, data);
        int nb2 = (int) node.jjtGetChild(1).jjtAccept(this, data);
        System.out.println("ASTsub -> " + nb1 + " - " + nb2);
        return nb1 - nb2;
    }

    @Override
    public Object visit(ASTmul node, Object data) {
        int nb1 = (int) node.jjtGetChild(0).jjtAccept(this, data);
        int nb2 = (int) node.jjtGetChild(1).jjtAccept(this, data);
        System.out.println("ASTmul -> " + nb1 + " * " + nb2);
        return nb1 * nb2;
    }

    @Override
    public Object visit(ASTdiv node, Object data) {
        int nb1 = (int) node.jjtGetChild(0).jjtAccept(this, data);
        int nb2 = (int) node.jjtGetChild(1).jjtAccept(this, data);
        System.out.println("ASTdiv -> " + nb1 + " / " + nb2);
        return nb1 / nb2;
    }

    @Override
    public Object visit(ASTlongeur node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTvrai node, Object data) {
        return true;
    }

    @Override
    public Object visit(ASTfaux node, Object data) {
        return false;
    }

    @Override
    public Object visit(ASTexp node, Object data) {
        return node.jjtGetChild(0).jjtAccept(this, data);
    }

    @Override
    public Object visit(ASTappelE node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTtab node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTrien node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTentier node, Object data) {
        return ObjectType.INT;
    }

    @Override
    public Object visit(ASTbooleen node, Object data) {
        return ObjectType.BOOLEAN;
    }

    @Override
    public Object visit(ASTnbre node, Object data) {
        return node.jjtGetValue();
    }

    @Override
    public Object visit(ASTchaine node, Object data) {
        return node.jjtGetValue();
    }
}
