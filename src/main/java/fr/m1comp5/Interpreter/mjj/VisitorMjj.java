package fr.m1comp5.Interpreter.mjj;

import fr.m1comp5.Analyzer.*;
import fr.m1comp5.Analyzer.Type;
import fr.m1comp5.Memory.SymbolTable;

public class VisitorMjj implements MiniJajaVisitor {

    private SymbolTable symbolTable;

    public VisitorMjj() {
        this.symbolTable = new SymbolTable();
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
        System.out.println("Class : "+ ident);

        node.jjtGetChild(1).jjtAccept(this, data); //Visit vars or vnil
        node.jjtGetChild(2).jjtAccept(this, data); //Visit main
        return null;
    }

    @Override
    public Object visit(ASTident node, Object data) {
        String varIdent = (String) node.jjtGetValue();

        if (node.jjtGetParent() instanceof ASTvar) {
            return varIdent;
        }
        return symbolTable.get(varIdent);
    }

    @Override
    public Object visit(ASTvnil node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTdecls node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTcst node, Object data) {
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
    public Object visit(ASTtableau node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTvar node, Object data) {
        Type varType = (Type) node.jjtGetChild(0).jjtAccept(this, data); //Var type
        String varIdent = (String) node.jjtGetChild(1).jjtAccept(this, data); //Var name
        Object value = null;
        if (node.jjtGetNumChildren() > 2) {
            value = node.jjtGetChild(2).jjtAccept(this, data); // Retrieve the initialized value
        }

        if (varIdent == null) {
            throw new RuntimeException("Variable name cannot be null.");
        }

        symbolTable.put(varIdent, value);
//        System.out.println("Declared var: "+varType+" "+varIdent+ " = "+value);
//        symbolTable.getTable().forEach((key,val) -> System.out.println(key +": "+ val));

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
        System.out.println("Return Value ! "+returnValue);
        return returnValue;
    }

    @Override
    public Object visit(ASTecrire node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTecrireln node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTsi node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTtantque node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTaffectation node, Object data) {
        String varIdent = (String) node.jjtGetChild(0).jjtAccept(this, data);
        Object value = node.jjtGetChild(1).jjtAccept(this, data);

        symbolTable.put(varIdent, value);

        System.out.println("Assigned " + value + " to var " + varIdent);
        return null;
    }

    @Override
    public Object visit(ASTsomme node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTincrement node, Object data) {
        return null;
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
        return exp1 && exp2;
    }

    @Override
    public Object visit(ASTou node, Object data) {
        boolean exp1 = (boolean) node.jjtGetChild(0).jjtAccept(this, data);
        boolean exp2 = (boolean) node.jjtGetChild(1).jjtAccept(this, data);
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
        return nb1 > nb2;
    }

    @Override
    public Object visit(ASTadd node, Object data) {
        int nb1 = (int) node.jjtGetChild(0).jjtAccept(this, data);
        int nb2 = (int) node.jjtGetChild(1).jjtAccept(this, data);
        return nb1 + nb2;
    }

    @Override
    public Object visit(ASTsub node, Object data) {
        int nb1 = (int) node.jjtGetChild(0).jjtAccept(this, data);
        int nb2 = (int) node.jjtGetChild(1).jjtAccept(this, data);
        System.out.println(nb1 + "-" + nb2);
        return nb1 - nb2;
    }

    @Override
    public Object visit(ASTmul node, Object data) {
        int nb1 = (int) node.jjtGetChild(0).jjtAccept(this, data);
        int nb2 = (int) node.jjtGetChild(1).jjtAccept(this, data);
        return nb1 * nb2;
    }

    @Override
    public Object visit(ASTdiv node, Object data) {
        int nb1 = (int) node.jjtGetChild(0).jjtAccept(this, data);
        int nb2 = (int) node.jjtGetChild(1).jjtAccept(this, data);
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
        return Type.INTEGER;
    }

    @Override
    public Object visit(ASTbooleen node, Object data) {
        return Type.BOOLEAN;
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
