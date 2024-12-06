package fr.m1comp5;
import fr.m1comp5.mjj.generated.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import fr.m1comp5.Debug.AppLogger;
import fr.m1comp5.Debug.TestLoggerListener;
import fr.m1comp5.Typechecker.TypeChecker;


public class TypeCheckerTest {

    private TypeChecker typeChecker;
    TestLoggerListener loggerListener = new TestLoggerListener();
    AppLogger logger = AppLogger.getInstance();


    
    @BeforeEach
    public void setUp() {
        typeChecker = new TypeChecker();
        logger = AppLogger.getInstance();
        loggerListener = new TestLoggerListener();
        logger.addLoggerListener(loggerListener);
        AppLogger.setDebugMode(true); 
    }

    @Test
    public void testIdentCorrect() {
        ASTIdent ident = new ASTIdent(0);
        ident.jjtSetValue("Xml12");
        typeChecker.visit(ident, null);
        assertTrue(loggerListener.getMessages().isEmpty(), 
        "Aucun log d'erreur ne devrait être produit pour un identifiant valide.");
    }

    @Test
    public void testIdentIncorrect() {
        ASTIdent ident = new ASTIdent(0);
        ident.jjtSetValue("12Xml");
        typeChecker.visit(ident, null);
        assertFalse(loggerListener.getMessages().isEmpty(), 
                "Expected exception for incorrect ident");
        assertTrue(loggerListener.getMessages().get(0).contains("Invalid identifier: 12Xml"), 
                "Expected log message for incorrect ident");
    }

    @Test 
    public void testTableau() {
        ASTEntier int1 = new ASTEntier(0);
        ASTIdent tabl = new ASTIdent(0);
        tabl.jjtSetValue("tab");  
        ASTVnil nill = new ASTVnil(0);
        ASTTableau tab = new ASTTableau(0);
        tab.jjtAddChild(int1, 0);
        tab.jjtAddChild(tabl, 1); 
        tab.jjtAddChild(nill, 2);
        typeChecker.visit(tab, null);
        assertTrue(loggerListener.getMessages().isEmpty(), 
                "Aucun log d'erreur ne devrait être produit pour un tableau valide.");
    }

    @Test 
    public void testLong() {
        ASTIdent longueur = new ASTIdent(0);
        longueur.jjtSetValue("exemple");
        ASTLongeur longueurNode = new ASTLongeur(0);
        longueurNode.jjtAddChild(longueur, 0);
        typeChecker.visit(longueurNode, null); 
        assertTrue(loggerListener.getMessages().isEmpty(), 
                "Exception thrown during Longueur declaration: ");
    }

    @Test
    public void testVariableDeclaration() {
        ASTEntier entier = new ASTEntier(0);
        ASTIdent varIdent = new ASTIdent(1);
        varIdent.jjtSetValue("myVar");
        ASTNbre n = new ASTNbre(2);
        n.jjtSetValue(5);
        ASTVar varNode = new ASTVar(0);
        varNode.jjtAddChild(entier, 0);
        varNode.jjtAddChild(varIdent, 1);
        varNode.jjtAddChild(n, 2);

        MemoryObject t = (MemoryObject) typeChecker.visit(varNode, null);
        assertEquals(t.getId(), "myVar");
        assertEquals(t.getNature(), ObjectNature.VAR);
        assertEquals(t.getType(), ObjectType.INT);
        assertEquals(t.getValue(), null);
        assertTrue(loggerListener.getMessages().isEmpty(), 
                "Aucun log d'erreur ne devrait être produit pour une déclaration de variable valide.");
    }

   
    @Test 
    public void testCst() {
        ASTCst cst = new ASTCst(0);
        ASTEntier entier = new ASTEntier(0);
        ASTIdent cstIdent = new ASTIdent(1);
        ASTNbre nbre = new ASTNbre(2);
        nbre.jjtSetValue(2);    
        cstIdent.jjtSetValue("deux");
        cst.jjtAddChild(entier, 0);
        cst.jjtAddChild(cstIdent, 1);
        cst.jjtAddChild(nbre, 2); 
        typeChecker.visit(cst, null);
        assertTrue(loggerListener.getMessages().isEmpty(), 
                "Aucun log d'erreur ne devrait être produit pour une constante valide.");
    }
    
