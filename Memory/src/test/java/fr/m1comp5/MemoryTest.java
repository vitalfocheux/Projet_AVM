package fr.m1comp5;

import fr.m1comp5.mjj.generated.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static fr.m1comp5.mjj.generated.MiniJajaTreeConstants.*;

public class MemoryTest {

    Memory mem;

    @BeforeEach
    public void setup() throws HeapException {
        mem = new Memory();
    }

    public ASTMethode createMethode(String id){
        ASTEntier enti = new ASTEntier(JJTENTIER);
        ASTIdent varIdent = new ASTIdent(JJTIDENT);
        varIdent.jjtSetValue(id);

        ASTEntetes entetes = new ASTEntetes(JJTENTETES);
        ASTEntete entete = new ASTEntete(JJTENTETE);
        ASTEnil enil = new ASTEnil(JJTENIL);

        ASTEntier entettype = new ASTEntier(JJTENTIER);
        ASTIdent IdentEntete = new ASTIdent(JJTIDENT);
        IdentEntete.jjtSetValue("x");
        entete.jjtAddChild(entettype, 0);
        entete.jjtAddChild(IdentEntete, 1);

        entetes.jjtAddChild(entete, 0);
        entetes.jjtAddChild(enil, 1);

        ASTVnil var = new ASTVnil(JJTVNIL);
        ASTRetour retour = new ASTRetour(JJTRETOUR);
        ASTNbre retourNbre = new ASTNbre(JJTNBRE);

        retourNbre.jjtSetValue(0);
        retour.jjtAddChild(retourNbre, 0);
        ASTMethode methode = new ASTMethode(JJTMETHODE);
        methode.jjtAddChild(enti, 0);
        methode.jjtAddChild(varIdent, 1);
        methode.jjtAddChild(entetes, 2);
        methode.jjtAddChild(var, 3);
        methode.jjtAddChild(retour, 4);
        return methode;
    }

