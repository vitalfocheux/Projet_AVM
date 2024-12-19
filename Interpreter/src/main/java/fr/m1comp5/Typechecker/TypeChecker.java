package fr.m1comp5.Typechecker;

import fr.m1comp5.*;
import fr.m1comp5.custom.exception.VisitorException;
import fr.m1comp5.mjj.generated.*;
import java.util.ArrayList;
import java.util.List;
import fr.m1comp5.Logger.AppLogger;

public class TypeChecker implements MiniJajaVisitor {

    private String currentMethod = null; // Méthode courante
    private AppLogger logger = AppLogger.getInstance(); // Logger pour les messages d'erreur de TypeChecker
    private SymbolTable symbolTable = null;

    public TypeChecker() {
        symbolTable = new SymbolTable(); // Initialiser la table des symboles globale
    }

    @Override
    public Object visit(ASTRoot node, Object data) throws VisitorException
    {
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTClasse node, Object data) throws VisitorException {

        ASTIdent classNameNode = (ASTIdent) node.jjtGetChild(0);
        String className = (String) classNameNode.jjtGetValue();
        currentMethod = className;
        symbolTable.newScope();
        try {
            MemoryObject classMemoryObject = new MemoryObject(className, "Class" + className, ObjectNature.VAR,
                    ObjectType.OMEGA);
            symbolTable.putObjectInCurrentScope(classMemoryObject);
        } catch (  SymbolTableException e) {
            logger.logError("TypeChecker : Error adding class to symbol table: " + e.getMessage(), node.getLine(),
                    node.getColumn());
            e.printStackTrace();
        }

        // Visite des déclarations et méthodes dans la classe
        for (int i = 1; i < node.jjtGetNumChildren(); i++) {
            node.jjtGetChild(i).jjtAccept(this, data);
        }

        try {
            MemoryObject mo = symbolTable.get(className);
            symbolTable.removeObjectFromCurrentScope(mo);
            symbolTable.popScope();
        } catch (SymbolTableException  e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public Object visit(ASTIdent node, Object data) throws VisitorException {

        Object value = node.jjtGetValue();

        if (value instanceof String) {
            String identv = (String) value;
            if (identv.matches("[a-zA-Z][a-zA-Z0-9]*")) {
                return identv;
            } else {

                logger.logError("TypeChecker  : Invalid identifier: " + identv
                                + ". Identifiers must start with a letter and contain only letters and digits.", node.getLine(),
                        node.getColumn());
            }
        } else {
            logger.logError("TypeChecker  : Expected a string identifier, but got: " + value, node.getLine(),
                    node.getColumn());
        }
        return null;
    }

    @Override
    public Object visit(ASTVnil node, Object data) throws VisitorException {
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
        Object cstType = node.jjtGetChild(0).jjtAccept(this, data);
        if (!(cstType instanceof ObjectType))
        {
            logger.logError("Expected ObjectType during decl cst  but got " + cstType.getClass().getSimpleName(),
                    node.getLine(), node.getColumn());
            return null;
        }
        ObjectType cstTypeObject = (ObjectType) cstType;
        String cstName = (String) ((ASTIdent) node.jjtGetChild(1)).jjtGetValue();
        Object cstValue = null;
        if (node.jjtGetNumChildren() > 2)
        {
            cstValue = node.jjtGetChild(2).jjtAccept(this, data);
        }
        if (lookupSymbol(cstName) != null)
        {
            logger.logError("TypeChecker  : Constant " + cstName + " is already defined.", node.getLine(),
                    node.getColumn());
            return null;
        }
        MemoryObject mo;
        try {
            if (cstValue == null || cstValue == ObjectType.OMEGA)
                mo = new MemoryObject(cstName, null, ObjectNature.VCST, cstTypeObject);
            else
                mo = new MemoryObject(cstName, cstValue, ObjectNature.CST, cstTypeObject);
            symbolTable.putObjectInCurrentScope(mo);
        } catch (SymbolTableException  e) {
            e.printStackTrace();
        }

        return cstTypeObject;

    }

    @Override
    public Object visit(ASTVars node, Object data) throws VisitorException
    {
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTVar node, Object data) throws VisitorException
    {   logger.logInfo("en astvar declaration ");
        ASTIdent identNode = (ASTIdent) node.jjtGetChild(1);
        String varName = identNode.jjtGetValue().toString();

        Object varType = node.jjtGetChild(0).jjtAccept(this, data);
        if (!(varType instanceof ObjectType))
        {
            logger.logError(
                    "TypeChecker  : Expected ObjectType during decl var but got " + varType.getClass().getSimpleName(),
                    node.getLine(), node.getColumn());
            return null;
        }
        if (lookupSymbol(varName) != null)
        {
            logger.logError("TypeChecker  : Variable " + varName + " is already defined.", node.getLine(),
                    node.getColumn());
            return null;
        }
        ObjectType varTypev = (ObjectType) varType;

        logger.logInfo("le type de variable est " + varTypev);

        // Vérifiez si une expression d'initialisation est présente
        if (node.jjtGetNumChildren() > 2)
        {
            ObjectType typeExp = getNodeType(node.jjtGetChild(2), data);
            if (typeExp == null || typeExp == ObjectType.OMEGA)
            {
                MemoryObject mo = new MemoryObject(varName, null, ObjectNature.VAR, varTypev);
                try
                {
                    symbolTable.putObjectInCurrentScope(mo);
                }
                catch (SymbolTableException  e) {
                    e.printStackTrace();
                }
                return varTypev;
            }
            if (!(typeExp instanceof ObjectType))
            {
                logger.logError(
                        "TypeChecker  : Expected ObjectType during decl var but got "
                                + typeExp.getClass().getSimpleName(),
                        node.getLine(), node.getColumn());
                return null;
            }

            if (typeExp != varType)
            {
                logger.logError(
                        "TypeChecker  : Type mismatch Cannot assign " + typeExp + " to " + identNode + " of type "
                                + varType,
                        node.getLine(), node.getColumn());
            }

            MemoryObject mo = new MemoryObject(varName, null, ObjectNature.VAR, varTypev);
            try
            {
                symbolTable.putObjectInCurrentScope(mo);
            }
            catch (SymbolTableException  e) {
                e.printStackTrace();
            }
        }
        else
        {
            // Déclarez simplement la variable sans initialisation
            MemoryObject mo = new MemoryObject(varName, null, ObjectNature.VAR, varTypev);
            try
            {
                symbolTable.putObjectInCurrentScope(mo);
            }
            catch (SymbolTableException  e) {
                e.printStackTrace();
            }
        }

        visitChildren(node, data);
        return varTypev;
    }

    @Override
    public Object visit(ASTTableau node, Object data) throws VisitorException
    {
        String arrayName = (String) node.jjtGetChild(1).jjtAccept(this, data);
        // vérifier le type du tableau
        Object arrayTypep = node.jjtGetChild(0).jjtAccept(this, data);
        if (!(arrayTypep instanceof ObjectType))
        {
            logger.logError(
                    "TypeChecker  : Expected ObjectType for decl tableau but got "
                            + arrayTypep.getClass().getSimpleName(),
                    node.getLine(), node.getColumn());
            return null;
        }
        ObjectType arrayType = (ObjectType) arrayTypep;
        // verifie type de l'index
        ObjectType IndextypeExp = getNodeType(node.jjtGetChild(2), data);
        logger.logInfo("indice de tableau de type " + IndextypeExp);

        if (IndextypeExp == null)
        {
            logger.logError("TypeChecker  : Index type cannot be null.", node.getLine(), node.getColumn());
            return null;
        }
        else if (IndextypeExp != ObjectType.INT) {
            logger.logError("TypeChecker  : Index must be of type int.", node.getLine(), node.getColumn());
            return null;
        }
        if (lookupSymbol(arrayName) != null)
        {
            logger.logError("TypeChecker  : Tableau " + arrayName + " is already defined during declaration.", node.getLine(),
                    node.getColumn());
            return null;
        }

        if (isDefined(arrayName, arrayType, ObjectNature.TAB))
        {
            logger.logError("TypeChecker  : Tableau " + arrayName + " is already defined.", node.getLine(),
                    node.getColumn());
        } else
        {
            MemoryObject mo = new MemoryObject(arrayName, null, ObjectNature.TAB, arrayType);
            logger.logInfo("le tableau " + arrayName );
            try {
                symbolTable.putObjectInCurrentScope(mo);
            }
            catch (SymbolTableException  e)
            {
                e.printStackTrace();
            }
        }

        return arrayType;
    }

    @Override
    public Object visit(ASTOmega node, Object data)
    {
        return ObjectType.OMEGA;
    }

    @Override
    public Object visit(ASTMethode node, Object data) throws VisitorException
    {
        Object returnTypeObject = node.jjtGetChild(0).jjtAccept(this, data);


        if (!(returnTypeObject instanceof ObjectType))
        {
            logger.logError(
                    "TypeChecker  : Expected type valide but found " + returnTypeObject,
                    node.getLine(), node.getColumn());
            return null;
        }

        ObjectType returnType = (ObjectType) returnTypeObject;
        String methodName = (String) node.jjtGetChild(1).jjtAccept(this, data);
        List<ObjectType> paramTypes = new ArrayList<>();
        List<String> paramNames = new ArrayList<>();

        // Récupérer les types des paramètres
        if (node.jjtGetChild(2) instanceof ASTEntetes)
        {
            ASTEntetes entetesNode = (ASTEntetes) node.jjtGetChild(2);
            collectParamTypesName(entetesNode, paramTypes, paramNames, data);
            logger.logInfo("TypeChecker : Method " + methodName + " has " + paramTypes.size() + " parameters.");
        }
        else if (node.jjtGetChild(2) instanceof ASTEnil) {
            logger.logInfo("TypeChecker : Method " + methodName + " has no parameters.");
        }

        // Construire la signature de la méthode
        MethodSignature methodSignature = new MethodSignature(methodName, paramTypes);
        currentMethod = methodSignature.toString();

        if (lookupSymbol(currentMethod)!= null ){
            logger.logError("TypeChecker  : Method " + methodName + " is already defined.", node.getLine(),
                    node.getColumn());
            return null;
        }

        // Vérifier si la méthode avec cette signature est déjà définie
        if (isDefinedMeth(methodSignature, returnType, ObjectNature.METH)) {
            logger.logError("TypeChecker  : declaration Method " + methodSignature + " is already defined.",
                    node.getLine(),
                    node.getColumn());

        }
        else {
            try {
                if (paramTypes.size() == 0) {
                    MemoryObject mo = new MemoryObject(methodSignature.toString(), null, ObjectNature.METH, returnType,
                            null);
                    symbolTable.putObjectInCurrentScope(mo);
                    logger.logInfo(methodSignature.toString() + mo.toString());
                    logger.logInfo("TypeChecker : Adding method " + methodSignature + " to symbol table.");
                } else {
                    MemoryObject mo = new MemoryObject(methodSignature.toString(), null, ObjectNature.METH, returnType,
                            paramTypes);
                    logger.logInfo("TypeChecker : Adding method " + methodSignature + " to symbol table.");
                    symbolTable.putObjectInCurrentScope(mo);
                }
            } catch (SymbolTableException  e) {
                e.printStackTrace();
            }
            currentMethod = methodSignature.toString();

            // Ajouter les paramètres dans la table des symboles
            symbolTable.newScope();

            for (int i = 0; i < paramNames.size(); i++) {
                String paramName = paramNames.get(i);
                ObjectType paramType = paramTypes.get(i);
                MemoryObject paramObject = new MemoryObject(paramName, null, ObjectNature.VAR, paramType);
                try
                {
                    symbolTable.putObjectInCurrentScope(paramObject);
                }
                catch (SymbolTableException  e) {
                    e.printStackTrace();
                }
            }

            node.jjtGetChild(3).jjtAccept(this, data); // vars
            node.jjtGetChild(4).jjtAccept(this, data); // instrs

            // réinitialiser
            try
            {
                // MemoryObject mo = symbolTable.get(currentMethod);
                //stack.eraseVariable(currentMethod);
                //symbolTable.removeObjectFromCurrentScope();
                symbolTable.popScope();
            }
            catch (SymbolTableException  e) {
                e.printStackTrace();
            }
            currentMethod = null;
        }
        return null;

    }

    @Override
    public Object visit(ASTMain node, Object data) throws VisitorException {

        currentMethod = "main";
        symbolTable.newScope();

        visitChildren(node, data);
        try
        {
            symbolTable.popScope();
        }
        catch (Exception e) {
            logger.logError("TypeChecker : Error popping main method scope: " + e.getMessage(), node.getLine(),
                    node.getColumn());
            e.printStackTrace();
        }

        currentMethod = null;

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
        Object ob =  node.jjtGetChild(0).jjtAccept(this, data);
        if (ob != null){
            if (ob instanceof ObjectType){
                return ob;
            }else
            {
                return ObjectType.OMEGA;
            }
        } else {
            return ObjectType.OMEGA;
        }
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
            logger.logError("TypeChecker  : Condition in if statement must be boolean.", node.getLine(),
                    node.getColumn());
        }
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTTantQue node, Object data) throws VisitorException
    {
        ObjectType conditionType = (ObjectType) node.jjtGetChild(0).jjtAccept(this, data);
        logger.logInfo("le type d'exp de while est "+ conditionType);
        if (conditionType != ObjectType.BOOLEAN) {
            logger.logError("TypeChecker  : Condition in while statement must be boolean.", node.getLine(),
                    node.getColumn());
        }
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTAffectation node, Object data)  throws VisitorException
    {
        ObjectType id = getNodeType(node.jjtGetChild(0), data);
        ObjectType id2 = getNodeType(node.jjtGetChild(1), data);
        if (id == null) {
            return null;
        }
        if (id2 == null) {
            return null;
        }
        if (id != id2) {
            logger.logError("TypeChecker  : Type mismatch Cannot assign " + id2 + " to " + id, node.getLine(),
                    node.getColumn());
        }

        return id;
    }

    @Override
    public Object visit(ASTIncrement node, Object data)  throws VisitorException
    {
        String varName = (String) node.jjtGetChild(0).jjtAccept(this, data); // nom de la variable

        MemoryObject mo = lookupSymbolidMeth(varName);

        if (mo == null) {
            logger.logError("TypeChecker  : Variable " + varName + " is not defined.", node.getLine(),
                    node.getColumn());
        }
        if (mo.getType() != ObjectType.INT) {
            logger.logError("TypeChecker : Type mismatch Cannot increment non-integer variable.", node.getLine(),
                    node.getColumn());
        }
        return ObjectType.INT;
    }

    @Override
    public Object visit(ASTAppelI node, Object data)  throws VisitorException
    {
        return visitMethodCall(node, data);
    }

    @Override
    public Object visit(ASTAppelE node, Object data)  throws VisitorException
    {
        return visitMethodCall(node, data);
    }


    private Object visitMethodCall(SimpleNode node, Object data) throws VisitorException {
        String methodName = (String) node.jjtGetChild(0).jjtAccept(this, data);
        List<ObjectType> actualParamTypes = new ArrayList<>();

        // Construire la signature de la méthode à partir des paramètres
        Object paramsData = node.jjtGetChild(1);

        if (paramsData instanceof ASTListExp)
        {
            ASTListExp paramsNode = (ASTListExp) paramsData;
            collectParamTypesFromListexp(paramsNode, actualParamTypes, data);
            logger.logInfo("TypeChecker : Method in appel  " + methodName + " has " + actualParamTypes.size()
                    + " parameters.");
        }
        else if (paramsData instanceof ASTExp)
        {
            ObjectType paramType = (ObjectType) paramsData;
            actualParamTypes.add(paramType);
            logger.logInfo("TypeChecker : Method in appel " + methodName + " has 1 parameter.");
        }
        else if (paramsData instanceof ASTExnil)
        {
            logger.logInfo("TypeChecker : Method in appel " + methodName + " has no parameters.");
        }

        // Construire la signature complète de la méthode
        MethodSignature methodSignature = new MethodSignature(methodName, actualParamTypes);
        logger.logInfo("TypeChecker : Method signature check signature " + methodSignature);

        // Rechercher la méthode avec la signature complète
        MemoryObject mo = lookupMethode(methodSignature.toString());

        if (mo == null) {
            logger.logError("TypeChecker  : Method " + methodSignature + " is not defined.",  node.jjtGetLastToken().beginLine, node.jjtGetLastToken().beginColumn);
            return null;

        }
        // appel methode avec entetes vide
        if (mo.getParamTypes() == null)
        {
            return mo.getType();
        }
        // Vérifier les types des paramètres
        List<ObjectType> expectedParamTypes = mo.getParamTypes();
        if (expectedParamTypes.size() != actualParamTypes.size()) {
            logger.logError("TypeChecker : Parameter count mismatch in method " + methodSignature,  node.jjtGetLastToken().beginLine, node.jjtGetLastToken().beginColumn);
            return null;
        }

        for (int i = 0; i < expectedParamTypes.size(); i++) {
            if (expectedParamTypes.get(i) != actualParamTypes.get(i)) {
                logger.logError("TypeChecker : Parameter type mismatch in method " + methodSignature + ": expected "
                        + expectedParamTypes.get(i) + " but got " + actualParamTypes.get(i),  node.jjtGetLastToken().beginLine, node.jjtGetLastToken().beginColumn);
                return null;
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

            if (firstChild instanceof ASTExp)
            {
                Object paramType = firstChild.jjtAccept(this, data);
                if (paramType == null)
                {
                    logger.logError("TypeChecker : Parameter type cannot be null.", node.getLine(), node.getColumn());
                }

            }
            else {
                ObjectType paramType = (ObjectType) firstChild.jjtAccept(this, data);
                return paramType;
            }

            if (secondChild instanceof ASTListExp) {
                return secondChild.jjtAccept(this, data);
            }
            else if (!(secondChild instanceof ASTExnil))
            {
                logger.logError(
                        "TypeChecker : Unexpected node type in ASTListExp: " + firstChild.getClass().getSimpleName(),
                        node.getLine(), node.getColumn());
            }
        }
        else if (node.jjtGetNumChildren() == 1) {
            if (node.jjtGetChild(0) instanceof ASTExnil)
            {
                return ObjectType.OMEGA;
            } else if (node.jjtGetChild(0) instanceof ASTExp) {
                return node.jjtGetChild(0).jjtAccept(this, data);
            }
        }
        return null;
    }

    @Override
    public Object visit(ASTExp node, Object data)  throws VisitorException{
        return node.jjtGetChild(0).jjtAccept(this, data);
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

        logger.logInfo("en visite de asteq");
        ObjectType leftType = getNodeType(node.jjtGetChild(0), data);
        ObjectType rightType = getNodeType(node.jjtGetChild(1), data);

        logger.logInfo("le type de right et left est " + leftType + rightType);
        if (!(leftType instanceof ObjectType) && !(leftType instanceof ObjectType)){
            logger.logError(  "TypeChecker : Type mismatch Expected Type Valide (int or boolean) for both operands for equivalence expression.",
                    node.getLine(), node.getColumn());
        }
        return ObjectType.BOOLEAN;
    }

    @Override
    public Object visit(ASTSup node, Object data) throws VisitorException
    {

        ObjectType leftType = getNodeType(node.jjtGetChild(0), data);
        ObjectType rightType = getNodeType(node.jjtGetChild(1), data);
        logger.logInfo("type de leftt type et right type de astsup " + leftType + " " +  rightType);

        if (leftType != ObjectType.INT || rightType != ObjectType.INT ){
            logger.logError("TypeChecker : Type mismatch. Expected type 'int' for both operands during '>' :'superior' operation.", node.getLine(), node.getColumn());
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
    public Object visit(ASTDiv node, Object data)  throws VisitorException{
        Object value = node.jjtGetChild(1);

        Object v = ((SimpleNode) value).jjtGetValue();
        logger.logInfo("TypeChecker : Value of right child in division: " + v);

        if (v instanceof Integer)
        {
            int rightValue = (Integer) v;
            if (rightValue == 0) {
                logger.logError("TypeChecker  :  not allowed to divide by zero!", node.getLine(), node.getColumn());
                //return null ;
            }
        }
        if (value instanceof ASTIdent || value instanceof ASTTab || value instanceof ASTAppelE
                || value instanceof ASTLongeur || value instanceof ASTExp)
        {
            ObjectType rightType = getNodeType(node.jjtGetChild(1), data);
            ObjectType leftType = getNodeType(node.jjtGetChild(0), data);
            if (rightType != ObjectType.INT || leftType != ObjectType.INT)
            {
                logger.logError("TypeChecker  : Expected type int for division operation.", node.getLine(),
                        node.getColumn());
            }
            if (rightType == ObjectType.INT) {
                if (isPotentiallyZero(node.jjtGetChild(1), data)) {
                    logger.logError(
                            "TypeChecker  : Expected type int for division operation.",
                            node.getLine(), node.getColumn()
                    );
                }

            }
        }
        return visitBinaryOperation(node, data, ObjectType.INT);
    }
    private boolean isPotentiallyZero(Node node, Object data) {
        if (node instanceof ASTIdent) {
            String ident = (String) ((ASTIdent) node).jjtGetValue();
            MemoryObject mo = lookupSymbol(ident);
            if (mo.getValue() != null || mo.getValue() instanceof Integer) {
                int value = (Integer) mo.getValue();
                return value == 0;

            }
        }
        return false;
    }
    @Override
    public Object visit(ASTLongeur node, Object data)
    {

        ASTIdent identNode = (ASTIdent) node.jjtGetChild(0);
        if (identNode ==  null){
            logger.logError("Expected an identifier while longueur operation",node.getLine(),node.getColumn());
        }


        return ObjectType.INT;
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
    public Object visit(ASTTab node, Object data)  throws VisitorException
    {
        String id = (String) ((ASTIdent) node.jjtGetChild(0)).jjtGetValue();
        Object indx = getNodeType(node.jjtGetChild(1), data);

        MemoryObject mo = lookupSymbolidMeth(id);
        if (mo == null)
        {
            logger.logError("TypeChecker  : Tableau " + id + " is not defined.", node.getLine(), node.getColumn());
            return null;
        }
        if (mo.getType() != ObjectType.INT )
        {
            logger.logError("TypeChecker  : Tableau " + id + " must be of type int ou boolean.", node.getLine(), node.getColumn());
            return null;
        }
        if (indx != ObjectType.INT)
        {
            logger.logError("TypeChecker  : Index must be of type int.", node.getLine(), node.getColumn());
            return null;
        }
        return ObjectType.INT;
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
    public Object visit(ASTNbre node, Object data)
    {

        Object value = node.jjtGetValue();
        if (value instanceof Integer)
        {
            return ObjectType.INT;
        }
        else
        {
            logger.logError("TypeChecker  : Expected an integer value, but got: " + value, node.getLine(),
                    node.getColumn());
        }
        return null;
    }

    @Override
    public Object visit(ASTChaine node, Object data)
    {
        Object value = node.jjtGetValue();
        if (value instanceof String)
        {
            String chaine = (String) value;
            return chaine;
        } else
        {
            logger.logError("TypeChecker  : Expected a string chaine, but got: " + value, node.getLine(),
                    node.getColumn());
        }

        return null;
    }

    @Override
    public Object visit(SimpleNode node, Object data)  throws VisitorException
    {
        //visitChildren(node, null);
        return node.jjtAccept(this, data);
    }

    /// * Methodes utils pour la verification des types *///

    private ObjectType visitBinaryOperation(SimpleNode node, Object data, ObjectType expectedType)  throws VisitorException
    {
        if (node.jjtGetNumChildren() != 2 || node.jjtGetChild(0) == null || node.jjtGetChild(1) == null)
        {
            logger.logError("TypeChecker  : Binary operation must have two operands.",  node.jjtGetLastToken().beginLine, node.jjtGetLastToken().beginColumn);
            return null;
        }

        ObjectType leftType = getNodeType(node.jjtGetChild(0), data);
        ObjectType rightType = getNodeType(node.jjtGetChild(1), data);
        logger.logInfo("type leftType " + leftType + "type right " + rightType) ;
        if (!(leftType instanceof ObjectType) || !(rightType instanceof ObjectType))
        {
            logger.logError("TypeChecker  : Expected ObjectType during binary op but got " + rightType,   node.jjtGetLastToken().beginLine, node.jjtGetLastToken().beginColumn);
            return null;
        }
        if (leftType != expectedType)
        {
            logger.logError("TypeChecker  : Type mismatch during binary Operation. Expected " + expectedType + " for left operand, but got " + leftType + ".",  node.jjtGetLastToken().beginLine, node.jjtGetLastToken().beginColumn);
            return null;
        }

        if (rightType != expectedType)
        {
            logger.logError("TypeChecker  : Type mismatch during binary Operation. Expected " + expectedType + " for right operand, but got " + rightType + ".",  node.jjtGetLastToken().beginLine, node.jjtGetLastToken().beginColumn);
            return null;
        }
        if (leftType != expectedType || rightType != expectedType)
        {
            logger.logError("TypeChecker  : Type mismatch during binary Operation Expected " + expectedType + " for operand.",
                    node.jjtGetLastToken().beginLine, node.jjtGetLastToken().beginColumn);
            return null;
        }
        return expectedType;
    }

    private ObjectType getNodeType(Node node, Object data) throws VisitorException
    {
        if (node instanceof ASTIdent)
        {
            Object value = node.jjtAccept(this, data);
            if (value instanceof ObjectType)
            {
                return (ObjectType) value;
            }
            if (value instanceof String)
            {
                String varName = (String) value;
                MemoryObject memoryObject = lookupSymbolidMeth(varName);
                MemoryObject mem = lookupSymbolident(varName);
                MemoryObject m = lookupSymbol(varName);
                if (memoryObject != null)
                {
                    return memoryObject.getType();
                } else if (mem != null){
                    return mem.getType();
                } else if (m != null){
                    return m.getType();
                }
                else
                {
                    logger.logError("variable " + varName + " is not defined!.",
                            ((SimpleNode) node).jjtGetLastToken().beginLine, ((SimpleNode) node).jjtGetLastToken().beginColumn);
                }
            }
            else if (node instanceof ASTExp)
            {
                return (ObjectType) node.jjtAccept(this, data);
            }
        } else if (node instanceof ASTExp){
            return (ObjectType) node.jjtAccept(this, data);
        }
        else if (node instanceof ASTOu){
            return (ObjectType) node.jjtAccept(this, data);
        }
        else if (node instanceof ASTVar || node instanceof ASTCst || node instanceof ASTTableau
        ) {
            Object value = node.jjtAccept(this, data);
            if (value instanceof String)
            {
                String varName = (String) value;
                MemoryObject memoryObject = lookupSymbolident(varName);
                if (memoryObject != null)
                {
                    return memoryObject.getType();
                }
            }
            if (value instanceof Integer)
            {
                return ObjectType.INT;
            }

        }
        else if (node instanceof ASTEt || node instanceof ASTOu )
        {
            return (ObjectType) visitBinaryOperation((SimpleNode)node, data, ObjectType.BOOLEAN);
        }
        else if (node instanceof ASTNot || node instanceof ASTNeg)
        {
            return (ObjectType) node.jjtAccept(this, data);
        }
        else if (node instanceof ASTChaine)
        {
            return ObjectType.INT;
        }
        else if (node instanceof ASTTab){
            return (ObjectType) node.jjtAccept(this, data);
        }
        else if (node instanceof ASTAppelE || node instanceof ASTAppelI)
        {
            return (ObjectType) visitMethodCall((SimpleNode) node, data);
        }
        else if (node instanceof ASTNbre)
        {
            return ObjectType.INT;
        }
        else if (node instanceof ASTVrai || node instanceof ASTFaux)
        {
            return ObjectType.BOOLEAN;
        }
        else {
            Object result = node.jjtAccept(this, data);
            logger.logInfo("object" + result);
            if (result instanceof ObjectType)
            {
                return (ObjectType) result;
            } else
            {
                logger.logError("TypeChecker : Expected ObjectType but got " + result, ((SimpleNode) node).jjtGetLastToken().beginLine, ((SimpleNode) node).jjtGetLastToken().beginColumn);
            }
        }
        return null;
    }

    private ObjectType visitUnaryOperation(SimpleNode node, Object data, ObjectType expectedType) throws VisitorException
    {
        ObjectType operandTypep = getNodeType(node.jjtGetChild(0), data);

        if (!(operandTypep instanceof ObjectType))
        {
            logger.logError("TypeChecker  : Expected ObjectType during unary op but got "
                    + operandTypep.getClass().getSimpleName(),node.jjtGetLastToken().beginLine, node.jjtGetLastToken().beginColumn);
            return null;
        }
        if (operandTypep != expectedType)
        {
            logger.logError("TypeChecker  : Type mismatch during unary op Expected  " + expectedType + " for operand.",
                    ((SimpleNode) node).jjtGetLastToken().beginLine, ((SimpleNode) node).jjtGetLastToken().beginColumn);
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


    // collecter les types des paramètres de la liste d'entetes avec les noms
    private void collectParamTypesName(ASTEntetes entetesNode, List<ObjectType> paramTypes, List<String> nameParam,
                                       Object data) throws VisitorException
    {
        for (int i = 0; i < entetesNode.jjtGetNumChildren(); i++)
        {
            SimpleNode child = (SimpleNode) entetesNode.jjtGetChild(i);
            if (child instanceof ASTEntete)
            {
                ObjectType paramType = (ObjectType) child.jjtGetChild(0).jjtAccept(this, data);
                String paramName = (String) ((ASTIdent) child.jjtGetChild(1)).jjtGetValue();
                if (paramType == null)
                {
                    logger.logError("Parameter type cannot be null.",  entetesNode.jjtGetLastToken().beginLine, entetesNode.jjtGetLastToken().beginColumn);
                }
                else
                {
                    paramTypes.add(paramType);
                    nameParam.add(paramName);
                }
            }
            else if (child instanceof ASTEntetes)
            {
                collectParamTypesName((ASTEntetes) child, paramTypes, nameParam, data);
            }
        }
    }

    // Collecter les types des paramètres de la liste d'expressions
    private void collectParamTypesFromListexp(ASTListExp listexpNode, List<ObjectType> paramTypes, Object data) throws VisitorException {
        for (int i = 0; i < listexpNode.jjtGetNumChildren(); i++)
        {
            SimpleNode child = (SimpleNode) listexpNode.jjtGetChild(i);
            if (child instanceof ASTExp)
            {
                ObjectType paramType = (ObjectType) child.jjtAccept(this, data);
                logger.logInfo("TypeChecker : Parameter type: " + paramType);
                if (paramType == null) {
                    logger.logError("Parameter type cannot be null.", listexpNode.jjtGetLastToken().beginLine, listexpNode.jjtGetLastToken().beginColumn);
                }
                else
                {
                    paramTypes.add(paramType);
                }
            }
            else if (child instanceof ASTListExp)
            {
                collectParamTypesFromListexp((ASTListExp) child, paramTypes, data);
            }
            else if (child instanceof ASTIdent)
            {
                MemoryObject mo = lookupSymbolidMeth((String) child.jjtGetValue());
                if (mo == null) {
                    logger.logError("TypeChecker : Variable " + child.jjtGetValue() + " is not defined.",   listexpNode.jjtGetLastToken().beginLine, listexpNode.jjtGetLastToken().beginColumn);
                }
                else
                {
                    paramTypes.add(mo.getType());
                }
            } else if (child instanceof ASTNbre) {
                paramTypes.add((ObjectType) child.jjtAccept(this, data));
            } else if (child instanceof ASTAppelE) {
                paramTypes.add((ObjectType) child.jjtAccept(this, data));
            } else if (child instanceof ASTTab) {
                paramTypes.add((ObjectType) child.jjtAccept(this, data));
            } else if (child instanceof ASTLongeur) {
                paramTypes.add((ObjectType) child.jjtAccept(this, data));
            } else if (child instanceof ASTNeg) {
                paramTypes.add((ObjectType) child.jjtAccept(this, data));
            } else if (child instanceof ASTNot) {
                paramTypes.add((ObjectType) child.jjtAccept(this, data));
            } else if (child instanceof ASTAdd) {
                paramTypes.add((ObjectType) child.jjtAccept(this, data));
            } else if (child instanceof ASTSub) {
                paramTypes.add((ObjectType) child.jjtAccept(this, data));
            } else if (child instanceof ASTMul) {
                paramTypes.add((ObjectType) child.jjtAccept(this, data));
            } else if (child instanceof ASTDiv) {
                paramTypes.add((ObjectType) child.jjtAccept(this, data));
            } else if (child instanceof ASTSomme) {
                paramTypes.add((ObjectType) child.jjtAccept(this, data));
            } else if (child instanceof ASTSup) {
                paramTypes.add((ObjectType) child.jjtAccept(this, data));
            } else if (child instanceof ASTEq) {
                paramTypes.add((ObjectType) child.jjtAccept(this, data));
            } else if (child instanceof ASTEt) {
                paramTypes.add((ObjectType) child.jjtAccept(this, data));
            } else if (child instanceof ASTOu) {
                paramTypes.add((ObjectType) child.jjtAccept(this, data));
            } else if (child instanceof ASTVrai) {
                paramTypes.add((ObjectType) child.jjtAccept(this, data));
            } else if (child instanceof ASTFaux) {
                paramTypes.add((ObjectType) child.jjtAccept(this, data));
            } else if (child instanceof ASTExnil) {
            }

        }

    }

    public MemoryObject lookupSymbol(String name) {

        try {
            MemoryObject mo = symbolTable.getCurrentScope(name);

            return mo;
        } catch (SymbolTableException e) {
            System.err.println("Error: " + e.getMessage());
        }


        return null;
    }

    public MemoryObject lookupSymbolidMeth(String name) {


        try {
            MemoryObject mo = symbolTable.get(name);

            return mo;
        } catch (SymbolTableException e) {
            System.err.println("Error: " + e.getMessage());
        }

        return null;
    }

    public Boolean isDefinedMeth(MethodSignature signature, ObjectType type, ObjectNature nat) {
        MemoryObject mo = lookupSymbol(signature.toString());
        if (mo != null) {
            return type == mo.getType() && nat == mo.getNature() && mo.getId() == signature.toString();
        }
        return false;
    }

    public Boolean isDefined(String name, ObjectType type, ObjectNature nat) {
        MemoryObject mo = lookupMethode(name);
        if (mo != null) {
            return type == mo.getType() && nat == mo.getNature();
        }
        return false;
    }

    public Boolean isDefinedDecl(String name, ObjectType type, ObjectNature nat) {
        MemoryObject mo = lookupMethode(name);
        if (mo != null) {
            return type == mo.getType() && nat == mo.getNature();
        }
        return false;
    }



    public MemoryObject lookupSymbolident(String name) {
        try {
            if (symbolTable == null) {
                throw new StackException("The symbol table is null, cannot lookup symbol");
            }
            for (HashTable scope : symbolTable.getScopes()) {
                for (List<MemoryObject> bucket : scope.getBuckets()) {
                    if (bucket != null) {
                        for (MemoryObject mo : bucket) {
                            if (mo.getId().equals(name)) {
                                return mo;
                            }
                        }
                    }
                }
            }
        } catch (StackException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return null;
    }

    public MemoryObject lookupMethode(String methodenSignature) {
        try {
            if (symbolTable == null) {
                throw new StackException("The symbol table is null, cannot lookup symbol");
            }
            for (HashTable scope : symbolTable.getScopes()) {
                for (List<MemoryObject> bucket : scope.getBuckets()) {
                    if (bucket != null) {
                        for (MemoryObject mo : bucket) {
                            if (mo.getNature() == ObjectNature.METH
                                    && mo.getId().equals(methodenSignature)) {
                                return mo;
                            }
                        }
                    }
                }
            }
        } catch (StackException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return null;
    }
}
