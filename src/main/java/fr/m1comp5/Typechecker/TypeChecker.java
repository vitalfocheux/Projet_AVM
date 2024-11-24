package fr.m1comp5.Typechecker;
import fr.m1comp5.Analyzer.mjj.generated.*;
import fr.m1comp5.Memory.MemoryObject;
import fr.m1comp5.Memory.ObjectNature;
import fr.m1comp5.Memory.ObjectType;
import fr.m1comp5.Memory.SymbolTable;
import fr.m1comp5.Memory.Stack;
import fr.m1comp5.Memory.StackException;

import java.util.ArrayList;
import java.util.List;

public class TypeChecker implements MiniJajaVisitor {

    private Stack stack = new Stack(); // Pile pour gérer les portées
    private String currentMethod = null; // Méthode courante

    public TypeChecker() {
        try {
            stack.push(new MemoryObject("global", new SymbolTable(), ObjectNature.VAR, ObjectType.VOID)); // Initialiser la table des symboles globale
        } catch (StackException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object visit(ASTRoot node, Object data) {
        return visitChildren(node, data);
    } 
   

     @Override
     public Object visit(ASTClasse node, Object data) {
        ASTIdent classNameNode = (ASTIdent) node.jjtGetChild(0);
        String className = (String) classNameNode.jjtGetValue(); 
        if (lookupSymbol(className) != null) {
            throw new TypeCheckException("La classe " + className + " est déjà définie.");
        }
        try {
            stack.push(new MemoryObject(className, new SymbolTable(), null, ObjectType.VOID));
        } catch (StackException e) {
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
                throw new TypeCheckException("Invalid identifier: " + ident + ". Identifiers must start with a letter and contain only letters and digits.");
            }
        } else {
            throw new TypeCheckException("Expected a string identifier, but got: " + value);
        }
    }


    @Override
    public Object visit(ASTVnil node, Object data) {
        return ObjectType.OMEGA;
    }

    @Override
    public Object visit(ASTDecls node, Object data) {
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTCst node, Object data) {
        ObjectType cstType = (ObjectType) node.jjtGetChild(0).jjtAccept(this, data);
        String cstName = (String) node.jjtGetChild(1).jjtAccept(this, data);
        Object  cstValue =  node.jjtGetChild(2).jjtAccept(this, data);

        if (lookupSymbol(cstName) != null) {
            throw new TypeCheckException("Constant " + cstName + " is already defined.");
        } else {
            MemoryObject mo = new MemoryObject(cstName, cstValue, ObjectNature.CST, cstType);
            try {
                ((SymbolTable) stack.getTop().getValue()).put(mo);
            } catch (StackException e) {
                e.printStackTrace();
            }
        }
        return cstType;
        
    }
    
   @Override
    public Object visit(ASTVars node, Object data) {
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTVar node, Object data) {

        ASTIdent identNode = (ASTIdent) node.jjtGetChild(1);
        ObjectType varType = (ObjectType) node.jjtGetChild(0).jjtAccept(this, data);
        String varName = identNode.jjtGetValue().toString();
        if (lookupSymbol(varName) != null) {
            throw new TypeCheckException("Variable " + varName + " is already defined.");
        } else {
            MemoryObject mo = new MemoryObject(varName, null, ObjectNature.VAR, varType);
            try {
                ((SymbolTable) stack.getTop().getValue()).put(mo);
            } catch (StackException e) {
                e.printStackTrace();
            }
        }
        MemoryObject mo = new MemoryObject(varName, null, ObjectNature.VAR,varType);
        return mo;
    }

    @Override
    public Object visit(ASTTableau node, Object data) {
        String arrayName = (String) node.jjtGetChild(1).jjtAccept(this, data);  // nom 
        ObjectType arrayType = (ObjectType) node.jjtGetChild(0).jjtAccept(this, data); // type 

        if (lookupSymbol(arrayName) != null) {
            throw new TypeCheckException("Tableau " + arrayName + " is already defined.");
        } else {
            MemoryObject mo = new MemoryObject(arrayName, null, ObjectNature.TAB, arrayType);
            try {
                ((SymbolTable) stack.getTop().getValue()).put(mo);
            } catch (StackException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Object visit(ASTOmega node, Object data) {
        return ObjectType.OMEGA;
    }

    @Override
    public Object visit(ASTMethode node, Object data) {
            Object returnTypeObject = node.jjtGetChild(0).jjtAccept(this, data);
        if (!(returnTypeObject instanceof ObjectType)) {
            throw new TypeCheckException("Expected ObjectType but found " + returnTypeObject.getClass().getSimpleName());
        }
        ObjectType returnType = (ObjectType) returnTypeObject;

        String methodName = (String) node.jjtGetChild(1).jjtAccept(this, data);
        List<ObjectType> paramTypes = new ArrayList<>();

        // Récupérer les types des paramètres 
        ASTEntetes entetesNode = (ASTEntetes) node.jjtGetChild(2);
        collectParamTypes(entetesNode, paramTypes, data);

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
        if (lookupSymbol(methodSignature) != null) {
            throw new TypeCheckException("Method " + methodSignature + " is already defined.");
        } else {
            MemoryObject mo = new MemoryObject(methodSignature, null, ObjectNature.METH, returnType, paramTypes);
            try {
                ((SymbolTable) stack.getTop().getValue()).put(mo);
            } catch (StackException e) {
                e.printStackTrace();
            }
            currentMethod = methodSignature; // Enregistrer la méthode courante
        }

        // nouvelle portée pour les paramètres et les variables locales
        try {
            stack.push(new MemoryObject("scope", new SymbolTable(), ObjectNature.VAR, ObjectType.VOID));
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

    private void collectParamTypes(ASTEntetes entetesNode, List<ObjectType> paramTypes, Object data) {
        for (int i = 0; i < entetesNode.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) entetesNode.jjtGetChild(i);
            if (child instanceof ASTEntete) {
                ObjectType paramType = (ObjectType) child.jjtGetChild(0).jjtAccept(this, data);
                if (paramType == null) {
                    throw new TypeCheckException("Parameter type cannot be null.");
                }
                paramTypes.add(paramType);
            } else if (child instanceof ASTEntetes) {
                collectParamTypes((ASTEntetes) child, paramTypes, data);
            }
        }
    }

    @Override
    public Object visit(ASTMain node, Object data) {

        if (lookupSymbol("main") != null) {
            throw new TypeCheckException("Main method is already defined.");
        }
        // Nouvelle portée pour le main
        try {
            stack.push(new MemoryObject("main", new SymbolTable(), ObjectNature.METH, ObjectType.VOID));
        } catch (StackException e) {
            e.printStackTrace();
        }
        // déclarations de variables, instructions
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            node.jjtGetChild(i).jjtAccept(this, data);
        }
        // Réinitialiser 
        try {
            stack.pop();
        } catch (StackException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Object visit(ASTEnil node, Object data) {
        return ObjectType.OMEGA;
    }

    @Override
    public Object visit(ASTEntetes node, Object data) {
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTEntete node, Object data) {
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTInil node, Object data) {
        return ObjectType.OMEGA;
    }

    @Override
    public Object visit(ASTInstrs node, Object data) {
        return visitChildren(node, data);
    }
    
    @Override
    public Object visit(ASTRetour node, Object data) {
        ObjectType returnType = (ObjectType) node.jjtGetChild(0).jjtAccept(this, data);
        MemoryObject mo = lookupSymbol(currentMethod); // Récupérer la méthode courante
        if (mo != null && returnType != mo.getType()) {
            throw new TypeCheckException("Return type mismatch: Expected " + mo.getType() + " but got " + returnType);
        }
        currentMethod = null;
        return null;
    }

    @Override
    public Object visit(ASTEcrire node, Object data) {
        return node.jjtGetChild(0).jjtAccept(this, data);
    }

    @Override
    public Object visit(ASTEcrireLn node, Object data) {
        return node.jjtGetChild(0).jjtAccept(this, data);
    }

    @Override
    public Object visit(ASTSi node, Object data) {
        ObjectType conditionType = (ObjectType) node.jjtGetChild(0).jjtAccept(this, data);
        if (conditionType != ObjectType.BOOLEAN) {
            throw new TypeCheckException("Condition in if statement must be boolean.");
        }
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTTantQue node, Object data) {
        ObjectType conditionType = (ObjectType) node.jjtGetChild(0).jjtAccept(this, data);
        if (conditionType != ObjectType.BOOLEAN) {
            throw new TypeCheckException("Condition in while statement must be boolean.");
        }
        return visitChildren(node, data);
    }
    
    @Override
    public Object visit(ASTAffectation node, Object data) {
        String identName = (String)  node.jjtGetChild(0).jjtAccept(this, data);
        
        ObjectType assignedType = (ObjectType) node.jjtGetChild(1).jjtAccept(this, data);

        MemoryObject mo = lookupSymbol(identName);
        if (mo == null) {
            throw new TypeCheckException("Variable " + identName + " is not defined.");
        }
        // Vérifier que les types correspondent
        if (assignedType != mo.getType()) {
            throw new TypeCheckException("Type mismatch: Cannot assign " + assignedType + " to " + identName + " of type " + mo.getType());
        }
        Object value = node.jjtGetChild(1).jjtAccept(this, data);
        MemoryObject mo2 = new MemoryObject(identName,value , ObjectNature.VAR, assignedType);
        return mo2;
    }

    @Override
    public Object visit(ASTIncrement node, Object data) {
        String varName = (String) node.jjtGetChild(0).jjtAccept(this, data); //  nom de la variable
        MemoryObject mo = lookupSymbol(varName); 
        
        if (mo == null) {
            throw new TypeCheckException("Variable " + varName + " is not defined.");
        }
        if ( mo.getType() != ObjectType.INT) {
            throw new TypeCheckException("Type mismatch: Cannot increment non-integer variable " + varName);
        }
        return ObjectType.INT;
    }
    @Override
    public Object visit(ASTAppelI node, Object data) {
        String methodName = (String) node.jjtGetChild(0).jjtAccept(this, data);
    
        // Construire la signature de la méthode à partir des paramètres
        Object paramsData = node.jjtGetChild(1);
        if (!(paramsData instanceof ASTListExp)) {
            throw new TypeCheckException("Expected ASTListExp for method parameters.");
        }
        ASTListExp paramsNode = (ASTListExp) paramsData;
        List<ObjectType> actualParamTypes = new ArrayList<>();
        collectParamTypesFromListexp(paramsNode, actualParamTypes, data);
    
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
            throw new TypeCheckException("Method " + methodSignature + " is not defined.");
        }
    
        // Vérifier les types des paramètres
        List<ObjectType> expectedParamTypes = mo.getParamTypes();
        if (expectedParamTypes.size() != actualParamTypes.size()) {
            throw new TypeCheckException("Parameter count mismatch in method " + methodSignature);
        }
    
        for (int i = 0; i < expectedParamTypes.size(); i++) {
            if (expectedParamTypes.get(i) != actualParamTypes.get(i)) {
                throw new TypeCheckException("Parameter type mismatch in method " + methodSignature + ": expected " + expectedParamTypes.get(i) + " but got " + actualParamTypes.get(i));
            }
        }
    
        return mo.getType();
    }
    
    // liste des expressions : exp, liste des expressions |exp , exnil |epsilon 
    @Override
    public Object visit(ASTListExp node, Object data) {
        if (node.jjtGetNumChildren() == 2) {
            SimpleNode firstChild = (SimpleNode) node.jjtGetChild(0);
            SimpleNode secondChild = (SimpleNode) node.jjtGetChild(1);
            if (firstChild instanceof ASTExp) {
                ObjectType paramType = (ObjectType) firstChild.jjtAccept(this, data);
                if (paramType == null) {
                    throw new TypeCheckException("Parameter type cannot be null.");
                }
            }else {
                throw new TypeCheckException("Unexpected node type in ASTListExp: " + firstChild.getClass().getSimpleName());
            }
            if (secondChild instanceof ASTListExp) {
                return secondChild.jjtAccept(this, data);
            } else if (!(secondChild instanceof ASTExnil)) {
                throw new TypeCheckException("Unexpected node type in ASTListExp: " + secondChild.getClass().getSimpleName());
            }
        } else if (node.jjtGetNumChildren() == 1) {
            if (node.jjtGetChild(0) instanceof ASTExnil) {
                return ObjectType.OMEGA;
            }
        }
        return null;
    }

    @Override
    public Object visit(ASTExp node, Object data) {
        return node.jjtGetChild(0).jjtAccept(this, data);
    }
    
    private void collectParamTypesFromListexp(ASTListExp listexpNode, List<ObjectType> paramTypes, Object data) {
        if (listexpNode.jjtGetNumChildren() == 2) {
            SimpleNode firstChild = (SimpleNode) listexpNode.jjtGetChild(0);
            SimpleNode secondChild = (SimpleNode) listexpNode.jjtGetChild(1);
    
            if (firstChild instanceof ASTExp) {
                ObjectType paramType = (ObjectType) firstChild.jjtAccept(this, data);
                if (paramType == null) {
                    throw new TypeCheckException("Parameter type cannot be null.");
                }
                paramTypes.add(paramType);
            }
    
            if (secondChild instanceof ASTListExp) {
                collectParamTypesFromListexp((ASTListExp) secondChild, paramTypes, data);
            } else if (!(secondChild instanceof ASTExnil)) {
                throw new TypeCheckException("Unexpected node type in ASTListExp: " + secondChild.getClass().getSimpleName());
            }
        } 
        else if (listexpNode.jjtGetNumChildren() == 1) {
            if (listexpNode.jjtGetChild(0) instanceof ASTExnil) {
                return;
            }

        } 
    }
    @Override
    public Object visit(ASTAppelE node, Object data) {
        String methodName = (String) node.jjtGetChild(0).jjtAccept(this, data);
    
        // Construire la signature de la méthode à partir des paramètres
        Object paramsData = node.jjtGetChild(1);
        if (!(paramsData instanceof ASTListExp)) {
            throw new TypeCheckException("Expected ASTListExp for method parameters.");
        }
        ASTListExp paramsNode = (ASTListExp) paramsData;
        List<ObjectType> actualParamTypes = new ArrayList<>();
        collectParamTypesFromListexp(paramsNode, actualParamTypes, data);
    
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
            throw new TypeCheckException("Method " + methodSignature + " is not defined.");
        }
    
        // Vérifier les types des paramètres
        List<ObjectType> expectedParamTypes = mo.getParamTypes();
        if (expectedParamTypes.size() != actualParamTypes.size()) {
            throw new TypeCheckException("Parameter count mismatch in method " + methodSignature);
        }
    
        for (int i = 0; i < expectedParamTypes.size(); i++) {
            if (expectedParamTypes.get(i) != actualParamTypes.get(i)) {
                throw new TypeCheckException("Parameter type mismatch in method " + methodSignature + ": expected " + expectedParamTypes.get(i) + " but got " + actualParamTypes.get(i));
            }
        }
    
        return mo.getType();
        
    }

    @Override
    public Object visit(ASTExnil node, Object data) {
        return ObjectType.OMEGA;
    }

    @Override
    public Object visit(ASTNot node, Object data) {
        return visitUnaryOperation(node, data, ObjectType.BOOLEAN);
    }

    @Override
    public Object visit(ASTNeg node, Object data) {
        return visitUnaryOperation(node, data, ObjectType.INT);
    }

    @Override
    public Object visit(ASTEt node, Object data) {
        return visitBinaryOperation(node, data, ObjectType.BOOLEAN);
    }

    @Override
    public Object visit(ASTOu node, Object data) {
        return visitBinaryOperation(node, data, ObjectType.BOOLEAN);
    }

    @Override
    public Object visit(ASTEq node, Object data) {
        ObjectType leftType = (ObjectType) node.jjtGetChild(0).jjtAccept(this, data);
        ObjectType rightType = (ObjectType) node.jjtGetChild(1).jjtAccept(this, data);
        
        if (leftType != rightType) {
            throw new TypeCheckException("Type mismatch: Expected same type for both operands.");
        }
        return ObjectType.BOOLEAN;
    }

    @Override
    public Object visit(ASTSup node, Object data) {
        ObjectType leftType = (ObjectType) node.jjtGetChild(0).jjtAccept(this, data);
        ObjectType rightType = (ObjectType) node.jjtGetChild(1).jjtAccept(this, data);
        
        if (leftType != rightType && leftType != ObjectType.INT && rightType != ObjectType.INT) {
            throw new TypeCheckException("Type mismatch: Expected  type INT for both operands.");
        }
        return ObjectType.BOOLEAN;
    }

    @Override
    public Object visit(ASTAdd node, Object data) {
        return visitBinaryOperation(node, data, ObjectType.INT);
    }

    @Override
    public Object visit(ASTSub node, Object data) {
        return visitBinaryOperation(node, data, ObjectType.INT);
    }

    @Override
    public Object visit(ASTMul node, Object data) {
        return visitBinaryOperation(node, data, ObjectType.INT);
    }

    @Override
    public Object visit(ASTSomme node, Object data) {
        return visitBinaryOperation(node, data, ObjectType.INT);
    }

    @Override
    public Object visit(ASTDiv node, Object data) {
        Object value = node.jjtGetChild(1).jjtAccept(this, data);
        if (value instanceof Integer) {
            int rightValue = (Integer) value;
            if (rightValue == 0) {
                throw new TypeCheckException("Error : not allowed to divide by zero !");
            }
        } else {
            throw new TypeCheckException("Expected an integer value, but got: " + value);
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
            throw new TypeCheckException("Expected a string identifier, but got: " + value);
        }
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
    public Object visit(ASTTab node, Object data) {
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
            throw new TypeCheckException("Expected an integer value, but got: " + value);
        }
    }

    @Override
    public Object visit(ASTChaine node, Object data) {
        Object value = node.jjtGetValue();
        if (value instanceof String) {
            String chaine = (String) value;
            return chaine ; 
        } else {
            throw new TypeCheckException("Expected a string chaine, but got: " + value);
        }

        
    }

    private ObjectType visitBinaryOperation(SimpleNode node, Object data, ObjectType expectedType) {
        ObjectType leftType = (ObjectType) node.jjtGetChild(0).jjtAccept(this, data);
        ObjectType rightType = (ObjectType) node.jjtGetChild(1).jjtAccept(this, data);
        
        if (leftType != expectedType || rightType != expectedType) {
            throw new TypeCheckException("Type mismatch: Expected " + expectedType + " for both operands.");
        }
        return expectedType;
    }

    private ObjectType visitUnaryOperation(SimpleNode node, Object data, ObjectType expectedType) {
        ObjectType operandType = (ObjectType) node.jjtGetChild(0).jjtAccept(this, data);
        
        if (operandType != expectedType) {
            throw new TypeCheckException("Type mismatch: Expected " + expectedType + " for operand.");
        }
        return expectedType;
    }

    private Object visitChildren(SimpleNode node, Object data) {
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
            SymbolTable symbolTable = (SymbolTable) stack.getTop().getValue(); 
            
            return symbolTable.get(name);
        } catch (StackException e) {
            System.err.println("Error: " + e.getMessage());
            return null; 
        }
    }
    
    @Override
    public Object visit(SimpleNode node, Object data) {
        return node.jjtAccept(this, data);
    }
}