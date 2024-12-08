package fr.m1comp5.mjj;

import fr.m1comp5.*;
import fr.m1comp5.mjj.generated.*;

public class VisitorMjj implements MiniJajaVisitor {
    private String toDisplay;
    private Memory memory;

    public VisitorMjj() {
        try
        {
            this.memory = new Memory();
        }
        catch (HeapException he)
        {
            throw new RuntimeException("Can't interpret without the memory");
        }
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
        String id = (String) ((ASTIdent) node.jjtGetChild(0)).jjtGetValue();
        try {
            memory.getSymbolTable().newScope();
            memory.declVar(id, null, ObjectType.OMEGA);
            node.jjtGetChild(1).jjtAccept(this, MjjInterpreterMode.DEFAULT); //Visit vars or vnil
            node.jjtGetChild(2).jjtAccept(this, MjjInterpreterMode.DEFAULT);
            node.jjtGetChild(1).jjtAccept(this, MjjInterpreterMode.REMOVE);
            memory.removeDecl(id);
            memory.getSymbolTable().popScope();
        } catch (Exception e) {
            System.out.println("Exception is : "+ e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Object visit(ASTIdent node, Object data) {
        try {
            if (memory.getSymbolTable().get((String) node.jjtGetValue()).getType() == ObjectType.OMEGA) {
                throw new Exception();
            }
            System.out.println("ASTident -> "+(String)node.jjtGetValue() + " = " + memory.getSymbolTable().get((String) node.jjtGetValue()).getValue());
            return memory.getSymbolTable().get((String) node.jjtGetValue()).getValue();
        } catch (Exception e) {
            System.out.println("Exception is : "+ e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    public Object visit(ASTDecls node, Object data) {
        RDeclsAndVars(node, data);
        return null;
    }

    @Override
    public Object visit(ASTMethode node, Object data) {
        ObjectType type = (ObjectType) node.jjtGetChild(0).jjtAccept(this, data);
        String id = (String) ((ASTIdent) node.jjtGetChild(1)).jjtGetValue();
        try
        {
            if (data == MjjInterpreterMode.DEFAULT)
            {
                memory.declMeth(id, node, type);
            }
            else
            {
                memory.removeDecl(id);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public Object visit(ASTVars node, Object data) {
        RDeclsAndVars(node, data);
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

        try
        {
            if (data == MjjInterpreterMode.DEFAULT)
            {
                memory.declCst(varIdent, value, varType);
            }
            else
            {
                memory.removeDecl(varIdent);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
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
        if (node.jjtGetNumChildren() > 2 && data == MjjInterpreterMode.DEFAULT) {
            value = node.jjtGetChild(2).jjtAccept(this, data); // Retrieve the initialized value
        }
        try
        {
            if (data == MjjInterpreterMode.DEFAULT)
            {
                memory.declVar(varIdent, value, varType);
            }
            else
            {
                memory.removeDecl(varIdent);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public Object visit(ASTTableau node, Object data) {
        ObjectType type = (ObjectType) node.jjtGetChild(0).jjtAccept(this, data);
        String id = (String) ((ASTIdent) node.jjtGetChild(1)).jjtGetValue();
        int arraySize = (int) node.jjtGetChild(2).jjtAccept(this, data);
        try
        {
            if (data == MjjInterpreterMode.DEFAULT)
            {
                memory.declTab(id, arraySize, type);
            }
            else
            {
                memory.removeDecl(id);
            }
        } catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public Object visit(ASTOmega node, Object data) {
        return "OMEGA";
    }

    @Override
    public Object visit(ASTMain node, Object data) {
        memory.getSymbolTable().newScope();
        node.jjtGetChild(0).jjtAccept(this, MjjInterpreterMode.DEFAULT);
        node.jjtGetChild(1).jjtAccept(this, MjjInterpreterMode.DEFAULT);
        node.jjtGetChild(0).jjtAccept(this, MjjInterpreterMode.REMOVE);
        try
        {
            memory.getSymbolTable().popScope();
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }

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
        Object val = node.jjtGetChild(0).jjtAccept(this, data);
        try
        {
            memory.assignValue(memory.classVariable(), val);
            System.out.println("Return value is " + memory.getVal(memory.classVariable()));
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public Object visit(ASTEcrire node, Object data) {
        Object value = node.jjtGetChild(0).jjtAccept(this, data);
        try {
            if (value instanceof String) {
                toDisplay += ((String) value).replace("\"","");
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
        try {
            while ((boolean) node.jjtGetChild(0).jjtAccept(this, data))
            {
                node.jjtGetChild(1).jjtAccept(this, data);
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
            Object val = node.jjtGetChild(1).jjtAccept(this, data);
            if (node.jjtGetChild(0) instanceof ASTTab tab)
            {
                String id = (String) ((ASTIdent) tab.jjtGetChild(0)).jjtGetValue();
                int idx = (int) tab.jjtGetChild(1).jjtAccept(this, data);
                if (idx < 0 || idx >= memory.getHeap().getArraySize((int) memory.getSymbolTable().get(id).getValue()))
                {
                    throw new ArrayIndexOutOfBoundsException();
                }
                memory.assignValueArray(id, idx, val);
            }
            else
            {
                String id = (String) ((ASTIdent) node.jjtGetChild(0)).jjtGetValue();
                memory.assignValue(id, val);
            }
        } catch (Exception e) {
            System.out.println("Exception is : "+ e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Object visit(ASTSomme node, Object data) {
        try {
            int val = (int) node.jjtGetChild(1).jjtAccept(this, data);
            if (node.jjtGetChild(0) instanceof ASTTab tab)
            {
                String id = (String) ((ASTIdent) tab.jjtGetChild(0)).jjtGetValue();
                int idx = (int) tab.jjtGetChild(1).jjtAccept(this, data);
                Object idxVal = node.jjtGetChild(0).jjtAccept(this, data);
                memory.assignValueArray(id, idx, (int) idxVal + val);
            }
            else
            {
                String id = (String) ((ASTIdent) node.jjtGetChild(0)).jjtGetValue();
                memory.assignValue(id, (int) memory.getSymbolTable().get(id).getValue() + val);
            }
        } catch (Exception e) {
            System.out.println("Exception is : "+ e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Object visit(ASTIncrement node, Object data) {
        try
        {
            if (node.jjtGetChild(0) instanceof ASTTab tab)
            {
                String id = (String) ((ASTIdent) tab.jjtGetChild(0)).jjtGetValue();
                int idx = (int) tab.jjtGetChild(1).jjtAccept(this, data);
                Integer idxVal = (Integer) node.jjtGetChild(0).jjtAccept(this, data);
                memory.assignValueArray(id, idx, ++idxVal);
            }
            else
            {
                String id = (String) ((ASTIdent) node.jjtGetChild(0)).jjtGetValue();
                Integer val = (Integer) node.jjtGetChild(0).jjtAccept(this, data);
                memory.assignValue(id, ++val);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public Object visit(ASTAppelI node, Object data) {
        String funcID = (String) ((ASTIdent) node.jjtGetChild(0)).jjtGetValue();
        Node lexp = node.jjtGetChild(1);
        try
        {
            memory.getSymbolTable().newScope();
            Node params = memory.getParams(funcID);
            if (lexp instanceof ASTListExp liExo && params instanceof ASTEntetes entetes)
            {
                expParam(liExo, entetes);
            }
            memory.getDecls(funcID).jjtAccept(this, MjjInterpreterMode.DEFAULT);
            memory.getBody(funcID).jjtAccept(this, MjjInterpreterMode.DEFAULT);
            memory.getDecls(funcID).jjtAccept(this, MjjInterpreterMode.REMOVE);
            memory.removeParams(funcID);
            memory.getSymbolTable().popScope();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public Object visit(ASTListExp node, Object data) {
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
        String id = (String) node.jjtGetChild(0).jjtAccept(this, data);
        try
        {
            MemoryObject mo = memory.getSymbolTable().get(id);
            if (mo.getNature() != ObjectNature.TAB)
            {
                throw new RuntimeException("The identifier is not the identifier of an array");
            }
            return memory.getHeap().getArraySize((int) mo.getValue());
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
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
        try
        {
            ASTAppelI appelI = new ASTAppelI(MiniJajaTreeConstants.JJTAPPELI);
            appelI.jjtAddChild(node.jjtGetChild(0),0);
            appelI.jjtAddChild(node.jjtGetChild(1),1);
            appelI.jjtAccept(this,data);
            return memory.getVal(memory.classVariable());
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Object visit(ASTTab node, Object data) {
        String id = (String) node.jjtGetChild(0).jjtAccept(this, data);
        int idx = (int) node.jjtGetChild(1).jjtAccept(this, data);
        try
        {
            MemoryObject obj = memory.getSymbolTable().get(id);
            if (idx < 0 || idx >= memory.getHeap().getArraySize((int) memory.getSymbolTable().get(id).getValue()))
            {
                throw new ArrayIndexOutOfBoundsException();
            }
            return memory.getHeap().accessValue((int) obj.getValue(), idx);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
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

    private void expParam(ASTListExp lexp, ASTEntetes ent) throws SymbolTableException, StackException
    {
        if (lexp == null && ent == null)
        {
            return;
        }
        Object currEntetes = ent;
        Object currListExp = lexp;
        while(!(currEntetes instanceof ASTEnil) && !(currListExp instanceof ASTExnil))
        {
            ASTEntete entete = (ASTEntete) ((ASTEntetes) currEntetes).jjtGetChild(0);
            String id = (String) ((ASTIdent) entete.jjtGetChild(1)).jjtGetValue();
            ObjectType type = (ObjectType) entete.jjtGetChild(0).jjtAccept(this, MjjInterpreterMode.DEFAULT);
            Object value = ((ASTListExp) currListExp).jjtGetChild(0).jjtAccept(this, MjjInterpreterMode.DEFAULT);
            memory.declVar(id, value, type);
            currEntetes = ((ASTEntetes) currEntetes).jjtGetChild(1);
            currListExp = ((ASTListExp) currListExp).jjtGetChild(1);
        }
    }

    private void RDeclsAndVars(Node node, Object data)
    {
        if (data == MjjInterpreterMode.DEFAULT)
        {
            for (int i = 0; i < node.jjtGetNumChildren(); ++i)
            {
                node.jjtGetChild(i).jjtAccept(this, data);
            }
        }
        else if (data == MjjInterpreterMode.REMOVE)
        {
            for (int i = node.jjtGetNumChildren() - 1; i >= 0; --i)
            {
                node.jjtGetChild(i).jjtAccept(this, data);
            }
        }
    }
}
