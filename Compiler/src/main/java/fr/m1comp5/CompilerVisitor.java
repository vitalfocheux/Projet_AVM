package fr.m1comp5;

import fr.m1comp5.jjc.generated.*;
import fr.m1comp5.mjj.generated.*;
import fr.m1comp5.mjj.generated.ASTAdd;
import fr.m1comp5.mjj.generated.ASTDiv;
import fr.m1comp5.mjj.generated.ASTMul;
import fr.m1comp5.mjj.generated.ASTNeg;
import fr.m1comp5.mjj.generated.ASTNot;
import fr.m1comp5.mjj.generated.ASTRoot;
import fr.m1comp5.mjj.generated.ASTSub;
import fr.m1comp5.mjj.generated.ASTSup;
import fr.m1comp5.mjj.generated.Node;
import fr.m1comp5.mjj.generated.SimpleNode;

import java.util.ArrayList;
import java.util.List;

import static fr.m1comp5.jjc.generated.JajaCodeTreeConstants.*;

public class CompilerVisitor implements MiniJajaVisitor {
    List<fr.m1comp5.jjc.generated.Node> instrs;

    public CompilerVisitor() {
        instrs = new ArrayList<>();
    }

    public List<fr.m1comp5.jjc.generated.Node> getInstrs() {
        return instrs;
    }