    @Test
    public void testMethodDeclaration() {
        ASTCst cst = new ASTCst(0);
        ASTEntier entier = new ASTEntier(0);
        ASTIdent cstIdent = new ASTIdent(1);
        ASTNbre nbre = new ASTNbre(2);
        nbre.jjtSetValue(0);    
        cstIdent.jjtSetValue("zero");
        cst.jjtAddChild(entier, 0);
        cst.jjtAddChild(cstIdent, 1);
        cst.jjtAddChild(nbre, 2);
        typeChecker.visit(cst, null);
        assertTrue(loggerListener.getMessages().isEmpty(), 
                "Aucun log d'erreur ne devrait être produit pour une constante valide.");

        ASTEntier enti = new ASTEntier(0);
        ASTIdent varIdent = new ASTIdent(1);
        varIdent.jjtSetValue("zero");
        // ENTETES
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

        typeChecker.visit(methode, null);
        assertTrue(loggerListener.getMessages().isEmpty(), 
                "Aucun log d'erreur ne devrait être produit pour une méthode valide.");
    }

    @Test
    public void testAffectation() {
        // Déclarer la variable x avant de tester l'affectation
        ASTEntier entier = new ASTEntier(0);
        ASTIdent varIdent = new ASTIdent(0);
        varIdent.jjtSetValue("x");
        ASTNbre n = new ASTNbre(0);
        n.jjtSetValue(5);
        ASTVar varNode = new ASTVar(0);
        varNode.jjtAddChild(entier, 0);
        varNode.jjtAddChild(varIdent, 1);
        varNode.jjtAddChild(n, 2);
        typeChecker.visit(varNode, null);

        // Tester l'affectation
        ASTAffectation affectnode = new ASTAffectation(0);
        ASTIdent varIdent2 = new ASTIdent(1);
        varIdent2.jjtSetValue("x");
        ASTNbre assignnombre = new ASTNbre(2);
        assignnombre.jjtSetValue(5);
        affectnode.jjtAddChild(varIdent2, 0);
        affectnode.jjtAddChild(assignnombre, 1);

        typeChecker.visit(affectnode, null);
        assertTrue(loggerListener.getMessages().isEmpty(), 
                "Aucun log d'erreur ne devrait être produit pour une affectation valide.");
    }

    @Test
    public void testTantque() {
        ASTVrai vrai = new ASTVrai(0);
        ASTInil inil = new ASTInil(1);
        ASTTantQue tantque = new ASTTantQue(0);
        tantque.jjtAddChild(vrai, 0);
        tantque.jjtAddChild(inil, 1);
        typeChecker.visit(tantque, null);
        assertTrue(loggerListener.getMessages().isEmpty(), 
                "Aucun log d'erreur ne devrait être produit pour une boucle tantque valide.");
    }

    @Test
    public void testTantqueNoBoolean() {
        ASTNbre nbre = new ASTNbre(0);
        nbre.jjtSetValue(4);
        ASTInil inil = new ASTInil(1);
        ASTTantQue tantque = new ASTTantQue(0);
        tantque.jjtAddChild(nbre, 0);  // pas booléen 
        tantque.jjtAddChild(inil, 1);

        typeChecker.visit(tantque, null);
        assertFalse(loggerListener.getMessages().isEmpty(), 
                "Expected log message for non-boolean condition in while loop");
        assertTrue(loggerListener.getMessages().get(0).contains("Condition in while statement must be boolean"), 
                "Expected log message for non-boolean condition in while loop");
    }

    @Test
    public void testIfConditionTypeCheck() {
        ASTVrai vrai = new ASTVrai(0);
        ASTInil inil = new ASTInil(1);
        ASTSi ifNode = new ASTSi(0);
        ifNode.jjtAddChild(vrai, 0);
        ifNode.jjtAddChild(inil, 1);

        typeChecker.visit(ifNode, null);
        assertTrue(loggerListener.getMessages().isEmpty(), 
                "Aucun log d'erreur ne devrait être produit pour une condition if valide.");
    }

