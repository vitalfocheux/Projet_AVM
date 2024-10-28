package fr.m1comp5.Compiler;

import fr.m1comp5.Analyzer.jjc.generated.Node;
import fr.m1comp5.Analyzer.mjj.generated.*;
import fr.m1comp5.Analyzer.jjc.generated.*;
import fr.m1comp5.Analyzer.mjj.generated.SimpleNode;

import java.util.ArrayList;
import java.util.List;

public class CompilerVisitor implements MiniJajaVisitor {
    List<fr.m1comp5.Analyzer.jjc.generated.Node> instrs;

    public CompilerVisitor() {
        instrs = new ArrayList<>();
    }

    public List<fr.m1comp5.Analyzer.jjc.generated.Node> getInstrs() {
        return instrs;
    }

    @Override
    public Object visit(SimpleNode node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTroot node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTclasse node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTident node, Object data) {
        return null;
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
        return null;
    }

    @Override
    public Object visit(ASTtableau node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTvar node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTomega node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTmain node, Object data) {
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
        return null;
    }

    @Override
    public Object visit(ASTretour node, Object data) {
        return null;
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
        return null;
    }

    @Override
    public Object visit(ASTneg node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTet node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTou node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTeq node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTsup node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTadd node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTsub node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTmul node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTdiv node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTlongeur node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTvrai node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTfaux node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTexp node, Object data) {
        return null;
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
        return null;
    }

    @Override
    public Object visit(ASTbooleen node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTnbre node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTchaine node, Object data) {
        return null;
    }
}
