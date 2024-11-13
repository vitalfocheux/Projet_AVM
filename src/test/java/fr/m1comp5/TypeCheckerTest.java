package fr.m1comp5;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import fr.m1comp5.Memory.MemoryObject;
import fr.m1comp5.Memory.ObjectNature;
import fr.m1comp5.Memory.ObjectType;
import fr.m1comp5.Typechecker.TypeCheckException;
import fr.m1comp5.Typechecker.TypeChecker;
import fr.m1comp5.Analyzer.mjj.generated.*;

public class TypeCheckerTest {

    private TypeChecker typeChecker;

    @BeforeEach
    public void setUp() {
        typeChecker = new TypeChecker();
    } 

    @Test
    public void testidentCorrect(){
        ASTident ident = new ASTident(0);
        ident.jjtSetValue("Xml12");
        try {
            typeChecker.visit(ident, null);
        } catch (TypeCheckException e) {
            fail("Exception thrown during ident check: " + e.getMessage());
        }
    }
    @Test
    public void testidentIncorrect(){
        ASTident ident = new ASTident(0);
        ident.jjtSetValue("12Xml");
        try {
            typeChecker.visit(ident, null);
            fail("Expected exception for incorrect ident");
        } catch (TypeCheckException e) {
            assertTrue(e.getMessage().contains("Invalid identifier"));
        }
    }

    @Test 
    public void testTableau(){
        ASTentier int1 = new  ASTentier(0);
        ASTident tabl = new ASTident(0);
        tabl.jjtSetValue("tab");  
        ASTvnil nill = new ASTvnil(0);
        ASTtableau tab = new ASTtableau(0);
        tab.jjtAddChild(int1, 0);
        tab.jjtAddChild(tabl,1); 
        tab.jjtAddChild(nill, 2);
        try {
            typeChecker.visit(tab,null ); 
        } catch (TypeCheckException e) {
            fail("Exception thrown during Tableau declaration: " + e.getMessage());
        }

    }
    @Test 
    public void testLong(){
        ASTident longueur = new ASTident(0);
        longueur.jjtSetValue("exemple");
        ASTlongeur longueurNode = new ASTlongeur(0);
        longueurNode.jjtAddChild(longueur, 0);
        try {
            typeChecker.visit(longueurNode,null ); 
        } catch (TypeCheckException e) {
            fail("Exception thrown during Longueur declaration: " + e.getMessage());
        }

    }

    @Test
    public void testVariableDeclaration() {
        ASTentier entier = new ASTentier(0);
        ASTident varIdent = new ASTident(1);
        varIdent.jjtSetValue("myVar");
        ASTomega omega = new ASTomega(2);
        ASTvar varNode = new ASTvar(0);
        varNode.jjtAddChild(entier, 0);
        varNode.jjtAddChild(varIdent, 1);
        varNode.jjtAddChild(omega, 2);
        try {
            MemoryObject t = (MemoryObject) typeChecker.visit(varNode, null);
            assertEquals(t.getId(),"myVar");
            assertEquals(t.getNature(),ObjectNature.VAR);
            assertEquals(t.getType(),ObjectType.INT);
            assertEquals(t.getValue(),null);
        } catch (TypeCheckException e) {
            fail("Exception thrown during variable declaration: " + e.getMessage());
        }
    }
    @Test
    public void testClassDeclaration() {
        ASTident classIdent = new ASTident(0);
        classIdent.jjtSetValue("MyClass");
        ASTdecls decls = new ASTdecls(1);
        ASTmain main = new ASTmain(2); 
        ASTclasse classNode = new ASTclasse(0);
        classNode.jjtAddChild(classIdent, 0);
        classNode.jjtAddChild(decls, 1);
        classNode.jjtAddChild(main, 2);
        Object t= typeChecker.visit(classNode, null);
        assertEquals(t, ObjectType.VOID);


        
    }
 
