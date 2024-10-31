package fr.m1comp5.Compiler;

import fr.m1comp5.Analyzer.jjc.generated.*;
import fr.m1comp5.Analyzer.mjj.generated.*;
import fr.m1comp5.Analyzer.mjj.generated.Node;
import fr.m1comp5.Analyzer.mjj.generated.SimpleNode;
import java.util.ArrayList;
import java.util.List;

import static fr.m1comp5.Analyzer.jjc.generated.JajaCodeTreeConstants.*;

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
        DataModel dm = (DataModel) data;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        if (m == Mode.DEFAULT) {
            ASTInit nodeInit = new ASTInit(JJTINIT);
            ASTPop nodePop = new ASTPop(JJTPOP);
            ASTJcStop nodeJcStop = new ASTJcStop(JJTJCSTOP);

            instrs.add(nodeInit);

            int ndss = (int) node.jjtGetChild(1).jjtAccept(this, new DataModel(n+1, Mode.DEFAULT));
            int nmma = (int) node.jjtGetChild(2).jjtAccept(this, new DataModel(n+ndss+1, Mode.DEFAULT));
            int nrdss = (int) node.jjtGetChild(1).jjtAccept(this, new DataModel(n+ndss+nmma+1, Mode.RETRAIT));

            instrs.add(nodePop);
            instrs.add(nodeJcStop);

            return ndss+nmma+nrdss+3;
        }

        return 0;
    }

    @Override
    public Object visit(ASTident node, Object data) {
        ASTLoad loadNode = new ASTLoad(JJTLOAD);
        ASTJcIdent varIdent = new ASTJcIdent(JJTJCIDENT);

        varIdent.jjtSetValue(node.jjtGetValue());
        loadNode.jjtAddChild(varIdent,0);

        instrs.add(loadNode);

        return 1;
    }

    @Override
    public Object visit(ASTdecls node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTmethode node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTvars node, Object data) {
        DataModel dm = (DataModel) data;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        if (m == Mode.DEFAULT) {
            int ndv = (int) node.jjtGetChild(0).jjtAccept(this, new DataModel(n, Mode.DEFAULT));
            int ndvs = (int) node.jjtGetChild(1).jjtAccept(this, new DataModel(n+ndv, Mode.DEFAULT));

            return ndv+ndvs;
        }

        return 0;
    }

    @Override
    public Object visit(ASTvnil node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTcst node, Object data) {
        DataModel dm = (DataModel) data;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        if (m == Mode.DEFAULT) {
            ASTNew newVar = new ASTNew(JJTNEW);

            ASTJcIdent varIdent = new ASTJcIdent(JJTJCIDENT);
            varIdent.jjtSetValue(((ASTident) node.jjtGetChild(1)).jjtGetValue());

            ASTType varType = new ASTType(JJTTYPE);
            if (node.jjtGetChild(0) instanceof ASTentier) {
                varType.jjtSetValue("entier");
            } else {
                varType.jjtSetValue("booleen");
            }

            ASTSorte varNature = new ASTSorte(JJTSORTE);
            varNature.jjtSetValue("cst");

            ASTJcNbre varValue = new ASTJcNbre(JJTJCNBRE);
            varValue.jjtSetValue(0);

            newVar.jjtAddChild(varIdent,0);
            newVar.jjtAddChild(varType,1);
            newVar.jjtAddChild(varNature,2);
            newVar.jjtAddChild(varValue,3);

            int ne = (int) node.jjtGetChild(2).jjtAccept(this, data);
            instrs.add(newVar);

            return ne+1;
        }

        if (m == Mode.RETRAIT) {
            return retraitDss();
        }

        return 0;
    }

    @Override
    public Object visit(ASTvar node, Object data) {
        DataModel dm = (DataModel) data;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        if (m == Mode.DEFAULT) {
            ASTNew newVar = new ASTNew(JJTNEW);

            ASTJcIdent varIdent = new ASTJcIdent(JJTJCIDENT);
            varIdent.jjtSetValue(((ASTident) node.jjtGetChild(1)).jjtGetValue());

            ASTType varType = new ASTType(JJTTYPE);
            if (node.jjtGetChild(0) instanceof ASTentier) {
                varType.jjtSetValue("entier");
            } else {
                varType.jjtSetValue("booleen");
            }

            ASTSorte varNature = new ASTSorte(JJTSORTE);
            varNature.jjtSetValue("var");

            ASTJcNbre varValue = new ASTJcNbre(JJTJCNBRE);
            varValue.jjtSetValue(0);

            newVar.jjtAddChild(varIdent,0);
            newVar.jjtAddChild(varType,1);
            newVar.jjtAddChild(varNature,2);
            newVar.jjtAddChild(varValue,3);

            int ne = (int) node.jjtGetChild(2).jjtAccept(this, data);
            instrs.add(newVar);

            return ne+1;
        }
        if (m == Mode.RETRAIT) {
            return retraitDss();
        }
        return 0;
    }

    @Override
    public Object visit(ASTtableau node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTomega node, Object data) {
        ASTPush push = new ASTPush(JJTPUSH);
        ASTJcChaine var = new ASTJcChaine(JJTJCCHAINE);

        push.jjtAddChild(var,0);
        instrs.add(push);

        return 1;
    }

    @Override
    public Object visit(ASTmain node, Object data) {
        DataModel dm = (DataModel) data;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        if (m == Mode.DEFAULT) {
            ASTPush push = new ASTPush(JJTPUSH);
            ASTJcNbre var = new ASTJcNbre(JJTJCNBRE);

            push.jjtAddChild(var,0);

            int ndvs = (int) node.jjtGetChild(0).jjtAccept(this, data);
            int niss = (int) node.jjtGetChild(1).jjtAccept(this, new DataModel(n+ndvs, Mode.DEFAULT));

            instrs.add(push);

            int nrdvs = (int) node.jjtGetChild(0).jjtAccept(this, new DataModel(n+ndvs+niss+1, Mode.RETRAIT));

            return ndvs+niss+nrdvs+1;
        }

        return 0;
    }

    @Override
    public Object visit(ASTenil node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTentetes node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTentete node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTinil node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTinstrs node, Object data) {
        DataModel dm = (DataModel) data;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        int nis = (int) node.jjtGetChild(0).jjtAccept(this, new DataModel(n, Mode.DEFAULT));
        int niss = (int) node.jjtGetChild(1).jjtAccept(this, new DataModel(n+nis, Mode.DEFAULT));

        return nis+niss;
    }

    @Override
    public Object visit(ASTretour node, Object data) {
        DataModel dm = (DataModel) data;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        int ne = (int) node.jjtGetChild(0).jjtAccept(this, data);

        return ne;
    }

    @Override
    public Object visit(ASTecrire node, Object data) {
        ASTWrite write = new ASTWrite(JJTWRITE);

        int ne = (int) node.jjtGetChild(0).jjtAccept(this, data);

        instrs.add(write);

        return ne+1;

    }

    @Override
    public Object visit(ASTecrireln node, Object data) {
        ASTWriteLn writeln = new ASTWriteLn(JJTWRITELN);

        int ne = (int) node.jjtGetChild(0).jjtAccept(this, data);

        instrs.add(writeln);

        return ne+1;
    }

    @Override
    public Object visit(ASTsi node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTtantque node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTaffectation node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTsomme node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTincrement node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTappelI node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTlistexp node, Object data) {
        DataModel dm = (DataModel) data;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        if (m == Mode.DEFAULT) {
            int ne = (int) node.jjtGetChild(0).jjtAccept(this, new DataModel(n, Mode.DEFAULT));
            int nlexp = (int) node.jjtGetChild(1).jjtAccept(this, new DataModel(n+ne, Mode.DEFAULT));

            return ne+nlexp;
        }

        return 0;
    }

    @Override
    public Object visit(ASTexnil node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTnot node, Object data) {
        return compileOp1(node, data);
    }

    @Override
    public Object visit(ASTneg node, Object data) {
        return compileOp1(node, data);
    }

    @Override
    public Object visit(ASTet node, Object data) {
        return compileOp2(node, data);
    }

    @Override
    public Object visit(ASTou node, Object data) {
        return compileOp2(node, data);
    }

    @Override
    public Object visit(ASTeq node, Object data) {
        return compileOp2(node, data);
    }

    @Override
    public Object visit(ASTsup node, Object data) {
        return compileOp2(node, data);
    }

    @Override
    public Object visit(ASTadd node, Object data) {
        return compileOp2(node, data);
    }

    @Override
    public Object visit(ASTsub node, Object data) {
        return compileOp2(node, data);
    }

    @Override
    public Object visit(ASTmul node, Object data) {
        return compileOp2(node, data);
    }

    @Override
    public Object visit(ASTdiv node, Object data) {
        return compileOp2(node, data);
    }

    @Override
    public Object visit(ASTlongeur node, Object data) {
        ASTLength lengthNode = new ASTLength(JJTLENGTH);
        ASTJcIdent ident = new ASTJcIdent(JJTJCIDENT);

        ident.jjtSetValue(((ASTident) node.jjtGetChild(0)).jjtGetValue());
        lengthNode.jjtAddChild(ident,0);

        instrs.add(lengthNode);

        return 1;
    }

    @Override
    public Object visit(ASTvrai node, Object data) {
        ASTPush push = new ASTPush(JJTPUSH);
        ASTJcVrai tr = new ASTJcVrai(JJTJCVRAI);

        tr.jjtSetValue(true);
        push.jjtAddChild(tr, 0);

        instrs.add(push);

        return 1;
    }

    @Override
    public Object visit(ASTfaux node, Object data) {
        ASTPush push = new ASTPush(JJTPUSH);
        ASTJcFalse fl = new ASTJcFalse(JJTJCFALSE);

        fl.jjtSetValue(false);
        push.jjtAddChild(fl, 0);

        instrs.add(push);

        return 1;
    }

    @Override
    public Object visit(ASTexp node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTappelE node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTtab node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTrien node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTentier node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTbooleen node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTnbre node, Object data) {
        ASTPush push = new ASTPush(JJTPUSH);
        ASTJcNbre nb = new ASTJcNbre(JJTJCNBRE);

        nb.jjtSetValue(node.jjtGetValue());
        push.jjtAddChild(nb, 0);

        instrs.add(push);

        return 1;
    }

    @Override
    public Object visit(ASTchaine node, Object data) {
        ASTPush push = new ASTPush(JJTPUSH);
        ASTJcChaine str = new ASTJcChaine(JJTJCCHAINE);

        str.jjtSetValue(node.jjtGetValue());
        push.jjtAddChild(str, 0);

        instrs.add(push);

        return 1;
    }


    private fr.m1comp5.Analyzer.jjc.generated.Node getNodeOp(Node node) {
        fr.m1comp5.Analyzer.jjc.generated.Node opNode = null;

        if (node instanceof ASTadd) opNode = new ASTAdd(JJTADD);
        else if (node instanceof ASTsub) opNode = new ASTSub(JJTSUB);
        else if (node instanceof ASTmul) opNode = new ASTMul(JJTMUL);
        else if (node instanceof ASTdiv) opNode = new ASTDiv(JJTDIV);
        else if (node instanceof ASTsup) opNode = new ASTSup(JJTSUP);
        else if (node instanceof ASTeq) opNode = new ASTCmp(JJTCMP);
        else if (node instanceof ASTou) opNode = new ASTOr(JJTOR);
        else if (node instanceof ASTet) opNode = new ASTAnd(JJTAND);

        return opNode;
    }

    private int compileOp2(Node node, Object data) {
        DataModel dm = (DataModel) data;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        if (m == Mode.DEFAULT) {
            int ne = (int) node.jjtGetChild(0).jjtAccept(this, new DataModel(n, Mode.DEFAULT));
            int ne1 = (int) node.jjtGetChild(1).jjtAccept(this, new DataModel(n + ne, Mode.DEFAULT));

            instrs.add(getNodeOp(node));

            return ne+ne1+1;
        }

        return 0;
    }

    private int compileOp1(Node node, Object data) {
        DataModel dm = (DataModel) data;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        if (m == Mode.DEFAULT) {
            int ne = (int) node.jjtGetChild(0).jjtAccept(this, new DataModel(n, Mode.DEFAULT));

            instrs.add(getNodeOp(node));

            return ne+1;
        }

        return 0;
    }

    private int retraitDss() {
        ASTSwap swap = new ASTSwap(JJTSWAP);
        ASTPop pop = new ASTPop(JJTPOP);

        instrs.add(swap);
        instrs.add(pop);

        return 2;
    }
}