    @Test
    public void testIfConditionTypeCheck2() {
        ASTNbre nbre = new ASTNbre(0);
        nbre.jjtSetValue(1);
        ASTInil inil = new ASTInil(1);
        ASTSi ifNode = new ASTSi(0);
        ifNode.jjtAddChild(nbre, 0);  // Ajouter un nœud non-booléen comme condition
        ifNode.jjtAddChild(inil, 1);

        typeChecker.visit(ifNode, null);
        assertFalse(loggerListener.getMessages().isEmpty(), 
                "Expected log message for non-boolean condition in if statement");
        assertTrue(loggerListener.getMessages().get(0).contains("Condition in if statement must be boolean"), 
                "Expected log message for non-boolean condition in if statement");
    }

    @Test 
    public void testNegation() {
        ASTVrai vrai = new ASTVrai(0);
        ASTNot not = new ASTNot(0);
        not.jjtAddChild(vrai, 0);

        typeChecker.visit(not, null);
        assertTrue(loggerListener.getMessages().isEmpty(), 
                "Aucun log d'erreur ne devrait être produit pour une négation valide.");
    }

    @Test
    public void testNegationNoBoolean() {
        ASTNbre nbre = new ASTNbre(0);
        nbre.jjtSetValue(2);
        ASTNot not = new ASTNot(0);
        not.jjtAddChild(nbre, 0); 

        typeChecker.visit(not, null);
        assertFalse(loggerListener.getMessages().isEmpty(), 
                "Expected log message for non-boolean operand in negation");
        assertTrue(loggerListener.getMessages().get(0).contains("Type mismatch: Expected BOOLEAN for operand"), 
                "Expected log message for non-boolean operand in negation");
    }

    @Test
    public void testANDoperation() {
        ASTVrai vrai = new ASTVrai(0);
        ASTFaux faux = new ASTFaux(1);
        ASTEt et = new ASTEt(0);
        et.jjtAddChild(vrai, 0);
        et.jjtAddChild(faux, 1);
        
        typeChecker.visit(et, null);
        assertTrue(loggerListener.getMessages().isEmpty(), 
                "Aucun log d'erreur ne devrait être produit pour une opération AND valide.");
    }

    @Test
    public void testANDoperationNoBoolean() {
        ASTNbre nbre = new ASTNbre(0);
        nbre.jjtSetValue(2);
        ASTFaux faux = new ASTFaux(1);
        ASTEt et = new ASTEt(0);
        et.jjtAddChild(nbre, 0);
        et.jjtAddChild(faux, 1);

        typeChecker.visit(et, null);
        assertFalse(loggerListener.getMessages().isEmpty(), 
                "Expected log message for non-boolean operand in AND operation");
        assertTrue(loggerListener.getMessages().get(0).contains("Type mismatch: Expected BOOLEAN for both operands"), 
                "Expected log message for non-boolean operand in AND operation");
    }

    @Test
    public void testORoperation() {
        ASTVrai vrai = new ASTVrai(0);
        ASTFaux faux = new ASTFaux(1);
        ASTOu ou = new ASTOu(0);
        ou.jjtAddChild(vrai, 0);
        ou.jjtAddChild(faux, 1);
        typeChecker.visit(ou, null);
        assertTrue(loggerListener.getMessages().isEmpty(), 
                "Aucun log d'erreur ne devrait être produit pour une opération OR valide.");
    }

    @Test
    public void testORoperationNoBoolean() {
        ASTNbre nbre = new ASTNbre(0);
        nbre.jjtSetValue(2);
        ASTFaux faux = new ASTFaux(1);
        ASTOu ou = new ASTOu(0);
        ou.jjtAddChild(nbre, 0);
        ou.jjtAddChild(faux, 1);

        typeChecker.visit(ou, null);
        assertFalse(loggerListener.getMessages().isEmpty(), 
                "Expected log message for non-boolean operand in OR operation");
        assertTrue(loggerListener.getMessages().get(0).contains("Type mismatch"), 
                "Expected log message for non-boolean operand in OR operation");
    }