    @Test
    public void testDuplicateClassDeclaration() {
            ASTident classIdent = new ASTident(0);
            classIdent.jjtSetValue("Pop");
            ASTdecls decls = new ASTdecls(1);
            ASTmain main = new ASTmain(2); 
            ASTclasse classNode = new ASTclasse(0);
            classNode.jjtAddChild(classIdent, 0);
            classNode.jjtAddChild(decls, 1);
            classNode.jjtAddChild(main, 2);
            Object t= typeChecker.visit(classNode, null);
            assertEquals(t, ObjectType.VOID);
        try {
            typeChecker.visit(classNode, null);
            typeChecker.visit(classNode, null);
        } catch (TypeCheckException e) {
            assertEquals("La classe MyClass est déjà définie.", e.getMessage());
        }
    }

   @Test 
   public void testcst(){
    ASTcst cst = new ASTcst(0);
        ASTentier entier = new ASTentier(0);
        ASTident cstIdent = new ASTident(1);
        ASTnbre nbre = new ASTnbre(2);
        nbre.jjtSetValue(2);    
        cstIdent.jjtSetValue("deux");
        cst.jjtAddChild(entier, 0);
        cst.jjtAddChild(cstIdent, 1);
        cst.jjtAddChild(nbre, 2); 
        try {
            typeChecker.visit(cst, null);
        } catch (TypeCheckException e) {
            fail("Exception thrown during constant declaration: " + e.getMessage());
        }

   }
    
    
   
    @Test
    public void testMethodDeclaration() {

        ASTcst cst = new ASTcst(0);
        ASTentier entier = new ASTentier(0);
        ASTident cstIdent = new ASTident(1);
        ASTnbre nbre = new ASTnbre(2);
        nbre.jjtSetValue(0);    
        cstIdent.jjtSetValue("zero");
        cst.jjtAddChild(entier, 0);
        cst.jjtAddChild(cstIdent, 1);
        cst.jjtAddChild(nbre, 2);
        try {
            typeChecker.visit(cst, null);
        } catch (TypeCheckException e) {
            fail("Exception thrown during constant declaration: " + e.getMessage());
        }
        ASTentier enti = new ASTentier(0);
        ASTident varIdent = new ASTident(1);
        varIdent.jjtSetValue("zero");
        // ENTETES
        ASTentetes entetes = new ASTentetes(3);
        ASTentete entete = new ASTentete(0);
        ASTenil enil = new ASTenil(1);

        ASTentier entettype = new ASTentier(0);
        ASTident IdentEntete = new ASTident(1);
        IdentEntete.jjtSetValue("x");
        entete.jjtAddChild(entettype, 0);
        entete.jjtAddChild(IdentEntete, 1);

        entetes.jjtAddChild(entete, 0);
        entetes.jjtAddChild(enil, 1);
        
        ASTvnil var = new ASTvnil(4);
        ASTretour retour = new ASTretour(5);
        ASTnbre retourNbre = new ASTnbre(0);

        retourNbre.jjtSetValue(0);
        retour.jjtAddChild(retourNbre, 0);
        ASTmethode methode = new ASTmethode(0);
        methode.jjtAddChild(enti, 0);
        methode.jjtAddChild(varIdent, 1);
        methode.jjtAddChild(entetes, 2);
        methode.jjtAddChild(var, 3);
        methode.jjtAddChild(retour, 4);

        try {
            typeChecker.visit(methode, null);
        } catch (TypeCheckException e) {
            fail("Exception thrown during method declaration: " + e.getMessage());
        }
    }
 

