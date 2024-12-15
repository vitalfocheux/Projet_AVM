package fr.m1comp5.Typechecker;
import fr.m1comp5.*;
import fr.m1comp5.custom.exception.VisitorException;
import fr.m1comp5.mjj.generated.*;
import java.util.ArrayList;
import java.util.List;
import fr.m1comp5.Logger.AppLogger;


public class TypeChecker implements MiniJajaVisitor {

    private SymbolTable symbolTable;
    private Stack stack = new Stack(); // Pile pour gérer les portées
    private String currentMethod = null; // Méthode courante
    private AppLogger logger = AppLogger.getInstance();

    public TypeChecker() {
        try {
            stack.push(new MemoryObject("global", new HashTable(), ObjectNature.VAR, ObjectType.VOID)); // Initialiser la table des symboles globale
            } catch (StackException e) {
                e.printStackTrace();
        }
    }

    @Override
    public Object visit(ASTRoot node, Object data) throws VisitorException
    {
        return visitChildren(node, data);
    } 
   

     @Override
     public Object visit(ASTClasse node, Object data) throws VisitorException
     {

        ASTIdent classNameNode = (ASTIdent) node.jjtGetChild(0);
        String className = (String) classNameNode.jjtGetValue(); 

        try {
            //((HashTable) stack.getTop().getValue()).put(new MemoryObject(className, new HashTable(), null, ObjectType.VOID));    
            stack.push(new MemoryObject(className, new HashTable(), null, ObjectType.VOID));
        } catch (StackException  e) {
            e.printStackTrace();
        }
        // Visite des déclarations et méthodes dans la classe
        for (int i = 1; i < node.jjtGetNumChildren(); i++) {
            node.jjtGetChild(i).jjtAccept(this, data);
        }
    
        try {
            stack.pop(); // Retirer la portée de la classe après traitement
            } catch (StackException e) {
            e.printStackTrace();
        }

    
        return ObjectType.VOID;
    }
    
    @Override
    public Object visit(ASTIdent node, Object data) {
        Object value = node.jjtGetValue();

        if (value instanceof String) {
            String ident = (String) value;
            if (ident.matches("[a-zA-Z][a-zA-Z0-9]*")) {
                return ident;
            } else {
                 logger.logError("Invalid identifier: " + ident + ". Identifiers must start with a letter and contain only letters and digits.");
            }
        } else {
                logger.logError("Expected a string identifier, but got: " + value );
        }
        return null;
    }


    @Override
    public Object visit(ASTVnil node, Object data) {
        return ObjectType.OMEGA;
    }

    @Override
    public Object visit(ASTDecls node, Object data) throws VisitorException
    {
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTCst node, Object data) throws VisitorException
    {
        ObjectType cstType = (ObjectType) node.jjtGetChild(0).jjtAccept(this, data);
        String cstName = (String) node.jjtGetChild(1).jjtAccept(this, data);
        Object  cstValue =  node.jjtGetChild(2).jjtAccept(this, data);

       if (isDefined(cstName, cstType, ObjectNature.CST)){
        logger.logError("constant " + cstName +  " is already defined " );
   
        } else {
            MemoryObject mo = new MemoryObject(cstName, cstValue, ObjectNature.CST, cstType);
            try {
                ((HashTable) stack.getTop().getValue()).put(mo);
            } catch (StackException  | SymbolTableException e) {
                e.printStackTrace();
            }
        }
        return cstType;
        
    }
    
   @Override
    public Object visit(ASTVars node, Object data) throws VisitorException
   {
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTVar node, Object data) throws VisitorException
    {
        ASTIdent identNode = (ASTIdent) node.jjtGetChild(1);
        ObjectType varType = (ObjectType) node.jjtGetChild(0).jjtAccept(this, data);
        String varName = identNode.jjtGetValue().toString();
        ObjectType typeExp = getNodeType(node.jjtGetChild(2), data);

        if (typeExp != varType){
            logger.logError("Type mismatch: Cannot assign " + typeExp + " to " + identNode + " of type " +varType );
        }
        if (isDefined(varName, typeExp, ObjectNature.VAR)) {
             MemoryObject mp= lookupSymbol(varName); 
             if (mp.getType() == varType){
                logger.logError(" Variable " + varName + " is already defined with the same Type " + varType );
             }
        } 
        else {
            MemoryObject mo = new MemoryObject(varName, null, ObjectNature.VAR, varType);
            try {
                ((HashTable) stack.getTop().getValue()).put(mo);
            } catch (StackException  | SymbolTableException e) {
                e.printStackTrace();
            }
        }
        MemoryObject mo = new MemoryObject(varName, null, ObjectNature.VAR,varType);
        visitChildren(node, data);
        return mo;
    }