    @Test 
    public void testADDoperation() {
        ASTNbre nbre1 = new ASTNbre(0);
        nbre1.jjtSetValue(2);
        ASTNbre nbre2 = new ASTNbre(1);
        nbre2.jjtSetValue(3);
        ASTAdd plus = new ASTAdd(0);
        plus.jjtAddChild(nbre1, 0);
        plus.jjtAddChild(nbre2, 1);
     
        typeChecker.visit(plus, null);
        assertTrue(loggerListener.getMessages().isEmpty(), 
                "Aucun log d'erreur ne devrait être produit pour une opération ADD valide.");
    }

    @Test
    public void testADDoperationNoInteger() {
        ASTNbre nbre = new ASTNbre(0);
        nbre.jjtSetValue(2);
        ASTVrai vrai = new ASTVrai(1);
        ASTAdd plus = new ASTAdd(0);
        plus.jjtAddChild(nbre, 0);
        plus.jjtAddChild(vrai, 1);

        typeChecker.visit(plus, null);
        assertFalse(loggerListener.getMessages().isEmpty(), 
                "Expected log message for non-integer operand in ADD operation");
        assertTrue(loggerListener.getMessages().get(0).contains("Type mismatch"), 
                "Expected log message for non-integer operand in ADD operation");
    }
    @Test
    public void TestORoperation(){
        ASTVrai vrai = new ASTVrai(0);
        ASTFaux faux = new ASTFaux(1);
        ASTOu ou = new ASTOu(0);
        ou.jjtAddChild(vrai, 0);
        ou.jjtAddChild(faux, 1);
        typeChecker.visit(ou, null);
        assertTrue(loggerListener.getMessages().isEmpty());
    }

    @Test
    public void testSUBoperation(){
        ASTNbre nbre1 = new ASTNbre(0);
        nbre1.jjtSetValue(2);
        ASTNbre nbre2 = new ASTNbre(1);
        nbre2.jjtSetValue(3);
        ASTSub moins = new ASTSub(0);
        moins.jjtAddChild(nbre1, 0);
        moins.jjtAddChild(nbre2, 1);
        typeChecker.visit(moins, null);
        assertTrue(loggerListener.getMessages().isEmpty());

        
    }
    @Test
    public void testSUBoperationNoInteger() {
        ASTNbre nbre = new ASTNbre(0);
        nbre.jjtSetValue(2);
        ASTVrai vrai = new ASTVrai(1);
        ASTSub moins = new ASTSub(0);
        moins.jjtAddChild(nbre, 0);
        moins.jjtAddChild(vrai, 1);

        typeChecker.visit(moins, null);

        assertFalse(loggerListener.getMessages().isEmpty());    
     }

    @Test
    public void testMULoperation(){
        ASTNbre nbre1 = new ASTNbre(0);
        nbre1.jjtSetValue(2);
        ASTNbre nbre2 = new ASTNbre(1);
        nbre2.jjtSetValue(3);
        ASTMul mul = new ASTMul(0);
        mul.jjtAddChild(nbre1, 0);
        mul.jjtAddChild(nbre2, 1);
        typeChecker.visit(mul, null);
        assertTrue(loggerListener.getMessages().isEmpty());

    }
    @Test
    public void testMULoperationNoInteger() {
        ASTNbre nbre = new ASTNbre(0);
        nbre.jjtSetValue(2);
        ASTVrai vrai = new ASTVrai(1);
        ASTMul mul = new ASTMul(0);
        mul.jjtAddChild(nbre, 0);
        mul.jjtAddChild(vrai, 1);

            typeChecker.visit(mul, null);
            assertFalse(loggerListener.getMessages().isEmpty());    

    }
 
    @Test
    public void testListexpvide(){
        ASTExnil exnil = new ASTExnil(0);
        ASTListExp listexp = new ASTListExp(0);
        listexp.jjtAddChild(exnil, 0);
       
        typeChecker.visit(listexp, null);
        assertTrue(loggerListener.getMessages().isEmpty());
      
    }
 