    public void declvariableentier(String nom){
        ASTentier entier = new ASTentier(0);
         ASTident varIdent = new ASTident(1);
         varIdent.jjtSetValue(nom);
         ASTomega omega = new ASTomega(2);
         ASTvar varNode = new ASTvar(0);
         varNode.jjtAddChild(entier, 0);
         varNode.jjtAddChild(varIdent, 1);
         varNode.jjtAddChild(omega, 2);
         
    }
    @Test
    public void testAffectation() {
        // Déclarer la variable x avant de tester l'affectation
        ASTentier entier = new ASTentier(0);
        ASTident varIdent = new ASTident(1);
        varIdent.jjtSetValue("x");
        ASTomega omega = new ASTomega(2);
        ASTvar varNode = new ASTvar(0);
        varNode.jjtAddChild(entier, 0);
        varNode.jjtAddChild(varIdent, 1);
        varNode.jjtAddChild(omega, 2);
    
    
        // Tester l'affectation
        ASTaffectation affectnode = new ASTaffectation(0);
        ASTident varIdent2 = new ASTident(1);
        varIdent2.jjtSetValue("x");
        ASTnbre assignnombre = new ASTnbre(2);
        assignnombre.jjtSetValue(5);
        affectnode.jjtAddChild(varIdent2, 0);
        affectnode.jjtAddChild(assignnombre, 1);
        try {
            MemoryObject t = (MemoryObject) typeChecker.visit(varNode, null);
            assertEquals(t.getType(), ObjectType.INT);
        } catch (TypeCheckException e) {
            fail("Exception thrown during variable declaration: " + e.getMessage());
        }
    }
    @Test
    public void testTantque(){
        ASTvrai vrai = new ASTvrai(0);
        ASTinil inil = new ASTinil(1);
        ASTtantque tantque = new ASTtantque(0);
        tantque.jjtAddChild(vrai, 0);
        tantque.jjtAddChild(inil, 1);
        try {
            typeChecker.visit(tantque, null);
        } catch (TypeCheckException e) {
            fail("Exception thrown during while condition check: " + e.getMessage());
        }

    }
    

    @Test
    public void testTantqueNoBoolean() {
        ASTnbre nbre = new ASTnbre(0);
        nbre.jjtSetValue(4);
        ASTinil inil = new ASTinil(1);
        ASTtantque tantque = new ASTtantque(0);
        tantque.jjtAddChild(nbre, 0);  // pas booléen 
        tantque.jjtAddChild(inil, 1);

        TypeCheckException exception = assertThrows(TypeCheckException.class, () -> {
            typeChecker.visit(tantque, null);
        });

        assertTrue(exception.getMessage().contains("Condition in while statement must be boolean."));
    }
    
    @Test
    public void testIfConditionTypeCheck() {
        ASTvrai vrai = new ASTvrai(0);
        ASTinil inil = new ASTinil(1);
        ASTsi ifNode = new ASTsi(0);
        ifNode.jjtAddChild(vrai, 0);
        ifNode.jjtAddChild(inil, 1);

        try {
            typeChecker.visit(ifNode, null);
        } catch (TypeCheckException e) {
            fail("Exception thrown during if condition check: " + e.getMessage());
        }
    } 
    @Test
    public void testIfConditionTypeCheck2() {
        ASTnbre nbre = new ASTnbre(0);
        nbre.jjtSetValue(1);
        ASTinil inil = new ASTinil(1);
        ASTsi ifNode = new ASTsi(0);
        ifNode.jjtAddChild(nbre, 0);  // Ajouter un nœud non-booléen comme condition
        ifNode.jjtAddChild(inil, 1);

        TypeCheckException exception = assertThrows(TypeCheckException.class, () -> {
            typeChecker.visit(ifNode, null);
        });
        assertTrue(exception.getMessage().contains("Condition in if statement must be boolean."));
    }


    @Test 
    public void testNegation(){
        ASTvrai vrai = new ASTvrai(0);
        ASTnot not = new ASTnot(0);
        not.jjtAddChild(vrai, 0);

        try {
            typeChecker.visit(not, null);
        } catch (TypeCheckException e) {
            fail("Exception thrown during negation check: " + e.getMessage());
        }
    }

    @Test
    public void testNegationNoBoolean() {
        ASTnbre nbre = new ASTnbre(0);
        nbre.jjtSetValue(2);
        ASTnot not = new ASTnot(0);
        not.jjtAddChild(nbre, 0); 

        TypeCheckException exception = assertThrows(TypeCheckException.class, () -> {
            typeChecker.visit(not, null);
        });

        assertTrue(exception.getMessage().contains("Type mismatch"));
    }

    @Test
    public void TestANDoperation(){
        ASTvrai vrai = new ASTvrai(0);
        ASTfaux faux = new ASTfaux(1);
        ASTet et = new ASTet(0);
        et.jjtAddChild(vrai, 0);
        et.jjtAddChild(faux, 1);
        try {
            typeChecker.visit(et, null);
        } catch (TypeCheckException e) {
            fail("Exception thrown during AND operation check: " + e.getMessage());
        }
    }
    @Test
    public void TestANDoperationNoBoolean() {
        ASTnbre nbre = new ASTnbre(0);
        nbre.jjtSetValue(2);
        ASTfaux faux = new ASTfaux(1);
        ASTet et = new ASTet(0);
        et.jjtAddChild(nbre, 0);
        et.jjtAddChild(faux, 1);

        TypeCheckException exception = assertThrows(TypeCheckException.class, () -> {
            typeChecker.visit(et, null);
        });

        assertTrue(exception.getMessage().contains("Type mismatch"));
    }

