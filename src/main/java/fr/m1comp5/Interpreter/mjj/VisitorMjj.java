package fr.m1comp5.Interpreter.mjj;

import fr.m1comp5.Analyzer.mjj.generated.*;
import fr.m1comp5.Memory.*;

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
    public Object visit(ASTRoot node, Object data) {
        node.jjtGetChild(0).jjtAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTClasse node, Object data) {
        String ident = (String) ((ASTIdent) node.jjtGetChild(0)).jjtGetValue();
        try {
            //symbolTable.put(new MemoryObject(ident, ObjectType.EPSILON, ObjectNature.VAR, ObjectType.EPSILON));
            node.jjtGetChild(1).jjtAccept(this, data); //Visit vars or vnil
            node.jjtGetChild(2).jjtAccept(this, data); //Visit main
        } catch (Exception e) {
            System.out.println("Exception is : "+ e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Object visit(ASTIdent node, Object data) {
        try {
            if (symbolTable.get((String) node.jjtGetValue()).getType() == ObjectType.OMEGA) {
                throw new Exception();
            }
            System.out.println("ASTident -> "+(String)node.jjtGetValue() + " = " + symbolTable.get((String) node.jjtGetValue()).getValue());
            return symbolTable.get((String) node.jjtGetValue()).getValue();
        } catch (Exception e) {
            System.out.println("Exception is : "+ e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    public Object visit(ASTDecls node, Object data) {
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            node.jjtGetChild(i).jjtAccept(this, data);
        }
        return null;
    }

    @Override
    public Object visit(ASTMethode node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTVars node, Object data) {
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            node.jjtGetChild(i).jjtAccept(this, data);
        }
        return null;
    }

    @Override
    public Object visit(ASTVnil node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTCst node, Object data) {
        ObjectType varType = (ObjectType) node.jjtGetChild(0).jjtAccept(this, data); //Var type
        String varIdent = (String) ((ASTIdent) node.jjtGetChild(1)).jjtGetValue();

        if (varIdent == null) {
            throw new RuntimeException("Variable name cannot be null.");
        }
        Object value = null;
        if (node.jjtGetNumChildren() > 2) {
            value = node.jjtGetChild(2).jjtAccept(this, data); // Retrieve the initialized value
        }

        MemoryObject mo = new MemoryObject(varIdent,value, ObjectNature.CST, varType);
        try
        {
            symbolTable.put(mo);
        }
        catch (SymbolTableException ignored)
        {
        }

        return null;
    }

    @Override
    public Object visit(ASTVar node, Object data) {
        ObjectType varType = (ObjectType) node.jjtGetChild(0).jjtAccept(this, data); //Var type
        String varIdent = (String) ((ASTIdent) node.jjtGetChild(1)).jjtGetValue();

        if (varIdent == null) {
            throw new RuntimeException("Variable name cannot be null.");
        }
        Object value = null;
        if (node.jjtGetNumChildren() > 2) {
            value = node.jjtGetChild(2).jjtAccept(this, data); // Retrieve the initialized value
        }

        MemoryObject mo = new MemoryObject(varIdent,value, ObjectNature.VAR, varType);
        try
        {
            symbolTable.put(mo);
        }
        catch (SymbolTableException ignored)
        {
        }
        return null;
    }

    @Override
    public Object visit(ASTTableau node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTOmega node, Object data) {
        return "EMPTY";
    }

    @Override
    public Object visit(ASTMain node, Object data) {
        node.jjtGetChild(0).jjtAccept(this, data); // Visit vars
        node.jjtGetChild(1).jjtAccept(this, data); // Visit instrs
        return null;
    }

    @Override
    public Object visit(ASTEnil node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTEntetes node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTEntete node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTInil node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTInstrs node, Object data) {
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            node.jjtGetChild(i).jjtAccept(this, data);
        }
        return null;
    }

    @Override
    public Object visit(ASTRetour node, Object data) {
        Object returnValue = node.jjtGetChild(0).jjtAccept(this, data);
        toDisplay +=  "Return value is : "+returnValue+"\n";
        return returnValue;
    }

    @Override
    public Object visit(ASTEcrire node, Object data) {
        Object value = node.jjtGetChild(0).jjtAccept(this, data);
        try {
            if (value instanceof String) {
                toDisplay += (String) ((String) value).replace("\"","");
            } else {
                toDisplay += value;
            }
        } catch (Exception e) {
            System.out.println("Exception is : "+ e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Object visit(ASTEcrireLn node, Object data) {
        Object value = node.jjtGetChild(0).jjtAccept(this, data);
        try {
            if (value instanceof String) {
                toDisplay += (String) ((String) value).replace("\"","");
                toDisplay += "\n";
            } else {
                toDisplay += value+"\n";
            }
        } catch (Exception e) {
            System.out.println("Exception is : "+ e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Object visit(ASTSi node, Object data) {
        Object value = node.jjtGetChild(0).jjtAccept(this, data);
        try {
            if ((boolean) value) {
                node.jjtGetChild(1).jjtAccept(this, data);
            } else {
                node.jjtGetChild(2).jjtAccept(this, data);
            }
        } catch (Exception e) {
            System.out.println("Exception is : "+ e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Object visit(ASTTantQue node, Object data) {
        Object value = node.jjtGetChild(0).jjtAccept(this, data);
        try {
            if ((boolean) value) {
                node.jjtGetChild(1).jjtAccept(this, data);
                node.jjtAccept(this, data);
            }
        } catch (Exception e) {
            System.out.println("Exception is : "+ e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Object visit(ASTAffectation node, Object data) {
        try {
            Object value = node.jjtGetChild(1).jjtAccept(this, data);
            String varIdent = (String) ((ASTIdent) node.jjtGetChild(0)).jjtGetValue();;
            MemoryObject mo = symbolTable.get(varIdent);
            symbolTable.update(varIdent,value);
            System.out.println("ASTaffectation -> " + value + " = " + varIdent);
        } catch (Exception e) {
            System.out.println("Exception is : "+ e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Object visit(ASTSomme node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTIncrement node, Object data) {
        String varIdent = (String) ((ASTIdent) node.jjtGetChild(0)).jjtGetValue();
        MemoryObject mo = symbolTable.get(varIdent);
        int val = (int) mo.getValue() + 1;
        try
        {
            symbolTable.put(new MemoryObject(varIdent,val,mo.getNature(),mo.getType()));
        }
        catch (SymbolTableException ignored)
        {
        }
        return val;
    }

    @Override
    public Object visit(ASTAppelI node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTListExp node, Object data) {
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            node.jjtGetChild(i).jjtAccept(this, data);
        }
        return null;
    }

    @Override
    public Object visit(ASTExnil node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTNot node, Object data) {
        boolean exp = (boolean) node.jjtGetChild(0).jjtAccept(this, data);
        return !exp;
    }

    @Override
    public Object visit(ASTNeg node, Object data) {
        int nb = (int) node.jjtGetChild(0).jjtAccept(this, data);
        return -nb;
    }

    @Override
    public Object visit(ASTEt node, Object data) {
        boolean exp1 = (boolean) node.jjtGetChild(0).jjtAccept(this, data);
        boolean exp2 = (boolean) node.jjtGetChild(1).jjtAccept(this, data);
        System.out.println("ASTet -> " + exp1 + " && " + exp2);
        return exp1 && exp2;
    }

    @Override
    public Object visit(ASTOu node, Object data) {
        boolean exp1 = (boolean) node.jjtGetChild(0).jjtAccept(this, data);
        boolean exp2 = (boolean) node.jjtGetChild(1).jjtAccept(this, data);
        System.out.println("ASTou -> " + exp1 + " || " + exp2);
        return exp1 || exp2;
    }

    @Override
    public Object visit(ASTEq node, Object data) {
        return node.jjtGetChild(0).jjtAccept(this, data) == node.jjtGetChild(1).jjtAccept(this, data);
    }

    @Override
    public Object visit(ASTSup node, Object data) {
        int nb1 = (int) node.jjtGetChild(0).jjtAccept(this, data);
        int nb2 = (int) node.jjtGetChild(1).jjtAccept(this, data);
        System.out.println("ASTsup -> " + nb1 + " > " + nb2);
        return nb1 > nb2;
    }

    @Override
    public Object visit(ASTAdd node, Object data) {
        int nb1 = (int) node.jjtGetChild(0).jjtAccept(this, data);
        int nb2 = (int) node.jjtGetChild(1).jjtAccept(this, data);
        System.out.println("ASTadd -> " + nb1 + " + " + nb2);
        return nb1 + nb2;
    }

    @Override
    public Object visit(ASTSub node, Object data) {
        int nb1 = (int) node.jjtGetChild(0).jjtAccept(this, data);
        int nb2 = (int) node.jjtGetChild(1).jjtAccept(this, data);
        System.out.println("ASTsub -> " + nb1 + " - " + nb2);
        return nb1 - nb2;
    }

    @Override
    public Object visit(ASTMul node, Object data) {
        int nb1 = (int) node.jjtGetChild(0).jjtAccept(this, data);
        int nb2 = (int) node.jjtGetChild(1).jjtAccept(this, data);
        System.out.println("ASTmul -> " + nb1 + " * " + nb2);
        return nb1 * nb2;
    }

    @Override
    public Object visit(ASTDiv node, Object data) {
        int nb1 = (int) node.jjtGetChild(0).jjtAccept(this, data);
        int nb2 = (int) node.jjtGetChild(1).jjtAccept(this, data);
        System.out.println("ASTdiv -> " + nb1 + " / " + nb2);
        return nb1 / nb2;
    }

    @Override
    public Object visit(ASTLongeur node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTVrai node, Object data) {
        return true;
    }

    @Override
    public Object visit(ASTFaux node, Object data) {
        return false;
    }

    @Override
    public Object visit(ASTExp node, Object data) {
        return node.jjtGetChild(0).jjtAccept(this, data);
    }

    @Override
    public Object visit(ASTAppelE node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTTab node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTRien node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTEntier node, Object data) {
        return ObjectType.INT;
    }

    @Override
    public Object visit(ASTBooleen node, Object data) {
        return ObjectType.BOOLEAN;
    }

    @Override
    public Object visit(ASTNbre node, Object data) {
        return node.jjtGetValue();
    }

    @Override
    public Object visit(ASTChaine node, Object data) {
        return node.jjtGetValue();
    }
}