    @Test 
    public void testListexp1(){
        ASTNbre nbre1 = new ASTNbre(0);
        nbre1.jjtSetValue(2);
        ASTNbre nbre2 = new ASTNbre(1);
        nbre2.jjtSetValue(3);
        ASTListExp listexp = new ASTListExp(0);
        ASTExnil exnil = new ASTExnil(0);
        ASTExp exp = new ASTExp(0);
        exp.jjtAddChild(nbre1, 0);
        listexp.jjtAddChild(exp, 0);
        listexp.jjtAddChild(exnil, 1);
        typeChecker.visit(listexp, null);
        assertTrue(loggerListener.getMessages().isEmpty());
        
    }

    @Test
    public void testListexp2() {
        ASTIncrement incrementNode = increment("z",4);
        ASTListExp listexp = new ASTListExp(0);
        ASTExnil exnil = new ASTExnil(0);
        ASTExp exp = new ASTExp(0);
        exp.jjtAddChild(incrementNode, 0);
        listexp.jjtAddChild(exp, 0);
        listexp.jjtAddChild(exnil, 1);
        typeChecker.visit(listexp, null);
        assertTrue(loggerListener.getMessages().isEmpty());
    }

    @Test
    public void  testEntetes(){
        ASTEntetes entetes = new ASTEntetes(0);
       
        ASTEntetes entests1 = new ASTEntetes(0);
        // entete1 : (x:entier)
        ASTEntete entete1 = new ASTEntete(0);
        ASTEntier entettype1 = new ASTEntier(0);
        ASTIdent IdentEntete1 = new ASTIdent(0);
        entete1.jjtAddChild(entettype1, 0);
        IdentEntete1.jjtSetValue("x");
        entete1.jjtAddChild(IdentEntete1, 1);
        entests1.jjtAddChild(entete1, 0);



        ASTEntete entete = new ASTEntete(0);
        ASTBooleen type = new ASTBooleen(0);
        ASTIdent ident = new ASTIdent(0);
        ident.jjtSetValue("y");
        entete.jjtAddChild(type, 0);
        entete.jjtAddChild(ident, 1);
        entetes.jjtAddChild(entete, 0);
        entetes.jjtAddChild(entests1, 1);
        
        typeChecker.visit(entetes, null);
        assertTrue(loggerListener.getMessages().isEmpty());


    }
     
    @Test 
    public void testAppelI(){
        // declarer methode int zero(x:entier){return 0;}
        ASTEntier enti = new ASTEntier(0);
        ASTIdent nomMeth = new ASTIdent(0);
        nomMeth.jjtSetValue("zero");
        // ENTETES pour methode : (x:entier)
        ASTEntetes entetes = new ASTEntetes(0);
        ASTEntete entete = new ASTEntete(0);
        ASTEnil enil = new ASTEnil(0);

        ASTEntier entettype = new ASTEntier(0);
        ASTIdent IdentEntete = new ASTIdent(0);
        IdentEntete.jjtSetValue("x");
        entete.jjtAddChild(entettype, 0);
        entete.jjtAddChild(IdentEntete, 1);

        entetes.jjtAddChild(entete, 0);
        entetes.jjtAddChild(enil, 1);
        
        ASTVnil var = new ASTVnil(0);
        ASTRetour retour = new ASTRetour(0);
        ASTNbre retourNbre = new ASTNbre(0);

        retourNbre.jjtSetValue(0);
        retour.jjtAddChild(retourNbre, 0);
        ASTMethode methode = new ASTMethode(0);
        methode.jjtAddChild(enti, 0);
        methode.jjtAddChild(nomMeth, 1);
        methode.jjtAddChild(entetes, 2);
        methode.jjtAddChild(var, 3);
        methode.jjtAddChild(retour, 4);

        typeChecker.visit(methode, null);
        assertTrue(loggerListener.getMessages().isEmpty());


        // AppelI: zero(5)
        ASTAppelI appelI = new ASTAppelI(0);
        ASTIdent ident = new ASTIdent(0);
        ident.jjtSetValue("zero");
        //  listexp 
        ASTNbre nbre1 = new ASTNbre(0);
        nbre1.jjtSetValue(2);
        ASTNbre nbre2 = new ASTNbre(1);
        nbre2.jjtSetValue(3);
        ASTListExp listexp = new ASTListExp(0);
        ASTExnil exnil = new ASTExnil(0);
        ASTExp exp = new ASTExp(0);
        exp.jjtAddChild(nbre1, 0);
        listexp.jjtAddChild(exp, 0);
        listexp.jjtAddChild(exnil, 1);
       
       
        appelI.jjtAddChild(ident, 0);
        appelI.jjtAddChild(listexp , 1);
        typeChecker.visit(appelI, null);
        assertTrue(loggerListener.getMessages().isEmpty());

    }