    @Test
    public void TestORoperation(){
        ASTvrai vrai = new ASTvrai(0);
        ASTfaux faux = new ASTfaux(1);
        ASTou ou = new ASTou(0);
        ou.jjtAddChild(vrai, 0);
        ou.jjtAddChild(faux, 1);
        try {
            typeChecker.visit(ou, null);
        } catch (TypeCheckException e) {
            fail("Exception thrown during OR operation check: " + e.getMessage());
        }
    }
    @Test
    public void TestORoperationNoBoolean() {
        ASTnbre nbre = new ASTnbre(0);
        nbre.jjtSetValue(2);
        ASTfaux faux = new ASTfaux(1);
        ASTou ou = new ASTou(0);
        ou.jjtAddChild(nbre, 0);
        ou.jjtAddChild(faux, 1);

        TypeCheckException exception = assertThrows(TypeCheckException.class, () -> {
            typeChecker.visit(ou, null);
        });

        assertTrue(exception.getMessage().contains("Type mismatch"));
    }
    @Test 
    public void testADDoperation(){
        ASTnbre nbre1 = new ASTnbre(0);
        nbre1.jjtSetValue(2);
        ASTnbre nbre2 = new ASTnbre(1);
        nbre2.jjtSetValue(3);
        ASTadd plus = new ASTadd(0);
        plus.jjtAddChild(nbre1, 0);
        plus.jjtAddChild(nbre2, 1);
        try {
            typeChecker.visit(plus, null);
        } catch (TypeCheckException e) {
            fail("Exception thrown during ADD operation check: " + e.getMessage());
        }
    }
    @Test
    public void testADDoperationNoInteger() {
        ASTnbre nbre = new ASTnbre(0);
        nbre.jjtSetValue(2);
        ASTvrai vrai = new ASTvrai(1);
        ASTadd plus = new ASTadd(0);
        plus.jjtAddChild(nbre, 0);
        plus.jjtAddChild(vrai, 1);

        TypeCheckException exception = assertThrows(TypeCheckException.class, () -> {
            typeChecker.visit(plus, null);
        });

        assertTrue(exception.getMessage().contains("Type mismatch"));
    }
    @Test
    public void testSUBoperation(){
        ASTnbre nbre1 = new ASTnbre(0);
        nbre1.jjtSetValue(2);
        ASTnbre nbre2 = new ASTnbre(1);
        nbre2.jjtSetValue(3);
        ASTsub moins = new ASTsub(0);
        moins.jjtAddChild(nbre1, 0);
        moins.jjtAddChild(nbre2, 1);
        try {
            typeChecker.visit(moins, null);
        } catch (TypeCheckException e) {
            fail("Exception thrown during SUB operation check: " + e.getMessage());
        }
    }
    @Test
    public void testSUBoperationNoInteger() {
        ASTnbre nbre = new ASTnbre(0);
        nbre.jjtSetValue(2);
        ASTvrai vrai = new ASTvrai(1);
        ASTsub moins = new ASTsub(0);
        moins.jjtAddChild(nbre, 0);
        moins.jjtAddChild(vrai, 1);

        TypeCheckException exception = assertThrows(TypeCheckException.class, () -> {
            typeChecker.visit(moins, null);
        });

        assertTrue(exception.getMessage().contains("Type mismatch"));
    }