    @Override
    public Object visit(ASTTableau node, Object data) throws VisitorException
    {
        String arrayName = (String) node.jjtGetChild(1).jjtAccept(this, data);  // nom 
        ObjectType arrayType = (ObjectType) node.jjtGetChild(0).jjtAccept(this, data); // type 

        if (isDefined(arrayName, arrayType, ObjectNature.TAB)){
            logger.logError("Tableau " + arrayName + " is already defined !.");
        }
        else {
            MemoryObject mo = new MemoryObject(arrayName, null, ObjectNature.TAB, arrayType);
            try {
                ((HashTable) stack.getTop().getValue()).put(mo);
              } catch (StackException e) {
                e.printStackTrace();
            } catch (SymbolTableException e)
            {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    public Object visit(ASTOmega node, Object data) {
        return ObjectType.OMEGA;
    }

    @Override
    public Object visit(ASTMethode node, Object data) throws VisitorException
    {
        Object returnTypeObject = node.jjtGetChild(0).jjtAccept(this, data);
        if (!(returnTypeObject instanceof ObjectType)) {
            logger.logError("Expected ObjectType but found " + returnTypeObject.getClass().getSimpleName());
            return null; 
        }
        ObjectType returnType = (ObjectType) returnTypeObject;  
        System.out.println("returnType : " + returnType);

        String methodName = (String) node.jjtGetChild(1).jjtAccept(this, data);
        List<ObjectType> paramTypes = new ArrayList<>();

        // Récupérer les types des paramètres 
        if (node.jjtGetChild(2) instanceof ASTEntetes){
        ASTEntetes entetesNode = (ASTEntetes) node.jjtGetChild(2);
        collectParamTypes(entetesNode, paramTypes, data);
        }

        // Construire la signature de la méthode
        StringBuilder signatureBuilder = new StringBuilder(methodName);
        signatureBuilder.append("(");
        for (ObjectType paramType : paramTypes) {
            signatureBuilder.append(paramType.toString()).append(",");
        }
        if (!paramTypes.isEmpty()) {
            signatureBuilder.setLength(signatureBuilder.length() - 1); 
        }
        signatureBuilder.append(")");
        String methodSignature = signatureBuilder.toString();

        // Vérifier si la méthode avec cette signature est déjà définie
        if (isDefined(methodName, returnType, ObjectNature.METH)) {
            logger.logError("Method " + methodSignature + " is already defined ");
        } else {
            MemoryObject mo = new MemoryObject(methodSignature, null, ObjectNature.METH, returnType, paramTypes);
            try {
                ((HashTable) stack.getTop().getValue()).put(mo);
                } catch (StackException | SymbolTableException e) {
                e.printStackTrace();
            }
            currentMethod = methodSignature; 
        }

        // nouvelle portée pour les paramètres et les variables locales
        try {
            stack.push(new MemoryObject("scope", new HashTable(), ObjectNature.VAR, ObjectType.VOID));
            } catch (StackException e) {
            e.printStackTrace();
        }
        node.jjtGetChild(3).jjtAccept(this, data); // vars
        node.jjtGetChild(4).jjtAccept(this, data); // instrs

        // réinitialiser 
        try {
            stack.pop();
            } catch (StackException e) {
            e.printStackTrace();
        }
        currentMethod = null;
        return null;
    }

    private void collectParamTypes(ASTEntetes entetesNode, List<ObjectType> paramTypes, Object data) throws VisitorException
    {
        for (int i = 0; i < entetesNode.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) entetesNode.jjtGetChild(i);
            if (child instanceof ASTEntete) {
                ObjectType paramType = (ObjectType) child.jjtGetChild(0).jjtAccept(this, data);
                if (paramType == null) {
                    logger.logError("Parameter type cannot be null.");
                }else {
                paramTypes.add(paramType);
                }
            } else if (child instanceof ASTEntetes) {
                collectParamTypes((ASTEntetes) child, paramTypes, data);
            }
        }
    }

    @Override
public Object visit(ASTMain node, Object data) throws VisitorException
    {
    if (isDefined("main", ObjectType.VOID, ObjectNature.METH)) {
        logger.logError("Main method is already defined.");
        return null;
    }

    try {
        MemoryObject mainMemoryObject = new MemoryObject("main", "Methmain", ObjectNature.METH, ObjectType.VOID);
        stack.push(mainMemoryObject);
        currentMethod = "Methmain";
        visitChildren(node, data);
    } catch (StackException e) {
        logger.logError("Error pushing main method scope: " + e.getMessage());
        e.printStackTrace();
    } finally {
        try {
            stack.pop();
        } catch (StackException e) {
            logger.logError("Error popping main method scope: " + e.getMessage());
            e.printStackTrace();
        }
        currentMethod = null;
    }

    return null;
}

    @Override
    public Object visit(ASTEnil node, Object data) {
        return ObjectType.OMEGA;
    }

    @Override
    public Object visit(ASTEntetes node, Object data) throws VisitorException
    {
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTEntete node, Object data) throws VisitorException
    {
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTInil node, Object data) {
        return ObjectType.OMEGA;
    }

    @Override
    public Object visit(ASTInstrs node, Object data) throws VisitorException
    {
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTRetour node, Object data) throws VisitorException
    {
        return visitChildren(node, data);
    }


    @Override
    public Object visit(ASTEcrire node, Object data) throws VisitorException
    {
        return node.jjtGetChild(0).jjtAccept(this, data);
    }

    @Override
    public Object visit(ASTEcrireLn node, Object data) throws VisitorException
    {
        return node.jjtGetChild(0).jjtAccept(this, data);
    }

    @Override
    public Object visit(ASTSi node, Object data) throws VisitorException
    {
        ObjectType conditionType = (ObjectType) node.jjtGetChild(0).jjtAccept(this, data);
        if (conditionType != ObjectType.BOOLEAN) {
            logger.logError("Condition in if statement must be boolean.");
        }
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTTantQue node, Object data) throws VisitorException
    {
        ObjectType conditionType = (ObjectType) node.jjtGetChild(0).jjtAccept(this, data);
        if (conditionType != ObjectType.BOOLEAN) {
            logger.logError("Condition in while statement must be boolean.");
        }
        return visitChildren(node, data);
    }
    
    @Override
    public Object visit(ASTAffectation node, Object data) throws VisitorException
    {
        String identName = (String)  node.jjtGetChild(0).jjtAccept(this, data);
        
        ObjectType assignedType = (ObjectType) node.jjtGetChild(1).jjtAccept(this, data);

        MemoryObject mo = lookupSymbol(identName);
        if (mo == null) {
            logger.logError("Variable " + identName + " is not defined.");
        }
        // Vérifier que les types correspondent
        if (assignedType != mo.getType()) {
            logger.logError("Type mismatch: Cannot assign " + assignedType + " to " + identName + " of type " + mo.getType());
        }
        Object value = node.jjtGetChild(1).jjtAccept(this, data);
        MemoryObject mo2 = new MemoryObject(identName,value, ObjectNature.VAR, assignedType);

        return mo2;
    }

    @Override
    public Object visit(ASTIncrement node, Object data) throws VisitorException
    {
        String varName = (String) node.jjtGetChild(0).jjtAccept(this, data); //  nom de la variable
        MemoryObject mo = lookupSymbol(varName); 

        
        if (mo == null) {
            logger.logError("Variable " + varName + " is not defined.");
        }
        if ( mo.getType() != ObjectType.INT) {
            logger.logError("Type mismatch: Cannot increment non-integer variable");
        }
        return ObjectType.INT;
    }

    @Override
    public Object visit(ASTAppelI node, Object data) throws VisitorException
    {
        String methodName = (String) node.jjtGetChild(0).jjtAccept(this, data);
        List<ObjectType> actualParamTypes = new ArrayList<>();

        // Construire la signature de la méthode à partir des paramètres
        Object paramsData = node.jjtGetChild(1);
        if ((paramsData instanceof ASTListExp)) {
            ASTListExp paramsNode = (ASTListExp) paramsData;
            collectParamTypesFromListexp(paramsNode, actualParamTypes, data);
        }
    
        // Construire la signature complète de la méthode
        StringBuilder signatureBuilder = new StringBuilder(methodName);
        signatureBuilder.append("(");
        for (ObjectType paramType : actualParamTypes) {
            signatureBuilder.append(paramType.toString()).append(",");
        }
        if (!actualParamTypes.isEmpty()) {
            signatureBuilder.setLength(signatureBuilder.length() - 1); // Supprimer la dernière virgule
        }
        signatureBuilder.append(")");
        String methodSignature = signatureBuilder.toString();
    
        // Rechercher la méthode avec la signature complète
        MemoryObject mo = lookupSymbol(methodSignature);
        if (mo == null) {
            logger.logError("Method " + methodSignature + " is not defined.");
            return null;
        }
    
        // Vérifier les types des paramètres
        List<ObjectType> expectedParamTypes = mo.getParamTypes();
        if (expectedParamTypes.size() != actualParamTypes.size()) {
            logger.logError("Parameter count mismatch in method " + methodSignature);
            return null;
        }
    
        for (int i = 0; i < expectedParamTypes.size(); i++) {
            if (expectedParamTypes.get(i) != actualParamTypes.get(i)) {
                logger.logError("Parameter type mismatch in method "  + ": expected " + expectedParamTypes.get(i) + 
                " but got " + actualParamTypes.get(i) + methodSignature);
            }
        }
    
        return mo.getType();
    }
    
    @Override
    public Object visit(ASTListExp node, Object data) throws VisitorException
    {

        if (node.jjtGetNumChildren() == 2) {
            SimpleNode firstChild = (SimpleNode) node.jjtGetChild(0);
            SimpleNode secondChild = (SimpleNode) node.jjtGetChild(1);
            if (firstChild instanceof ASTExp) {
                ObjectType paramType = (ObjectType) firstChild.jjtAccept(this, data);
                if (paramType == null) {
                    logger.logError("Parameter type cannot be null.");
                }
            }else {
                logger.logError("Unexpected node type in ASTListExp: " + firstChild.getClass().getSimpleName());
            }
            if (secondChild instanceof ASTListExp) {
                return secondChild.jjtAccept(this, data);
            } else if (!(secondChild instanceof ASTExnil)) {
                logger.logError("Unexpected node type in ASTListExp: " + firstChild.getClass().getSimpleName());
            }
        } else if (node.jjtGetNumChildren() == 1) {
            if (node.jjtGetChild(0) instanceof ASTExnil) {
                return ObjectType.OMEGA;
            }
        }
        return null;
    }

    @Override
    public Object visit(ASTExp node, Object data) throws VisitorException
    {
        Object result = null;
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            result = node.jjtGetChild(i).jjtAccept(this, data);
        }
        return result;
    }
    
    
    private void collectParamTypesFromListexp(ASTListExp listexpNode, List<ObjectType> paramTypes, Object data) throws VisitorException
    {
        if (listexpNode.jjtGetNumChildren() == 2) {
            SimpleNode firstChild = (SimpleNode) listexpNode.jjtGetChild(0);
            SimpleNode secondChild = (SimpleNode) listexpNode.jjtGetChild(1);
    
            if (firstChild instanceof ASTExp) {
                ObjectType paramType = (ObjectType) firstChild.jjtAccept(this, data);
                if (paramType == null) {
                    logger.logError("Parameter type cannot be null.");
                }else {
                paramTypes.add(paramType);
                }
            }
    
            if (secondChild instanceof ASTListExp) {
                collectParamTypesFromListexp((ASTListExp) secondChild, paramTypes, data);
            } else if (!(secondChild instanceof ASTExnil)) {
                logger.logError("Unexpected node type in ASTListExp: " + secondChild.getClass().getSimpleName());
            }
        } 
        else if (listexpNode.jjtGetNumChildren() == 1) {
            if (listexpNode.jjtGetChild(0) instanceof ASTExnil) {
                return;
            }

        } 
    }
    @Override
    public Object visit(ASTAppelE node, Object data) throws VisitorException
    {
        String methodName = (String) node.jjtGetChild(0).jjtAccept(this, data);

        // Construire la signature de la méthode à partir des paramètres
        Object paramsData = node.jjtGetChild(1);
        List<ObjectType> actualParamTypes = new ArrayList<>();

        if (paramsData instanceof ASTListExp){
            ASTListExp paramsNode = (ASTListExp) paramsData;
            collectParamTypesFromListexp(paramsNode, actualParamTypes, data);
        }
    
        // Construire la signature complète de la méthode
        StringBuilder signatureBuilder = new StringBuilder(methodName);
        signatureBuilder.append("(");
        for (ObjectType paramType : actualParamTypes) {
            signatureBuilder.append(paramType.toString()).append(",");
        }
        if (!actualParamTypes.isEmpty()) {
            signatureBuilder.setLength(signatureBuilder.length() - 1); // Supprimer la dernière virgule
        }
        signatureBuilder.append(")");
        String methodSignature = signatureBuilder.toString();
    
        // Rechercher la méthode avec la signature complète
        MemoryObject mo = lookupSymbol(methodSignature);
        if (mo == null) {
            logger.logError("Method " + methodSignature + " is not defined.");
            return null;
        }
    
        // Vérifier les types des paramètres
        List<ObjectType> expectedParamTypes = mo.getParamTypes();
        if (expectedParamTypes.size() != actualParamTypes.size()) {
            logger.logError("Parameter count mismatch in method " + methodSignature);
            return null;
        }
    
        for (int i = 0; i < expectedParamTypes.size(); i++) {
            if (expectedParamTypes.get(i) != actualParamTypes.get(i)) {
                logger.logError("Parameter type mismatch in method "  + ": expected " + expectedParamTypes.get(i) + 
                " but got " + actualParamTypes.get(i) + methodSignature);
                return null;
            }
        }
    
        return mo.getType();
        
    }

    @Override
    public Object visit(ASTExnil node, Object data) {
        return ObjectType.OMEGA;
    }

    @Override
    public Object visit(ASTNot node, Object data) throws VisitorException
    {
        return visitUnaryOperation(node, data, ObjectType.BOOLEAN);
    }

    @Override
    public Object visit(ASTNeg node, Object data) throws VisitorException
    {
        return visitUnaryOperation(node, data, ObjectType.INT);
    }

    @Override
    public Object visit(ASTEt node, Object data) throws VisitorException
    {
        return visitBinaryOperation(node, data, ObjectType.BOOLEAN);
    }

    @Override
    public Object visit(ASTOu node, Object data) throws VisitorException
    {
        return visitBinaryOperation(node, data, ObjectType.BOOLEAN);
    }

    @Override
    public Object visit(ASTEq node, Object data) throws VisitorException
    {

        ObjectType leftType = (ObjectType) node.jjtGetChild(0).jjtAccept(this, data);
        ObjectType rightType = (ObjectType) node.jjtGetChild(1).jjtAccept(this, data);
        
        if (leftType != rightType) {
            logger.logError("Type mismatch: Expected same type (Boolean) for both operands for equivalence expression.");
        }
        return ObjectType.BOOLEAN;
    }

    @Override
    public Object visit(ASTSup node, Object data) throws VisitorException
    {

        ObjectType leftType = (ObjectType) node.jjtGetChild(0).jjtAccept(this, data);
        ObjectType rightType = (ObjectType) node.jjtGetChild(1).jjtAccept(this, data);
        
        if (leftType != rightType && leftType != ObjectType.BOOLEAN && rightType != ObjectType.BOOLEAN) {
            logger.logError("Type mismatch: Expected  type Boolean for both operands.");
        }
        return ObjectType.BOOLEAN;
    }

    @Override
    public Object visit(ASTAdd node, Object data) throws VisitorException
    {
        return visitBinaryOperation(node, data, ObjectType.INT);
    }

    @Override
    public Object visit(ASTSub node, Object data) throws VisitorException
    {
        return visitBinaryOperation(node, data, ObjectType.INT);
    }

    @Override
    public Object visit(ASTMul node, Object data) throws VisitorException
    {
        return visitBinaryOperation(node, data, ObjectType.INT);
    }

    @Override
    public Object visit(ASTSomme node, Object data) throws VisitorException
    {
        return visitBinaryOperation(node, data, ObjectType.INT);
    }

    @Override
    public Object visit(ASTDiv node, Object data) throws VisitorException
    {

        Object value = node.jjtGetChild(1).jjtAccept(this, data);

        if (value instanceof Integer) {
            int rightValue = (Integer) value;
            if (rightValue == 0) {
                logger.logError("Error : not allowed to divide by zero !");
            }
        } 
        else {
            logger.logError("Expected an integer value, but got: " + value);
        }
        return visitBinaryOperation(node, data, ObjectType.INT);
    }

    @Override
    public Object visit(ASTLongeur node, Object data) {

        ASTIdent identNode = (ASTIdent) node.jjtGetChild(0);
        Object value = identNode.jjtGetValue();
        if (value instanceof String) {
            return ObjectType.INT; //  le type INT pour la longueur d'une chaîne
        } else {
            logger.logError("Expected a string identifier, but got: " + value);
        }

        return null;
    }

    @Override
    public Object visit(ASTVrai node, Object data) {
        return ObjectType.BOOLEAN;
    }

    @Override
    public Object visit(ASTFaux node, Object data) {
        return ObjectType.BOOLEAN;
    }


    @Override
    public Object visit(ASTTab node, Object data) throws VisitorException
    {
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTRien node, Object data) {
        return ObjectType.VOID;
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

        Object value = node.jjtGetValue();
        if (value instanceof Integer) {
            return ObjectType.INT;
        } else {
            logger.logError("Expected an integer value, but got: " + value);
        }
        return null; 
    }

    @Override
    public Object visit(ASTChaine node, Object data) {    
        Object value = node.jjtGetValue();
        if (value instanceof String) {
            String chaine = (String) value;
            return chaine ; 
        } else {
            logger.logError("Expected a string chaine, but got: " + value);
        }
        
        return null ; 
    }

    private ObjectType visitBinaryOperation(SimpleNode node, Object data, ObjectType expectedType) throws VisitorException
    {
        
        ObjectType leftType = getNodeType(node.jjtGetChild(0), data);
        ObjectType rightType = getNodeType(node.jjtGetChild(1), data);
    
        if (leftType != expectedType || rightType != expectedType) {
            logger.logError("Type mismatch: Expected " + expectedType + " for both operands.");
        }
        return expectedType;
    }
    
    private ObjectType getNodeType(Node node, Object data) throws VisitorException
    {
        if (node instanceof ASTIdent || node instanceof  ASTVar || node instanceof ASTCst || node instanceof ASTTableau ) {
            String varName = (String) node.jjtAccept(this, data);
            MemoryObject memoryObject = lookupSymbol(varName);
            if (memoryObject != null) {
                return memoryObject.getType();
            } else {
                logger.logError("Variable " + varName + " is not defined.");
            }
        } else if (node instanceof ASTNbre) {
            return ObjectType.INT;
        } else if (node instanceof ASTTab){
            String varName = (String) node.jjtGetChild(0).jjtAccept(this, data);
            MemoryObject memoryObject = lookupSymbol(varName);
            if (memoryObject != null) {
                return memoryObject.getType();
            }
        }
            else if (node instanceof ASTVrai || node instanceof ASTFaux) {
            return ObjectType.BOOLEAN;
        } 
        else {
            return (ObjectType) node.jjtAccept(this, data);
        }
        return null;
    }


    private ObjectType visitUnaryOperation(SimpleNode node, Object data, ObjectType expectedType) throws VisitorException
    {
       
        ObjectType operandType = (ObjectType) node.jjtGetChild(0).jjtAccept(this, data);

        if (operandType != expectedType) {
            logger.logError("Type mismatch: Expected " + expectedType + " for operand.");
        }
        return expectedType;
    }

    private Object visitChildren(SimpleNode node, Object data) throws VisitorException
    {
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            node.jjtGetChild(i).jjtAccept(this, data);
        }
        return null;
    }