    @Test 
    public void testAppelE(){
        // declarer methode int zero(x:entier){return 0;}
        ASTEntier enti = new ASTEntier(0);
        ASTIdent nomMeth = new ASTIdent(0);
        nomMeth.jjtSetValue("zero");
        // ENTETES pour methode : (x:entier)
        ASTEntetes entetes = new ASTEntetes(0);
        ASTEntete entete = new ASTEntete(0);
        ASTEnil enil = new ASTEnil(0);

        ASTEntier entettype = new ASTEntier(0);
        ASTIdent IdentEntete = new ASTIdent(0);
        IdentEntete.jjtSetValue("x");
        entete.jjtAddChild(entettype, 0);
        entete.jjtAddChild(IdentEntete, 1);

        entetes.jjtAddChild(entete, 0);
        entetes.jjtAddChild(enil, 1);
        
        ASTVnil var = new ASTVnil(0);
        ASTRetour retour = new ASTRetour(0);
        ASTNbre retourNbre = new ASTNbre(0);

        retourNbre.jjtSetValue(0);
        retour.jjtAddChild(retourNbre, 0);
        ASTMethode methode = new ASTMethode(0);
        methode.jjtAddChild(enti, 0);
        methode.jjtAddChild(nomMeth, 1);
        methode.jjtAddChild(entetes, 2);
        methode.jjtAddChild(var, 3);
        methode.jjtAddChild(retour, 4);

        typeChecker.visit(methode, null);
        assertTrue(loggerListener.getMessages().isEmpty());

       

        // AppelI: zero(5)
        ASTAppelI appelE = new ASTAppelI(0);
        ASTIdent ident = new ASTIdent(0);
        ident.jjtSetValue("zero");
        //  listexp 
        ASTNbre nbre1 = new ASTNbre(0);
        nbre1.jjtSetValue(2);
        ASTNbre nbre2 = new ASTNbre(1);
        nbre2.jjtSetValue(3);
        ASTListExp listexp = new ASTListExp(0);
        ASTExnil exnil = new ASTExnil(0);
        ASTExp exp = new ASTExp(0);
        exp.jjtAddChild(nbre1, 0);
        listexp.jjtAddChild(exp, 0);
        listexp.jjtAddChild(exnil, 1);
       
       
        appelE.jjtAddChild(ident, 0);
        appelE.jjtAddChild(listexp , 1);
        typeChecker.visit(appelE, null);
        assertTrue(loggerListener.getMessages().isEmpty());

    }

    @Test
    public void testIncrementOnInteger() {
        // Déclarer une variable y de type entier
        ASTCst cst = new ASTCst(0);
        ASTEntier entier = new ASTEntier(0);
        ASTIdent cstIdent = new ASTIdent(0);
        ASTNbre nbre = new ASTNbre(0);
        nbre.jjtSetValue(2);    
        cstIdent.jjtSetValue("y");
        cst.jjtAddChild(entier, 0);
        cst.jjtAddChild(cstIdent, 1);
        cst.jjtAddChild(nbre, 2); 
            
        typeChecker.visit(cst, null);
        assertTrue(loggerListener.getMessages().isEmpty());    

       
        ASTIncrement incrementNode = new ASTIncrement(0);
        ASTIdent varIdent1 = new ASTIdent(0);
        varIdent1.jjtSetValue("y");
        incrementNode.jjtAddChild(varIdent1, 0);

           Object t = typeChecker.visit(incrementNode, null);
           assertEquals(t, ObjectType.INT);
    }
    