    @Test
    public void testMULoperation(){
        ASTnbre nbre1 = new ASTnbre(0);
        nbre1.jjtSetValue(2);
        ASTnbre nbre2 = new ASTnbre(1);
        nbre2.jjtSetValue(3);
        ASTmul mul = new ASTmul(0);
        mul.jjtAddChild(nbre1, 0);
        mul.jjtAddChild(nbre2, 1);
        try {
            typeChecker.visit(mul, null);
        } catch (TypeCheckException e) {
            fail("Exception thrown during MUL operation check: " + e.getMessage());
        }
    }
    @Test
    public void testMULoperationNoInteger() {
        ASTnbre nbre = new ASTnbre(0);
        nbre.jjtSetValue(2);
        ASTvrai vrai = new ASTvrai(1);
        ASTmul mul = new ASTmul(0);
        mul.jjtAddChild(nbre, 0);
        mul.jjtAddChild(vrai, 1);

        TypeCheckException exception = assertThrows(TypeCheckException.class, () -> {
            typeChecker.visit(mul, null);
        });
        assertTrue(exception.getMessage().contains("Type mismatch"));
    }

    @Test
    public void testListexpvide(){
        ASTexnil exnil = new ASTexnil(0);
        ASTlistexp listexp = new ASTlistexp(0);
        listexp.jjtAddChild(exnil, 0);
        try {
            typeChecker.visit(listexp, null);
        } catch (TypeCheckException e) {
            fail("Exception thrown during listexp check: " + e.getMessage());
        }
    }
 
    @Test 
    public void testListexp1(){
        ASTnbre nbre1 = new ASTnbre(0);
        nbre1.jjtSetValue(2);
        ASTnbre nbre2 = new ASTnbre(1);
        nbre2.jjtSetValue(3);
        ASTlistexp listexp = new ASTlistexp(0);
        ASTexnil exnil = new ASTexnil(0);
        ASTexp exp = new ASTexp(0);
        exp.jjtAddChild(nbre1, 0);
        listexp.jjtAddChild(exp, 0);
        listexp.jjtAddChild(exnil, 1);
        try {
            typeChecker.visit(listexp, null);
        } catch (TypeCheckException e) {
            fail("Exception thrown during listexp check: " + e.getMessage());
        }
    }
    @Test
    public void testListexp2() {
        ASTincrement incrementNode = increment("z",4);
        ASTlistexp listexp = new ASTlistexp(0);
        ASTexnil exnil = new ASTexnil(0);
        ASTexp exp = new ASTexp(0);
        exp.jjtAddChild(incrementNode, 0);
        listexp.jjtAddChild(exp, 0);
        listexp.jjtAddChild(exnil, 1);
        try {
            typeChecker.visit(listexp, null);
        } catch (TypeCheckException e) {
            fail("Exception thrown during listexp check: " + e.getMessage());
        }
        
    }
     
    @Test 
    public void testAppelI(){
        // declarer methode int zero(x:entier){return 0;}
        ASTentier enti = new ASTentier(0);
        ASTident nomMeth = new ASTident(0);
        nomMeth.jjtSetValue("zero");
        // ENTETES pour methode : (x:entier)
        ASTentetes entetes = new ASTentetes(0);
        ASTentete entete = new ASTentete(0);
        ASTenil enil = new ASTenil(0);

        ASTentier entettype = new ASTentier(0);
        ASTident IdentEntete = new ASTident(0);
        IdentEntete.jjtSetValue("x");
        entete.jjtAddChild(entettype, 0);
        entete.jjtAddChild(IdentEntete, 1);

        entetes.jjtAddChild(entete, 0);
        entetes.jjtAddChild(enil, 1);
        
        ASTvnil var = new ASTvnil(0);
        ASTretour retour = new ASTretour(0);
        ASTnbre retourNbre = new ASTnbre(0);

        retourNbre.jjtSetValue(0);
        retour.jjtAddChild(retourNbre, 0);
        ASTmethode methode = new ASTmethode(0);
        methode.jjtAddChild(enti, 0);
        methode.jjtAddChild(nomMeth, 1);
        methode.jjtAddChild(entetes, 2);
        methode.jjtAddChild(var, 3);
        methode.jjtAddChild(retour, 4);

        try {
            typeChecker.visit(methode, null);
        } catch (TypeCheckException e) {
            fail("Exception thrown during method declaration: " + e.getMessage());
        }

        // AppelI: zero(5)
        ASTappelI appelI = new ASTappelI(0);
        ASTident ident = new ASTident(0);
        ident.jjtSetValue("zero");
        //  listexp 
        ASTnbre nbre1 = new ASTnbre(0);
        nbre1.jjtSetValue(2);
        ASTnbre nbre2 = new ASTnbre(1);
        nbre2.jjtSetValue(3);
        ASTlistexp listexp = new ASTlistexp(0);
        ASTexnil exnil = new ASTexnil(0);
        ASTexp exp = new ASTexp(0);
        exp.jjtAddChild(nbre1, 0);
        listexp.jjtAddChild(exp, 0);
        listexp.jjtAddChild(exnil, 1);
       
       
        appelI.jjtAddChild(ident, 0);
        appelI.jjtAddChild(listexp , 1);
        try {
            typeChecker.visit(appelI, null);
        } catch (TypeCheckException e) {
            fail("Exception thrown during appelI  check: " + e.getMessage());
        }
    }

