package fr.m1comp5;

import fr.m1comp5.custom.exception.VisitorException;
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
        return 0;
    }

    @Override
    public Object visit(ASTRoot node, Object data) throws VisitorException {
        node.childrenAccept(this, data);
        return 0;
    }

    @Override
    public Object visit(ASTClasse node, Object data) throws VisitorException {
        DataModel dm = (DataModel) data;
        dm.data[0] = instrs.size()+1;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        if (m == Mode.DEFAULT)  {
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
    public Object visit(ASTDecls node, Object data) throws VisitorException {
        return compileDeclsVars(node, data);
    }

    @Override
    public Object visit(ASTVars node, Object data) throws VisitorException {
        return compileDeclsVars(node, data);
    }

    @Override
    public Object visit(ASTVnil node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTVar node, Object data) throws VisitorException {
        return compileVarCst(node, data, "var");
    }

    @Override
    public Object visit(ASTCst node, Object data) throws VisitorException {
        return compileVarCst(node, data, "cst");
    }

    @Override
    public Object visit(ASTTableau node, Object data) throws VisitorException {
        DataModel dm = (DataModel) data;
        dm.data[0] = instrs.size()+1;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        if (m == Mode.DEFAULT) {
            int ne = (int) node.jjtGetChild(2).jjtAccept(this, data);

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

            instrs.add(newTab);

            return ne+1;
        }
        if (m == Mode.RETRAIT) {
            return retraitDss();
        }
        return 0;
    }

    @Override
    public Object visit(ASTEntetes node, Object data) throws VisitorException {
        DataModel dm = (DataModel) data;
        dm.data[0] = instrs.size()+1;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];
        int nh = -1;
        if (dm.data.length == 3) {
            nh = (int) dm.data[2];
        }

        if (m == Mode.DEFAULT) {
            int nens = (int) node.jjtGetChild(1).jjtAccept(this, new DataModel(n, Mode.DEFAULT, nh-1));
            int nen = (int) node.jjtGetChild(0).jjtAccept(this, new DataModel(n+nens, Mode.DEFAULT, nh));

            return nens+nen;
        } else if (m == Mode.RETRAIT) {
            ASTSwap nSwap = new ASTSwap(JJTSWAP);
            instrs.add(nSwap);

            ASTPop nPop = new ASTPop(JJTPOP);
            instrs.add(nPop);
            int nrlexp = (int) node.jjtGetChild(1).jjtAccept(this, data);

            return nrlexp + 2;
        }

        return 0;
    }

    @Override
    public Object visit(ASTEntete node, Object data) {
        DataModel dm = (DataModel) data;
        dm.data[0] = instrs.size()+1;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        ASTNew nNew = new ASTNew(JJTNEW);

        ASTJcIdent varIdent = new ASTJcIdent(JJTJCIDENT);
        varIdent.jjtSetValue(((ASTIdent) node.jjtGetChild(1)).jjtGetValue());

        ASTType varType = new ASTType(JJTTYPE);
        varType.jjtSetValue(node.jjtGetChild(0));

        ASTSorte varNature = new ASTSorte(JJTSORTE);
        varNature.jjtSetValue("var");

        ASTJcNbre varValue = new ASTJcNbre(JJTJCNBRE);
        varValue.jjtSetValue(dm.data[2]);

        nNew.jjtAddChild(varIdent,0);
        nNew.jjtAddChild(varType,1);
        nNew.jjtAddChild(varNature,2);
        nNew.jjtAddChild(varValue,3);

        instrs.add(nNew);

        return 1;
    }

    @Override
    public Object visit(ASTMain node, Object data) throws VisitorException {
        DataModel dm = (DataModel) data;
        dm.data[0] = instrs.size()+1;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        if (m == Mode.DEFAULT) {
            int ndvs = (int) node.jjtGetChild(0).jjtAccept(this, data);
            int niss = (int) node.jjtGetChild(1).jjtAccept(this, new DataModel(n+ndvs, Mode.DEFAULT));

            ASTPush push = new ASTPush(JJTPUSH);
            ASTJcNbre var = new ASTJcNbre(JJTJCNBRE);
            var.jjtSetValue(0);
            push.jjtAddChild(var,0);

            instrs.add(push);

            int nrdvs = (int) node.jjtGetChild(0).jjtAccept(this, new DataModel(n+ndvs+niss+1, Mode.RETRAIT));

            return ndvs+niss+nrdvs+1;
        }

        return 0;
    }

    @Override
    public Object visit(ASTMethode node, Object data) throws VisitorException {
        DataModel dm = (DataModel) data;
        dm.data[0] = instrs.size()+1;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        if (m == Mode.DEFAULT) {
            // Get method ident, type, headers, var declarations and instructions
            var methType = node.jjtGetChild(0);
            int addrIncr = methType instanceof ASTRien ? 6 : 5;

            ASTPush nPush = new ASTPush(JJTPUSH);
            ASTJcNbre pushNbre = new ASTJcNbre(JJTJCNBRE);
            System.out.println(instrs.size());
            pushNbre.jjtSetValue(n+3);
            nPush.jjtAddChild(pushNbre,0);
            instrs.add(nPush);

            ASTNew nNew = new ASTNew(JJTNEW);

            ASTType methTypeJjc = new ASTType(JJTTYPE);
            methTypeJjc.jjtSetValue(methType);

            ASTJcIdent methIdentJjc = new ASTJcIdent(JJTJCIDENT);
            methIdentJjc.jjtSetValue(((ASTIdent) node.jjtGetChild(1)).jjtGetValue());

            ASTSorte methSorte = new ASTSorte(JJTSORTE);
            methSorte.jjtSetValue("meth");

            ASTJcNbre methNbre = new ASTJcNbre(JJTJCNBRE);
            methNbre.jjtSetValue(0);

            nNew.jjtAddChild(methIdentJjc,0);
            nNew.jjtAddChild(methTypeJjc,1);
            nNew.jjtAddChild(methSorte,2);
            nNew.jjtAddChild(methNbre,3);

            instrs.add(nNew);

            ASTGoTo nGoTo = new ASTGoTo(JJTGOTO);
            instrs.add(nGoTo);

            int count = 0;
            if (node.jjtGetChild(2) instanceof ASTEntetes) {
                ASTEntetes entetes = (ASTEntetes) node.jjtGetChild(2);
                while (entetes instanceof ASTEntetes) {
                    count++;
                    if (entetes.jjtGetChild(1) instanceof ASTEntetes) {
                        entetes = (ASTEntetes) entetes.jjtGetChild(1);
                    } else {
                        break;
                    }
                }
            }

            int nens = (int) node.jjtGetChild(2).jjtAccept(this, new DataModel(n+3, Mode.DEFAULT, count));
            int ndvs = (int) node.jjtGetChild(3).jjtAccept(this, new DataModel(n+3+nens, Mode.DEFAULT));
            int niss = (int) node.jjtGetChild(4).jjtAccept(this, new DataModel(n+3+nens+ndvs, Mode.DEFAULT));

            if (methType instanceof ASTRien) {
                ASTPush nPushVoid = new ASTPush(JJTPUSH);
                ASTJcNbre zero = new ASTJcNbre(JJTJCNBRE);

                zero.jjtSetValue(0);
                nPushVoid.jjtAddChild(zero,0);

                instrs.add(nPushVoid);
            }

            int nrdvs = (int) node.jjtGetChild(3).jjtAccept(this, new DataModel(n+nens+ndvs+niss+3, Mode.RETRAIT));

//            System.out.println("number line created for exp : "+ nens);
//            System.out.println("number line created for vars : "+ ndvs);
//            System.out.println("number line created for instrs : "+ niss);
//            System.out.println("number line created for rdvs : "+ nrdvs);

            ASTJcNbre addrGoTo = new ASTJcNbre(JJTJCNBRE);
            addrGoTo.jjtSetValue(n+nens+ndvs+niss+nrdvs+addrIncr);
            nGoTo.jjtAddChild(addrGoTo,0);

            ASTSwap nSwap = new ASTSwap(JJTSWAP);
            instrs.add(nSwap);

            ASTReturn nReturn = new ASTReturn(JJTRETURN);
            instrs.add(nReturn);

            return n+nens+ndvs+niss+nrdvs+addrIncr+1;
        }
        if (m == Mode.RETRAIT) {
            return retraitDss();
        }

        return 0;
    }

    @Override
    public Object visit(ASTEnil node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTInil node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTInstrs node, Object data) throws VisitorException {
        DataModel dm = (DataModel) data;
        dm.data[0] = instrs.size()+1;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        int nis = (int) node.jjtGetChild(0).jjtAccept(this, new DataModel(n, Mode.DEFAULT));
        int niss = (int) node.jjtGetChild(1).jjtAccept(this, new DataModel(n+nis, Mode.DEFAULT));

        return nis+niss;
    }

    @Override
    public Object visit(ASTSomme node, Object data) throws VisitorException {
        DataModel dm = (DataModel) data;
        dm.data[0] = instrs.size()+1;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        if (m == Mode.DEFAULT) {
            if (node.jjtGetChild(0) instanceof ASTTab) {
                var varIndex = node.jjtGetChild(0).jjtGetChild(1);
                ASTTab nodeTab = (ASTTab) node.jjtGetChild(0);
                ASTIdent i = (ASTIdent) nodeTab.jjtGetChild(0);

                int ne = (int) varIndex.jjtAccept(this, data);
                int ne1 = (int) node.jjtGetChild(1).jjtAccept(this, new DataModel(n+ne, Mode.DEFAULT));

                ASTAInc nAInc = new ASTAInc(JJTAINC);
                ASTJcIdent nJcIdent = new ASTJcIdent(JJTJCIDENT);
                nJcIdent.jjtSetValue(i.jjtGetValue());
                nAInc.jjtAddChild(nJcIdent,0);

                instrs.add(nAInc);

                return ne+ne1+1;
            }

            int ne = (int) node.jjtGetChild(1).jjtAccept(this, data);

            ASTInc nInc = new ASTInc(JJTINC);
            ASTJcIdent nJcIdent = new ASTJcIdent(JJTJCIDENT);
            nJcIdent.jjtSetValue(((ASTIdent) node.jjtGetChild(0)).jjtGetValue());
            nInc.jjtAddChild(nJcIdent,0);

            instrs.add(nInc);

            return ne+1;
        }

        return 0;
    }

    @Override
    public Object visit(ASTIncrement node, Object data) throws VisitorException {
        DataModel dm = (DataModel) data;
        dm.data[0] = instrs.size()+1;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        if (m == Mode.DEFAULT) {
            ASTPush nPush = new ASTPush(JJTPUSH);
            ASTJcNbre nJcNbre = new ASTJcNbre(JJTJCNBRE);
            nJcNbre.jjtSetValue(1);
            nPush.jjtAddChild(nJcNbre,0);

            if (node.jjtGetChild(0) instanceof ASTTab) {
                var varIndex = node.jjtGetChild(0).jjtGetChild(1);
                ASTTab nodeTab = (ASTTab) node.jjtGetChild(0);
                ASTIdent i = (ASTIdent) nodeTab.jjtGetChild(0);

                int ne = (int) varIndex.jjtAccept(this, data);

                ASTAInc nAInc = new ASTAInc(JJTAINC);
                ASTJcIdent nJcIdent = new ASTJcIdent(JJTJCIDENT);
                nJcIdent.jjtSetValue(i.jjtGetValue());
                nAInc.jjtAddChild(nJcIdent,0);

                instrs.add(nPush);
                instrs.add(nAInc);

                return ne+2;
            } else {
                instrs.add(nPush);

                ASTInc nInc = new ASTInc(JJTINC);
                ASTJcIdent nJcIdent = new ASTJcIdent(JJTJCIDENT);
                nJcIdent.jjtSetValue(((ASTIdent) node.jjtGetChild(0)).jjtGetValue());
                nInc.jjtAddChild(nJcIdent,0);

                instrs.add(nInc);

                return 2;
            }
        }

        return 0;
    }

    @Override
    public Object visit(ASTAffectation node, Object data) throws VisitorException {
        DataModel dm = (DataModel) data;
        dm.data[0] = instrs.size()+1;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        if (m == Mode.DEFAULT) {
            if (node.jjtGetChild(0) instanceof ASTTab) {
                var varIndex = node.jjtGetChild(0).jjtGetChild(1); // Get index of element in tab
                ASTTab nTab = (ASTTab) node.jjtGetChild(0); // Get the array node
                ASTIdent i = (ASTIdent) nTab.jjtGetChild(0);

                int ne = (int) varIndex.jjtAccept(this, data);
                int ne1 = (int) node.jjtGetChild(1).jjtAccept(this, new DataModel(n+ne, Mode.DEFAULT));

                ASTAStore nAStore = new ASTAStore(JJTASTORE);
                ASTJcIdent nJcIdent = new ASTJcIdent(JJTJCIDENT);
                nJcIdent.jjtSetValue(i.jjtGetValue());
                nAStore.jjtAddChild(nJcIdent,0);

                instrs.add(nAStore);

                return ne+ne1+1;
            }

            int ne = (int) node.jjtGetChild(1).jjtAccept(this, data);

            ASTStore nStore = new ASTStore(JJTSTORE);
            ASTJcIdent nIdentStore = new ASTJcIdent(JJTJCIDENT);
            nIdentStore.jjtSetValue(((ASTIdent) node.jjtGetChild(0)).jjtGetValue());
            nStore.jjtAddChild(nIdentStore,0);

            instrs.add(nStore);

            return ne+1;
        }

        return 0;
    }

    @Override
    public Object visit(ASTAppelE node, Object data) throws VisitorException {
        DataModel dm = (DataModel) data;
        dm.data[0] = instrs.size()+1;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        if (m == Mode.DEFAULT) {
            int nlexp = (int) node.jjtGetChild(1).jjtAccept(this, data);

            ASTJcIdent nJcIdent = new ASTJcIdent(JJTJCIDENT);
            ASTInvoke nInvoke = new ASTInvoke(JJTINVOKE);

            nJcIdent.jjtSetValue(((ASTIdent) node.jjtGetChild(0)).jjtGetValue());
            nInvoke.jjtAddChild(nJcIdent,0);

            instrs.add(nInvoke);

            int nrlexp = (int) node.jjtGetChild(1).jjtAccept(this, new DataModel(n+nlexp+1, Mode.RETRAIT));

            return nlexp+nrlexp+1;
        }
        return 0;
    }

    @Override
    public Object visit(ASTAppelI node, Object data) throws VisitorException {
        DataModel dm = (DataModel) data;
        dm.data[0] = instrs.size()+1;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        if (m == Mode.DEFAULT) {
            int nlexp = (int) node.jjtGetChild(1).jjtAccept(this, data);

            ASTJcIdent nJcIdent = new ASTJcIdent(JJTJCIDENT);
            ASTInvoke nInvoke = new ASTInvoke(JJTINVOKE);

            nJcIdent.jjtSetValue(((ASTIdent) node.jjtGetChild(0)).jjtGetValue());
            nInvoke.jjtAddChild(nJcIdent,0);

            instrs.add(nInvoke);

            int nrlexp = (int) node.jjtGetChild(1).jjtAccept(this, new DataModel(n+nlexp+1, Mode.RETRAIT));

            ASTPop Npop = new ASTPop(JJTPOP);
            instrs.add(Npop);

            return nlexp+nrlexp+2;
        }
        return 0;
    }

    @Override
    public Object visit(ASTRetour node, Object data) throws VisitorException {
        DataModel dm = (DataModel) data;
        dm.data[0] = instrs.size()+1;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        return (int) node.jjtGetChild(0).jjtAccept(this, data);
    }

    @Override
    public Object visit(ASTEcrire node, Object data) throws VisitorException {
        int ne = (int) node.jjtGetChild(0).jjtAccept(this, data);

        ASTWrite write = new ASTWrite(JJTWRITE);
        instrs.add(write);

        return ne+1;

    }

    @Override
    public Object visit(ASTEcrireLn node, Object data) throws VisitorException {
        int ne = (int) node.jjtGetChild(0).jjtAccept(this, data);

        ASTWriteLn writeln = new ASTWriteLn(JJTWRITELN);
        instrs.add(writeln);

        return ne+1;
    }

    @Override
    public Object visit(ASTSi node, Object data) throws VisitorException {
        DataModel dm = (DataModel) data;

        if (node.jjtGetParent().jjtGetParent() instanceof ASTMethode) {
            dm.data[0] = instrs.size()+1;
        } else {
            dm.data[0] = instrs.size();
        }
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        if (m == Mode.DEFAULT) {
            int ne = (int) node.jjtGetChild(0).jjtAccept(this, new DataModel(n, Mode.DEFAULT));

            ASTIf nIf = new ASTIf(JJTIF);
            instrs.add(nIf);

            int ns1 = (int) node.jjtGetChild(2).jjtAccept(this, new DataModel(n+ne+1, Mode.DEFAULT));

            ASTJcNbre ifNbre = new ASTJcNbre(JJTJCNBRE);
            ifNbre.jjtSetValue(n+ne+ns1+2+1);
            nIf.jjtAddChild(ifNbre,0);

            ASTGoTo nGoTo = new ASTGoTo(JJTGOTO);
            instrs.add(nGoTo);

            int ns = (int) node.jjtGetChild(1).jjtAccept(this, new DataModel(n+ne+ns1+2, Mode.DEFAULT));

            ASTJcNbre addrGoto = new ASTJcNbre(JJTJCNBRE);
            addrGoto.jjtSetValue(n+ne+ns1+ns+2);
            nGoTo.jjtAddChild(addrGoto,0);

            return ne+ns1+ns+2+1;
        }

        return 0;
    }

    @Override
    public Object visit(ASTTantQue node, Object data) throws VisitorException {
        DataModel dm = (DataModel) data;
        dm.data[0] = instrs.size()+1;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        if (m == Mode.DEFAULT) {
            int ne = (int) node.jjtGetChild(0).jjtAccept(this, new DataModel(n, Mode.DEFAULT));

            fr.m1comp5.jjc.generated.ASTNot nNot = new fr.m1comp5.jjc.generated.ASTNot(JJTNOT);
            instrs.add(nNot);

            ASTIf nIf = new ASTIf(JJTIF);
            instrs.add(nIf);


            int niss = (int) node.jjtGetChild(1).jjtAccept(this, new DataModel(n+ne+2, Mode.DEFAULT));

            ASTJcNbre ifNbre = new ASTJcNbre(JJTJCNBRE);
            ifNbre.jjtSetValue(n+ne+niss+3);
            nIf.jjtAddChild(ifNbre,0);

            ASTGoTo nGoTo = new ASTGoTo(JJTGOTO);
            ASTJcNbre addrGoto = new ASTJcNbre(JJTJCNBRE);
            addrGoto.jjtSetValue(n);
            nGoTo.jjtAddChild(addrGoto,0);

            instrs.add(nGoTo);

            return ne+niss+3;
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
    public Object visit(ASTTab node, Object data) throws VisitorException {
        DataModel dm = (DataModel) data;
        dm.data[0] = instrs.size()+1;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        if (m == Mode.DEFAULT) {
            int ne = (int) node.jjtGetChild(1).jjtAccept(this, data);

            ASTALoad nALoad = new ASTALoad(JJTALOAD);
            ASTJcIdent nJcIdent = new ASTJcIdent(JJTJCIDENT);

            nJcIdent.jjtSetValue(((ASTIdent) node.jjtGetChild(0)).jjtGetValue());
            nALoad.jjtAddChild(nJcIdent,0);

            instrs.add(nALoad);

            return ne+1;
        }

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

    @Override
    public Object visit(ASTLongeur node, Object data) {
        ASTLength lengthNode = new ASTLength(JJTLENGTH);
        ASTJcIdent nJcIdent = new ASTJcIdent(JJTJCIDENT);

        nJcIdent.jjtSetValue(((ASTIdent) node.jjtGetChild(0)).jjtGetValue());
        lengthNode.jjtAddChild(nJcIdent,0);

        instrs.add(lengthNode);

        return 1;
    }

    @Override
    public Object visit(ASTVrai node, Object data) {
        ASTPush nPush = new ASTPush(JJTPUSH);
        ASTJcVrai tr = new ASTJcVrai(JJTJCVRAI);

        tr.jjtSetValue("vrai");
        nPush.jjtAddChild(tr, 0);

        instrs.add(nPush);

        return 1;
    }

    @Override
    public Object visit(ASTFaux node, Object data) {
        ASTPush push = new ASTPush(JJTPUSH);
        ASTJcFalse fl = new ASTJcFalse(JJTJCFALSE);

        fl.jjtSetValue("faux");
        push.jjtAddChild(fl, 0);

        instrs.add(push);

        return 1;
    }

    @Override
    public Object visit(ASTExnil node, Object data) {
        return 0;
    }

    @Override
    public Object visit(ASTNot node, Object data) throws VisitorException {
        return compileOp1(node, data);
    }

    @Override
    public Object visit(ASTNeg node, Object data) throws VisitorException {
        return compileOp1(node, data);
    }

    @Override
    public Object visit(ASTEt node, Object data) throws VisitorException {
        return compileOp2(node, data);
    }

    @Override
    public Object visit(ASTOu node, Object data) throws VisitorException {
        return compileOp2(node, data);
    }

    @Override
    public Object visit(ASTEq node, Object data) throws VisitorException {
        return compileOp2(node, data);
    }

    @Override
    public Object visit(ASTSup node, Object data) throws VisitorException {
        return compileOp2(node, data);
    }

    @Override
    public Object visit(ASTAdd node, Object data) throws VisitorException {
        return compileOp2(node, data);
    }

    @Override
    public Object visit(ASTSub node, Object data) throws VisitorException {
        return compileOp2(node, data);
    }

    @Override
    public Object visit(ASTMul node, Object data) throws VisitorException {
        return compileOp2(node, data);
    }

    @Override
    public Object visit(ASTDiv node, Object data) throws VisitorException {
        return compileOp2(node, data);
    }

    @Override
    public Object visit(ASTListExp node, Object data) throws VisitorException {
        DataModel dm = (DataModel) data;
        dm.data[0] = instrs.size()+1;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        if (m == Mode.DEFAULT) {
            int nexp = (int) node.jjtGetChild(0).jjtAccept(this, new DataModel(n, Mode.DEFAULT));
            int nlexp = (int) node.jjtGetChild(1).jjtAccept(this, new DataModel(n+nexp, Mode.DEFAULT));

            return nexp+nlexp;
        }
        if (m == Mode.RETRAIT) {
            ASTSwap nSwap = new ASTSwap(JJTSWAP);
            instrs.add(nSwap);

            ASTPop nPop = new ASTPop(JJTPOP);
            instrs.add(nPop);
            int nrlexp = (int) node.jjtGetChild(1).jjtAccept(this, data);

            return nrlexp+2;
        }

        return 0;
    }

    @Override
    public Object visit(ASTExp node, Object data) throws VisitorException {
        return node.jjtGetChild(0).jjtAccept(this, data);
    }

    @Override
    public Object visit(ASTOmega node, Object data) {
        ASTPush push = new ASTPush(JJTPUSH);
        ASTJcChaine var = new ASTJcChaine(JJTJCCHAINE);

        var.jjtSetValue(ObjectType.OMEGA);
        push.jjtAddChild(var,0);
        instrs.add(push);
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


    private fr.m1comp5.jjc.generated.Node getNodeOp(Node node) {
        fr.m1comp5.jjc.generated.Node opNode = null;

        if (node instanceof ASTAdd) opNode = new fr.m1comp5.jjc.generated.ASTAdd(JJTADD);
        else if (node instanceof ASTSub) opNode =  new fr.m1comp5.jjc.generated.ASTSub(JJTSUB);
        else if (node instanceof ASTMul) opNode =  new fr.m1comp5.jjc.generated.ASTMul(JJTMUL);
        else if (node instanceof ASTDiv) opNode =  new fr.m1comp5.jjc.generated.ASTDiv(JJTDIV);
        else if (node instanceof ASTSup) opNode =  new fr.m1comp5.jjc.generated.ASTSup(JJTSUP);
        else if (node instanceof ASTEq) opNode = new fr.m1comp5.jjc.generated.ASTCmp(JJTCMP);
        else if (node instanceof ASTOu) opNode = new fr.m1comp5.jjc.generated.ASTOr(JJTOR);
        else if (node instanceof ASTEt) opNode = new fr.m1comp5.jjc.generated.ASTAnd(JJTAND);
        else if (node instanceof ASTNot) opNode = new fr.m1comp5.jjc.generated.ASTNot(JJTNOT);
        else if (node instanceof ASTNeg) opNode = new fr.m1comp5.jjc.generated.ASTNeg(JJTNEG);

        return opNode;
    }

    private int compileOp2(Node node, Object data) throws VisitorException {
        DataModel dm = (DataModel) data;
        dm.data[0] = instrs.size()+1;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        if (m == Mode.DEFAULT) {
            int ne1 = (int) node.jjtGetChild(0).jjtAccept(this, new DataModel(n, Mode.DEFAULT));
            int ne2 = (int) node.jjtGetChild(1).jjtAccept(this, new DataModel(n + ne1, Mode.DEFAULT));

            instrs.add(getNodeOp(node));

            return ne1+ne2+1;
        }

        return 0;
    }

    private int compileOp1(Node node, Object data) throws VisitorException {
        DataModel dm = (DataModel) data;
        dm.data[0] = instrs.size()+1;
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

    private int compileDeclsVars(Node node, Object data) throws VisitorException {
        DataModel dm = (DataModel) data;
        dm.data[0] = instrs.size()+1;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        if (m == Mode.DEFAULT) {
            int nds = (int) node.jjtGetChild(0).jjtAccept(this, data);
            int ndss = (int) node.jjtGetChild(1).jjtAccept(this, new DataModel(n+nds, Mode.DEFAULT));

            return nds+ndss;
        }
        if (m == Mode.RETRAIT) {
            int nrds = (int) node.jjtGetChild(1).jjtAccept(this, data);
            int nrdss = (int) node.jjtGetChild(0).jjtAccept(this, new DataModel(n+nrds, Mode.RETRAIT));
            return nrds+nrdss;
        }
        return 0;
    }

    private int compileVarCst(Node node, Object data, String nature) throws VisitorException {
        DataModel dm = (DataModel) data;
        dm.data[0] = instrs.size()+1;
        int n = (Integer) dm.data[0];
        Mode m = (Mode) dm.data[1];

        if (m == Mode.DEFAULT) {
            int ne = (int) node.jjtGetChild(2).jjtAccept(this, data);

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
            varNature.jjtSetValue(nature);

            ASTJcNbre varValue = new ASTJcNbre(JJTJCNBRE);
            varValue.jjtSetValue(0);

            newVar.jjtAddChild(varIdent,0);
            newVar.jjtAddChild(varType,1);
            newVar.jjtAddChild(varNature,2);
            newVar.jjtAddChild(varValue,3);

            instrs.add(newVar);

            return ne+1;
        }
        if (m == Mode.RETRAIT) {
            return retraitDss();
        }
        return 0;
    }
}