    @Test
    public void testIncrementOnNonInteger() {
        // Déclarer une variable y de type BOOLEAN 
        ASTBooleen booleen = new ASTBooleen(0);
        ASTIdent varIdent = new ASTIdent(1);
        varIdent.jjtSetValue("y");
        ASTFaux faux = new ASTFaux(0);
        ASTVar varNode = new ASTVar(0);
        varNode.jjtAddChild(booleen, 0);
        varNode.jjtAddChild(varIdent, 1);
        varNode.jjtAddChild(faux, 2);
        typeChecker.visit(varNode, null);
        assertTrue(loggerListener.getMessages().isEmpty());

        // Tester l'incrémentation sur une variable non-entière
        ASTIncrement incrementNode = new ASTIncrement(0);
        ASTIdent varIdent1 = new ASTIdent(1);
        varIdent1.jjtSetValue("y");
        incrementNode.jjtAddChild(varIdent1, 0);
    
            typeChecker.visit(incrementNode, null);
            assertFalse(loggerListener.getMessages().isEmpty());
        
    }

    // Créer une constante entière
    public void creerCstInt(String nom, int valeur){
        ASTCst cst = new ASTCst(0);
        ASTEntier entier = new ASTEntier(0);
        ASTIdent cstIdent = new ASTIdent(1);
        ASTNbre nbre = new ASTNbre(2);
        nbre.jjtSetValue(valeur);    
        cstIdent.jjtSetValue(nom);
        cst.jjtAddChild(entier, 0);
        cst.jjtAddChild(cstIdent, 1);
        cst.jjtAddChild(nbre, 2); 
        typeChecker.visit(cst, null);
        assertTrue(loggerListener.getMessages().isEmpty());

    }
    // Créer un nœud d'incrémentation
    public ASTIncrement  increment(String nom, int valeur){
        ASTIncrement incrementNode = new ASTIncrement(0);
        creerCstInt(nom, valeur);
        ASTIdent varIdent1 = new ASTIdent(0);
        varIdent1.jjtSetValue(nom);
         incrementNode.jjtAddChild(varIdent1, 0);
         return incrementNode;
    }

    @Test 
    public void testInstrs(){
        ASTIncrement inc = increment("x", 0); 
        ASTInstrs instrs = new ASTInstrs(0);
        ASTInil inil = new ASTInil(0);
        instrs.jjtAddChild(inc, 0);
        instrs.jjtAddChild(inil, 1);
        typeChecker.visit(instrs, null);
        assertTrue(loggerListener.getMessages().isEmpty());


    }
    @Test
    public void testInstrs2(){
        ASTIncrement inc = increment("x", 0); 
        ASTIncrement inc2 = increment("y", 0); 
        ASTInstrs instrs1 = new ASTInstrs(0);

        ASTInstrs instrs2 = new ASTInstrs(0);
        ASTInil inil2 = new ASTInil(1);
        
        instrs2.jjtAddChild(inc2, 0);
        instrs2.jjtAddChild(inil2, 1);
        
        instrs1.jjtAddChild(inc, 0);
        instrs1.jjtAddChild(instrs2, 1);
        typeChecker.visit(instrs1, null);
        assertTrue(loggerListener.getMessages().isEmpty());

          
    }

    @Test 
    public void testecrire(){
        ASTIdent ident = new ASTIdent(0);
        ident.jjtSetValue("x");
        ASTEcrire ecrire = new ASTEcrire(0);
        ecrire.jjtAddChild(ident, 0);
        typeChecker.visit(ecrire, null);
        assertTrue(loggerListener.getMessages().isEmpty());

    }
    @Test
    public void testecrire2(){
        ASTChaine chaine = new ASTChaine(0);
        chaine.jjtSetValue("Hello World");
        ASTEcrire ecrire = new ASTEcrire(0);
        ecrire.jjtAddChild(chaine, 0);
        typeChecker.visit(ecrire, null);
        assertTrue(loggerListener.getMessages().isEmpty());

    }

}