    @Test
    public void testIncrementOnInteger() {
        // Déclarer une variable y de type entier
        ASTcst cst = new ASTcst(0);
        ASTentier entier = new ASTentier(0);
        ASTident cstIdent = new ASTident(1);
        ASTnbre nbre = new ASTnbre(2);
        nbre.jjtSetValue(2);    
        cstIdent.jjtSetValue("y");
        cst.jjtAddChild(entier, 0);
        cst.jjtAddChild(cstIdent, 1);
        cst.jjtAddChild(nbre, 2); 
        try {
            typeChecker.visit(cst, null);
        } catch (TypeCheckException e) {
            fail("Exception thrown during variable declaration: " + e.getMessage());
        }
        ASTincrement incrementNode = new ASTincrement(0);
        ASTident varIdent1 = new ASTident(0);
        varIdent1.jjtSetValue("y");
        incrementNode.jjtAddChild(varIdent1, 0);

        try {
           Object t = typeChecker.visit(incrementNode, null);
            assertEquals(t, ObjectType.INT);
        } catch (TypeCheckException e) {
            fail("Expected exception for incrementing a integer variable");
        }
    }
    
    @Test
    public void testIncrementOnNonInteger() {
        // Déclarer une variable y de type BOOLEAN 
        ASTbooleen booleen = new ASTbooleen(0);
        ASTident varIdent = new ASTident(1);
        varIdent.jjtSetValue("y");
        ASTomega omega = new ASTomega(2);
        ASTvar varNode = new ASTvar(0);
        varNode.jjtAddChild(booleen, 0);
        varNode.jjtAddChild(varIdent, 1);
        varNode.jjtAddChild(omega, 2);
        try {
            typeChecker.visit(varNode, null);
        } catch (TypeCheckException e) {
            fail("Exception thrown during variable declaration: " + e.getMessage());
        }
        // Tester l'incrémentation sur une variable non-entière
        ASTincrement incrementNode = new ASTincrement(0);
        ASTident varIdent1 = new ASTident(1);
        varIdent1.jjtSetValue("y");
        incrementNode.jjtAddChild(varIdent1, 0);
    
        try {
            typeChecker.visit(incrementNode, null);
            fail("Expected exception for incrementing a non-integer variable");
        } catch (TypeCheckException e) {
            assertTrue(e.getMessage().contains("Type mismatch"));
        }
    }

    // Créer une constante entière
    public void creerCstInt(String nom, int valeur){
        ASTcst cst = new ASTcst(0);
        ASTentier entier = new ASTentier(0);
        ASTident cstIdent = new ASTident(1);
        ASTnbre nbre = new ASTnbre(2);
        nbre.jjtSetValue(valeur);    
        cstIdent.jjtSetValue(nom);
        cst.jjtAddChild(entier, 0);
        cst.jjtAddChild(cstIdent, 1);
        cst.jjtAddChild(nbre, 2); 
        try {
            typeChecker.visit(cst, null);
        } catch (TypeCheckException e) {
            fail("Exception thrown during constant declaration: " + e.getMessage());
        }
    }
    // Créer un nœud d'incrémentation
    public ASTincrement  increment(String nom, int valeur){
        ASTincrement incrementNode = new ASTincrement(0);
        creerCstInt(nom, valeur);
        ASTident varIdent1 = new ASTident(0);
        varIdent1.jjtSetValue(nom);
         incrementNode.jjtAddChild(varIdent1, 0);
         return incrementNode;
    }
    
}