    @Test
    public void declVar() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        ASTVar x = new ASTVar(0);
        x.jjtSetValue(1);
        mem.declVar("x", x, ObjectType.INT);
        Assertions.assertEquals(((ASTVar) mem.getVal("x")).jjtGetValue(), 1);
        Assertions.assertEquals(mem.getNature("x"), ObjectNature.VAR);
        Assertions.assertEquals(mem.getStack().get(0), new MemoryObject("x", 1, ObjectNature.VAR, ObjectType.INT));
        Assertions.assertEquals(mem.getType("x"), ObjectType.INT);
    }

    @Test
    public void declVarNotInitialisedWithNull() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        mem.declVar("x", null, ObjectType.OMEGA);
        Assertions.assertThrows(SymbolTableException.class, () -> {
            Assertions.assertNull(mem.getVal("x"));
        });
    }

    @Test
    public void declVarNotInitialisedWithOmega() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        mem.declVar("x", ObjectType.OMEGA, ObjectType.OMEGA);
        Assertions.assertThrows(SymbolTableException.class, () -> {
           Assertions.assertEquals(mem.getVal("x"), ObjectType.OMEGA);
        });
    }

    @Test
    public void getValDoesntExist(){
        mem.getSymbolTable().newScope();
        Assertions.assertThrows(SymbolTableException.class, () -> {
            ASTVar x = new ASTVar(0);
            x.jjtSetValue(1);
           Assertions.assertEquals(mem.getVal("x"), x);
        });
    }

    @Test
    public void assignValueVar() throws SymbolTableException, StackException, HeapException {
        mem.getSymbolTable().newScope();
        ASTVar x = new ASTVar(0);
        x.jjtSetValue(1);
        mem.declVar("x", x, ObjectType.INT);
        x.jjtSetValue(2);
        mem.assignValue("x", x);
        Assertions.assertEquals(((ASTVar) mem.getVal("x")).jjtGetValue(), 2);
    }

    @Test
    public void assignValueNoneVarWasDecl(){
        mem.getSymbolTable().newScope();
        ASTVar x = new ASTVar(0);
        x.jjtSetValue(2);
        Assertions.assertThrows(StackException.class, () -> {
            mem.assignValue("x", x);
        });
    }

    @Test
    public void assignValueDoesntExist() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        ASTVar y = new ASTVar(0);
        y.jjtSetValue(1);
        mem.declVar("y", y, ObjectType.INT);
        ASTVar x = new ASTVar(0);
        x.jjtSetValue(2);
        Assertions.assertThrows(SymbolTableException.class, () -> {
            mem.assignValue("x", x);
        });
    }

    @Test
    public void assignValueNotInitialised() throws SymbolTableException, StackException, HeapException {
        mem.getSymbolTable().newScope();
        mem.declVar("x", null, ObjectType.OMEGA);
        ASTVar x = new ASTVar(0);
        x.jjtSetValue(2);
        mem.assignValue("x", x);
    }

    @Test
    public void assignValueWithNull() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        mem.declVar("x", null, ObjectType.OMEGA);
        Assertions.assertThrows(RuntimeException.class, () -> {
            mem.assignValue("x", null);
        });
    }

    /*
    @Test
    public void assignValueAnIntToBool() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        ASTVar x = new ASTVar(0);
        x.jjtSetValue(1);
        mem.declVar("x", x, ObjectType.INT);
        Assertions.assertThrows(RuntimeException.class, () -> {
            mem.assignValue("x", true);
        });
    }
    */


    @Test
    public void getValCst() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        ASTCst x = new ASTCst(0);
        ASTNbre nbre = new ASTNbre(0);
        nbre.jjtSetValue(1);
        x.jjtAddChild(nbre, 0);
        mem.declCst("x", x, ObjectType.INT);
        ASTCst get = (ASTCst) mem.getVal("x");
        Assertions.assertEquals(((ASTNbre) get.jjtGetChild(0)).jjtGetValue(), 1);
    }

    @Test
    public void assignCst() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        ASTCst x = new ASTCst(0);
        ASTNbre nbre = new ASTNbre(0);
        nbre.jjtSetValue(1);
        x.jjtAddChild(nbre, 0);
        mem.declCst("x", x, ObjectType.INT);
        ASTCst x2 = new ASTCst(0);
        ASTNbre nbre2 = new ASTNbre(0);
        nbre.jjtSetValue(2);
        x2.jjtAddChild(nbre2, 0);
        Assertions.assertThrows(RuntimeException.class, () -> {
            mem.assignValue("x", x2);
        });
    }

    @Test
    public void assignCstDoesntExist() {
        mem.getSymbolTable().newScope();
        Assertions.assertThrows(StackException.class, () -> {
            mem.assignValue("x", 2);
        });
    }

    @Test
    public void assignCstNotInitialised() throws SymbolTableException, StackException, HeapException {
        mem.getSymbolTable().newScope();
        ASTCst x = new ASTCst(0);
        ASTNbre nbre = new ASTNbre(0);
        nbre.jjtSetValue(2);
        x.jjtAddChild(nbre, 0);
        mem.declCst("x", null, ObjectType.OMEGA);
        mem.assignValue("x", x);
        ASTCst get = (ASTCst) mem.getVal("x");
        Assertions.assertEquals(((ASTNbre) get.jjtGetChild(0)).jjtGetValue(), 2);
        Assertions.assertEquals(mem.getType("x"), ObjectType.OMEGA);
    }

    @Test
    public void assignCstNotInitialisedOmega() throws SymbolTableException, StackException, HeapException {
        mem.getSymbolTable().newScope();
        ASTCst x = new ASTCst(0);
        ASTNbre nbre = new ASTNbre(0);
        nbre.jjtSetValue(2);
        x.jjtAddChild(nbre, 0);
        mem.declCst("x", ObjectType.OMEGA, ObjectType.OMEGA);
        mem.assignValue("x", x);
        ASTCst get = (ASTCst) mem.getVal("x");
        Assertions.assertEquals(((ASTNbre) get.jjtGetChild(0)).jjtGetValue(), 2);
        Assertions.assertEquals(mem.getType("x"), ObjectType.OMEGA);
    }

    @Test
    public void assignCstWithNull() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        ASTCst x = new ASTCst(0);
        ASTNbre nbre = new ASTNbre(0);
        nbre.jjtSetValue(1);
        x.jjtAddChild(nbre, 0);
        mem.declCst("x", x, ObjectType.INT);
        Assertions.assertThrows(RuntimeException.class, () -> {
            mem.assignValue("x", null);
        });
    }

    @Test
    public void assignMeth() throws SymbolTableException, StackException, HeapException {
        mem.getSymbolTable().newScope();
        ASTMethode fct = createMethode("zero");
        mem.declMeth("zero", fct, ObjectType.INT);
        Assertions.assertEquals(mem.getType("zero"), ObjectType.INT);
        Assertions.assertEquals(mem.getNature("zero"), ObjectNature.METH);
        ASTMethode val = (ASTMethode) mem.getVal("zero");
        Assertions.assertInstanceOf(ASTEntier.class, val.jjtGetChild(0));
        Assertions.assertInstanceOf(ASTIdent.class, val.jjtGetChild(1));
        Assertions.assertEquals(((ASTIdent) val.jjtGetChild(1)).jjtGetValue(), "zero");
        Assertions.assertInstanceOf(ASTEntier.class, val.jjtGetChild(2).jjtGetChild(0).jjtGetChild(0));
        Assertions.assertInstanceOf(ASTIdent.class, val.jjtGetChild(2).jjtGetChild(0).jjtGetChild(1));
        Assertions.assertEquals(((ASTIdent) val.jjtGetChild(2).jjtGetChild(0).jjtGetChild(1)).jjtGetValue(), "x");
        Assertions.assertInstanceOf(ASTEnil.class, val.jjtGetChild(2).jjtGetChild(1));
        Assertions.assertInstanceOf(ASTVnil.class, val.jjtGetChild(3));
        Assertions.assertInstanceOf(ASTNbre.class, val.jjtGetChild(4).jjtGetChild(0));
        Assertions.assertEquals(((ASTNbre) val.jjtGetChild(4).jjtGetChild(0)).jjtGetValue(), 0);

        ASTMethode nw = createMethode("zero2");

        MemoryObject mo = mem.assignValue("zero", nw);
        Assertions.assertEquals(mo.getId(), "zero");
        Assertions.assertEquals(mo.getNature(), ObjectNature.METH);
        Assertions.assertEquals(mo.getType(), ObjectType.INT);
        ASTMethode mmo = (ASTMethode) mo.getValue();
        Assertions.assertInstanceOf(ASTEntier.class, mmo.jjtGetChild(0));
        Assertions.assertInstanceOf(ASTIdent.class, mmo.jjtGetChild(1));
        Assertions.assertEquals(((ASTIdent) mmo.jjtGetChild(1)).jjtGetValue(), "zero");
        Assertions.assertInstanceOf(ASTEntier.class, mmo.jjtGetChild(2).jjtGetChild(0).jjtGetChild(0));
        Assertions.assertInstanceOf(ASTIdent.class, mmo.jjtGetChild(2).jjtGetChild(0).jjtGetChild(1));
        Assertions.assertEquals(((ASTIdent) mmo.jjtGetChild(2).jjtGetChild(0).jjtGetChild(1)).jjtGetValue(), "x");
        Assertions.assertInstanceOf(ASTEnil.class, mmo.jjtGetChild(2).jjtGetChild(1));
        Assertions.assertInstanceOf(ASTVnil.class, mmo.jjtGetChild(3));
        Assertions.assertInstanceOf(ASTNbre.class, mmo.jjtGetChild(4).jjtGetChild(0));
        Assertions.assertEquals(((ASTNbre) mmo.jjtGetChild(4).jjtGetChild(0)).jjtGetValue(), 0);
    }

    @Test
    public void assignMethodDoesntExist() {
        mem.getSymbolTable().newScope();
        Assertions.assertThrows(StackException.class, () -> {
            mem.assignValue("main", "methmain2");
        });
    }

    /*
    @Test
    public void assignMethodNotInitialised() throws SymbolTableException, StackException, HeapException {
        mem.getSymbolTable().newScope();
        mem.declMeth("main", null, ObjectType.VOID);
        Assertions.assertThrows(RuntimeException.class, () -> {
            mem.assignValue("main", "methmain");
            Assertions.assertEquals(mem.getVal("main"), "methmain");
        });
    }
    */

    @Test
    public void assignMethodWithNull() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        ASTMethode fct = createMethode("zero");
        mem.declMeth("fct", fct, ObjectType.INT);
        Assertions.assertThrows(RuntimeException.class, () -> {
            mem.assignValue("fct", null);
        });
    }

    @Test
    public void declTab() throws SymbolTableException, HeapException, StackException {
        mem.getSymbolTable().newScope();
        mem.declTab("tab", 5, ObjectType.INT);
        Assertions.assertEquals(mem.getVal("tab"), 0);
        Assertions.assertEquals(mem.getNature("tab"), ObjectNature.TAB);
        Assertions.assertEquals(mem.getType("tab"), ObjectType.INT);
    }

    @Test
    public void declTabWithNotInteger() throws SymbolTableException, HeapException, StackException {
        mem.getSymbolTable().newScope();
        Assertions.assertThrows(java.lang.ClassCastException.class, () -> {
            mem.declTab("tab", "notInteger", ObjectType.INT);
        });
    }

    @Test
    public void assignTab() throws SymbolTableException, HeapException, StackException {
        mem.getSymbolTable().newScope();
        mem.declTab("tab", 5, ObjectType.INT);
        mem.assignValue("tab", 0);
        Map<Integer, List<HeapBlock>> blocks = mem.getHeap().getBlocks();
        List<HeapBlock> block = blocks.get(8);
        Assertions.assertEquals(block.get(0).getAddress(), 0);
        Assertions.assertEquals(block.get(0).getSize(), 8);
        Assertions.assertFalse(block.get(0).isFree());
        Assertions.assertEquals(block.get(1).getAddress(), 8);
        Assertions.assertEquals(block.get(1).getSize(), 8);
        Assertions.assertTrue(block.get(1).isFree());
    }

    //TODO: revoir pour que ça lève la bonne exception surement avec Mock
    @Test
    public void assignTabWithNotInteger() throws SymbolTableException, HeapException, StackException {
        mem.getSymbolTable().newScope();
        mem.declTab("tab", 5, ObjectType.INT);
        Assertions.assertThrows(RuntimeException.class, () -> {
            mem.assignValue("tab", "notInteger");
        });
    }

    @Test
    public void removeDeclDoesntExist(){
        mem.getSymbolTable().newScope();
        Assertions.assertThrows(SymbolTableException.class, () -> {
            mem.removeDecl("x");
        });
    }

    @Test
    public void removeDecl() throws SymbolTableException, StackException, HeapException {
        mem.getSymbolTable().newScope();
        ASTVar x = new ASTVar(0);
        x.jjtSetValue(1);
        mem.declVar("x", x, ObjectType.INT);
        Assertions.assertEquals(((ASTVar) mem.getVal("x")).jjtGetValue(), 1);
        Assertions.assertEquals(mem.getNature("x"), ObjectNature.VAR);
        Assertions.assertEquals(mem.getStack().get(0), new MemoryObject("x", 1, ObjectNature.VAR, ObjectType.INT));
        Assertions.assertEquals(mem.getType("x"), ObjectType.INT);
        mem.removeDecl("x");
        Assertions.assertThrows(SymbolTableException.class, () -> {
            mem.getVal("x");
        });
        Assertions.assertTrue(mem.getStack().empty());
    }

    @Test
    public void removeDeclTab() throws SymbolTableException, HeapException, StackException {
        mem.getSymbolTable().newScope();
        mem.declTab("tab", 5, ObjectType.INT);
        Map<Integer, List<HeapBlock>> blocks = mem.getHeap().getBlocks();
        List<HeapBlock> block = blocks.get(8);
        Assertions.assertEquals(block.get(0).getAddress(), 0);
        Assertions.assertEquals(block.get(0).getSize(), 8);
        Assertions.assertFalse(block.get(0).isFree());
        Assertions.assertEquals(block.get(1).getAddress(), 8);
        Assertions.assertEquals(block.get(1).getSize(), 8);
        Assertions.assertTrue(block.get(1).isFree());
        mem.removeDecl("tab");
        blocks = mem.getHeap().getBlocks();
        Assertions.assertTrue(blocks.get(8).isEmpty());
        Assertions.assertTrue(blocks.get(16).isEmpty());
        Assertions.assertTrue(blocks.get(32).isEmpty());
        Assertions.assertTrue(blocks.get(64).isEmpty());
        Assertions.assertTrue(blocks.get(128).isEmpty());
        Assertions.assertTrue(blocks.get(256).isEmpty());
        Assertions.assertTrue(blocks.get(512).isEmpty());
        Assertions.assertTrue(blocks.get(1024).get(0).isFree());
        Assertions.assertEquals(blocks.get(1024).get(0).getAddress(), 0);
    }

    @Test
    public void assignValueArrayNotTab() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        ASTVar x = new ASTVar(0);
        x.jjtSetValue(1);
        mem.declVar("x", x, ObjectType.INT);
        Assertions.assertThrows(RuntimeException.class, () -> {
            mem.assignValueArray("x", 0, 1);
        });
    }

    @Test
    public void assignValueArrayWithNotIntegerForIndex() throws SymbolTableException, StackException, HeapException {
        mem.getSymbolTable().newScope();
        mem.declTab("x", 5, ObjectType.INT);
        Assertions.assertThrows(RuntimeException.class, () -> {
            mem.assignValueArray("x", "1", 1);
        });
    }

    @Test
    public void assignValueArray() throws SymbolTableException, HeapException, StackException {
        mem.getSymbolTable().newScope();
        int size = 5;
        mem.declTab("x", size, ObjectType.INT);
        for(int i = 0; i < size; ++i){
            mem.assignValueArray("x", i, i);
            Assertions.assertEquals(mem.getHeap().accessValue(0, i), i);
        }
    }

    @Test
    public void assignTypeWithIDDoesntExistAndScopeEmpty(){
        mem.getSymbolTable().newScope();
        Assertions.assertThrows(StackException.class, () -> {
            mem.assignType("x", ObjectType.INT);
        });
    }

    @Test
    public void assignTypeWithIDDoesntExist() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        ASTVar x = new ASTVar(0);
        x.jjtSetValue(1);
        mem.declVar("x", x, ObjectType.INT);
        mem.assignType("y", ObjectType.INT);
        Assertions.assertThrows(SymbolTableException.class, () -> {
            Assertions.assertEquals(mem.getType("y"), ObjectType.INT);
        });
        Assertions.assertEquals(mem.getStack().size(), 1);
    }

    @Test
    public void assignType() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        ASTVar x = new ASTVar(0);
        x.jjtSetValue(1);
        mem.declVar("x", x, ObjectType.INT);
        mem.assignType("x", ObjectType.BOOLEAN);
        Assertions.assertEquals(mem.getType("x"), ObjectType.BOOLEAN);
    }

    @Test
    public void classVariable() throws StackException, SymbolTableException {
        mem.getSymbolTable().newScope();
        ASTVar x = new ASTVar(0);
        x.jjtSetValue(1);
        mem.declVar("x", x, ObjectType.INT);
        Assertions.assertEquals(mem.classVariable(), "x");
    }

    @Test
    public void classVariableEmpty() {
        mem.getSymbolTable().newScope();
        Assertions.assertThrows(StackException.class, () -> {
            mem.classVariable();
        });
    }

    @Test
    public void getParamsVar() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        ASTVar x = new ASTVar(0);
        x.jjtSetValue(1);
        mem.declVar("x", x, ObjectType.INT);
        Assertions.assertNull(mem.getParams("x"));
    }

    @Test
    public void getParams() throws SymbolTableException, StackException, HeapException {
        mem.getSymbolTable().newScope();

        ASTEntier enti = new ASTEntier(0);
        ASTIdent varIdent = new ASTIdent(1);
        varIdent.jjtSetValue("zero");

        ASTEntetes entetes = new ASTEntetes(3);
        ASTEntete entete = new ASTEntete(0);
        ASTEnil enil = new ASTEnil(1);

        ASTEntier entettype = new ASTEntier(0);
        ASTIdent IdentEntete = new ASTIdent(1);
        IdentEntete.jjtSetValue("x");
        entete.jjtAddChild(entettype, 0);
        entete.jjtAddChild(IdentEntete, 1);

        entetes.jjtAddChild(entete, 0);
        entetes.jjtAddChild(enil, 1);

        ASTVnil var = new ASTVnil(4);
        ASTRetour retour = new ASTRetour(5);
        ASTNbre retourNbre = new ASTNbre(0);

        retourNbre.jjtSetValue(0);
        retour.jjtAddChild(retourNbre, 0);
        ASTMethode methode = new ASTMethode(0);
        methode.jjtAddChild(enti, 0);
        methode.jjtAddChild(varIdent, 1);
        methode.jjtAddChild(entetes, 2);
        methode.jjtAddChild(var, 3);
        methode.jjtAddChild(retour, 4);

        mem.declMeth("zero", methode, ObjectType.INT);

        Node params = mem.getParams("zero");
        Assertions.assertEquals(params.jjtGetNumChildren(), 2);
        Assertions.assertInstanceOf(ASTEntete.class, params.jjtGetChild(0));
        Assertions.assertEquals(params.jjtGetChild(0).jjtGetNumChildren(), 2);
        Assertions.assertInstanceOf(ASTEntier.class, params.jjtGetChild(0).jjtGetChild(0));
        Assertions.assertEquals(params.jjtGetChild(0).jjtGetChild(0).jjtGetNumChildren(), 0);
        Assertions.assertInstanceOf(ASTIdent.class, params.jjtGetChild(0).jjtGetChild(1));
        Assertions.assertEquals(params.jjtGetChild(0).jjtGetChild(1).jjtGetNumChildren(), 0);
        Assertions.assertEquals(((ASTIdent) params.jjtGetChild(0).jjtGetChild(1)).jjtGetValue(), "x");
        Assertions.assertInstanceOf(ASTEnil.class, params.jjtGetChild(1));
        Assertions.assertEquals(params.jjtGetChild(1).jjtGetNumChildren(), 0);
    }

    @Test
    public void declVarsFromListOfParamsEmpty() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        mem.declVarsFromListOfParams(new ArrayList<>());
        Assertions.assertEquals(mem.getStack().size(), 0);
    }

    @Test
    public void declVarsFromListOfParamsNull() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        mem.declVarsFromListOfParams(null);
        Assertions.assertEquals(mem.getStack().size(), 0);
    }

    @Test
    public void declVarsFromListOfParams() throws SymbolTableException, StackException {
        mem.getSymbolTable().newScope();
        List<MemoryObject> fctParams = new ArrayList<>();
        MemoryObject mo = new MemoryObject("x", 1, ObjectNature.VAR, ObjectType.INT);
        MemoryObject mo2 = new MemoryObject("y", 2, ObjectNature.VAR, ObjectType.INT);
        fctParams.add(mo);
        fctParams.add(mo2);
        mem.declVarsFromListOfParams(fctParams);
        Assertions.assertEquals(mem.getStack().size(), 2);
        Assertions.assertEquals(mem.getStack().get(0), mo);
        Assertions.assertEquals(mem.getStack().get(1), mo2);
    }
}