    public MemoryObject lookupSymbol(String name) {
        try {
            if (stack.empty()) {
                throw new StackException("The stack is empty, cannot lookup symbol");
            }
            HashTable symbolTable = (HashTable) stack.getTop().getValue();
            
            return symbolTable.get(name);
            } catch (StackException e) {
            System.err.println("Error: " + e.getMessage());
            return null; 
        }
    }
    
    public Boolean isDefined(String name,ObjectType type,ObjectNature nat){
        MemoryObject mo ; 
        if (lookupSymbol(name)!=null){
            mo= lookupSymbol(name);
                if (type == mo.getType() && nat == mo.getNature()) {
                return true ; 
            }
        }
        return false;
    }
    
    public List<MemoryObject> lookupMethode() {        
        List<MemoryObject> methods = new ArrayList<>();
        try {
            if (stack.empty()) {
                throw new StackException("The stack is empty, cannot lookup symbol");
            }

            HashTable symbolTable = (HashTable) stack.getTop().getValue();

            for (List<MemoryObject> bucket : symbolTable.getBuckets()) {
                if (bucket != null) {
                    for (MemoryObject mo : bucket) {
                        if (mo.getNature() == ObjectNature.METH || mo.getNature() == ObjectNature.VAR || mo.getNature() == ObjectNature.TAB)  {
                            methods.add(mo);
                        }
                    }
                }
            }
        } catch (StackException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return methods;
    }
    @Override
    public Object visit(SimpleNode node, Object data) throws VisitorException
    {
        visitChildren(node,null);
        return node.jjtAccept(this, data);
    }
}