    @Override
    public Object visit(SimpleNode node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTRoot node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTClasse node, Object data) {
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
    public Object visit(ASTIdent node, Object data) {
        ASTLoad loadNode = new ASTLoad(JJTLOAD);
        ASTJcIdent varIdent = new ASTJcIdent(JJTJCIDENT);

        varIdent.jjtSetValue(node.jjtGetValue());
        loadNode.jjtAddChild(varIdent,0);

        instrs.add(loadNode);

        return 1;
    }

    @Override
    public Object visit(ASTDecls node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTMethode node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTVars node, Object data) {
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
    public Object visit(ASTVnil node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTCst node, Object data) {
        DataModel dm = (DataModel) data;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        if (m == Mode.DEFAULT) {
            ASTNew newVar = new ASTNew(JJTNEW);

            ASTJcIdent varIdent = new ASTJcIdent(JJTJCIDENT);
            varIdent.jjtSetValue(((ASTIdent) node.jjtGetChild(1)).jjtGetValue());

            ASTType varType = new ASTType(JJTTYPE);
            if (node.jjtGetChild(0) instanceof ASTEntier) {
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
    public Object visit(ASTVar node, Object data) {
        DataModel dm = (DataModel) data;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        if (m == Mode.DEFAULT) {
            ASTNew newVar = new ASTNew(JJTNEW);

            ASTJcIdent varIdent = new ASTJcIdent(JJTJCIDENT);
            varIdent.jjtSetValue(((ASTIdent) node.jjtGetChild(1)).jjtGetValue());

            ASTType varType = new ASTType(JJTTYPE);
            if (node.jjtGetChild(0) instanceof ASTEntier) {
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
    public Object visit(ASTTableau node, Object data) {
        DataModel dm = (DataModel) data;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        if (m == Mode.DEFAULT) {
            ASTNewArray newTab = new ASTNewArray(JJTNEWARRAY);

            ASTJcIdent tabIdent = new ASTJcIdent(JJTJCIDENT);
            tabIdent.jjtSetValue(((ASTIdent) node.jjtGetChild(1)).jjtGetValue());

            ASTType tabType = new ASTType(JJTTYPE);
            if (node.jjtGetChild(0) instanceof ASTEntier) {
                tabType.jjtSetValue("entier");
            } else {
                tabType.jjtSetValue("booleen");
            }

            newTab.jjtAddChild(tabIdent,0);
            newTab.jjtAddChild(tabType,1);

            int ne = (int) node.jjtGetChild(2).jjtAccept(this, data);
            instrs.add(newTab);

            return ne+1;
        }
        if (m == Mode.RETRAIT) {
            return retraitDss();
        }
        return 0;
    }

    @Override
    public Object visit(ASTOmega node, Object data) {
        ASTPush push = new ASTPush(JJTPUSH);
        ASTJcChaine var = new ASTJcChaine(JJTJCCHAINE);

        push.jjtAddChild(var,0);
        instrs.add(push);

        return 1;
    }

    @Override
    public Object visit(ASTMain node, Object data) {
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
    public Object visit(ASTEnil node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTEntetes node, Object data) {
        DataModel dm = (DataModel) data;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        if (m == Mode.DEFAULT) {
            int nens = (int) node.jjtGetChild(1).jjtAccept(this, data);
            int nen = (int) node.jjtGetChild(0).jjtAccept(this, new DataModel(n+nens, Mode.DEFAULT));

            return nens+nen;
        }

        return 0;
    }

    @Override
    public Object visit(ASTEntete node, Object data) {
        ASTNew nNew = new ASTNew(JJTNEW);

        ASTJcIdent varIdent = new ASTJcIdent(JJTJCIDENT);
        varIdent.jjtSetValue(((ASTIdent) node.jjtGetChild(1)).jjtGetValue());

        ASTType varType = new ASTType(JJTTYPE);
        varType.jjtSetValue(node.jjtGetChild(0));

        ASTSorte varNature = new ASTSorte(JJTSORTE);
        varNature.jjtSetValue("var");

        ASTJcNbre varValue = new ASTJcNbre(JJTJCNBRE);
        varValue.jjtSetValue(());

        nNew.jjtAddChild(varIdent,0);
        nNew.jjtAddChild(varType,1);
        nNew.jjtAddChild(varNature,2);
        nNew.jjtAddChild(varValue,3);

        int ne = (int) node.jjtGetChild(2).jjtAccept(this, data);
        instrs.add(nNew);

        return 1;
    }

    @Override
    public Object visit(ASTInil node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTInstrs node, Object data) {
        DataModel dm = (DataModel) data;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        int nis = (int) node.jjtGetChild(0).jjtAccept(this, new DataModel(n, Mode.DEFAULT));
        int niss = (int) node.jjtGetChild(1).jjtAccept(this, new DataModel(n+nis, Mode.DEFAULT));

        return nis+niss;
    }

    @Override
    public Object visit(ASTRetour node, Object data) {
        DataModel dm = (DataModel) data;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        int ne = (int) node.jjtGetChild(0).jjtAccept(this, data);

        return ne;
    }

    @Override
    public Object visit(ASTEcrire node, Object data) {
        ASTWrite write = new ASTWrite(JJTWRITE);

        int ne = (int) node.jjtGetChild(0).jjtAccept(this, data);

        instrs.add(write);

        return ne+1;

    }

    @Override
    public Object visit(ASTEcrireLn node, Object data) {
        ASTWriteLn writeln = new ASTWriteLn(JJTWRITELN);

        int ne = (int) node.jjtGetChild(0).jjtAccept(this, data);

        instrs.add(writeln);

        return ne+1;
    }

    @Override
    public Object visit(ASTSi node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTTantQue node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTAffectation node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTSomme node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTIncrement node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTAppelI node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTListExp node, Object data) {
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
    public Object visit(ASTExnil node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTNot node, Object data) {
        return compileOp1(node, data);
    }

    @Override
    public Object visit(ASTNeg node, Object data) {
        return compileOp1(node, data);
    }

    @Override
    public Object visit(ASTEt node, Object data) {
        return compileOp2(node, data);
    }

    @Override
    public Object visit(ASTOu node, Object data) {
        return compileOp2(node, data);
    }

    @Override
    public Object visit(ASTEq node, Object data) {
        return compileOp2(node, data);
    }

    @Override
    public Object visit(ASTSup node, Object data) {
        return compileOp2(node, data);
    }

    @Override
    public Object visit(ASTAdd node, Object data) {
        return compileOp2(node, data);
    }

    @Override
    public Object visit(ASTSub node, Object data) {
        return compileOp2(node, data);
    }

    @Override
    public Object visit(ASTMul node, Object data) {
        return compileOp2(node, data);
    }

    @Override
    public Object visit(ASTDiv node, Object data) {
        return compileOp2(node, data);
    }

    @Override
    public Object visit(ASTLongeur node, Object data) {
        ASTLength lengthNode = new ASTLength(JJTLENGTH);
        ASTJcIdent ident = new ASTJcIdent(JJTJCIDENT);

        ident.jjtSetValue(((ASTIdent) node.jjtGetChild(0)).jjtGetValue());
        lengthNode.jjtAddChild(ident,0);

        instrs.add(lengthNode);

        return 1;
    }

    @Override
    public Object visit(ASTVrai node, Object data) {
        ASTPush push = new ASTPush(JJTPUSH);
        ASTJcVrai tr = new ASTJcVrai(JJTJCVRAI);

        tr.jjtSetValue(true);
        push.jjtAddChild(tr, 0);

        instrs.add(push);

        return 1;
    }

    @Override
    public Object visit(ASTFaux node, Object data) {
        ASTPush push = new ASTPush(JJTPUSH);
        ASTJcFalse fl = new ASTJcFalse(JJTJCFALSE);

        fl.jjtSetValue(false);
        push.jjtAddChild(fl, 0);

        instrs.add(push);

        return 1;
    }

    @Override
    public Object visit(ASTExp node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTAppelE node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTTab node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTRien node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTEntier node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTBooleen node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTNbre node, Object data) {
        ASTPush push = new ASTPush(JJTPUSH);
        ASTJcNbre nb = new ASTJcNbre(JJTJCNBRE);

        nb.jjtSetValue(node.jjtGetValue());
        push.jjtAddChild(nb, 0);

        instrs.add(push);

        return 1;
    }

    @Override
    public Object visit(ASTChaine node, Object data) {
        ASTPush push = new ASTPush(JJTPUSH);
        ASTJcChaine str = new ASTJcChaine(JJTJCCHAINE);

        str.jjtSetValue(node.jjtGetValue());
        push.jjtAddChild(str, 0);

        instrs.add(push);

        return 1;
    }


    private fr.m1comp5.jjc.generated.Node getNodeOp(Node node) {
        fr.m1comp5.jjc.generated.Node opNode = null;

        if (node instanceof ASTAdd) opNode = (fr.m1comp5.jjc.generated.Node) new ASTAdd(JJTADD);
        else if (node instanceof ASTSub) opNode = (fr.m1comp5.jjc.generated.Node) new ASTSub(JJTSUB);
        else if (node instanceof ASTMul) opNode = (fr.m1comp5.jjc.generated.Node) new ASTMul(JJTMUL);
        else if (node instanceof ASTDiv) opNode = (fr.m1comp5.jjc.generated.Node) new ASTDiv(JJTDIV);
        else if (node instanceof ASTSup) opNode = (fr.m1comp5.jjc.generated.Node) new ASTSup(JJTSUP);
        else if (node instanceof ASTEq) opNode = new ASTCmp(JJTCMP);
        else if (node instanceof ASTOu) opNode = new ASTOr(JJTOR);
        else if (node instanceof ASTEt) opNode = new ASTAnd(JJTAND);

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
