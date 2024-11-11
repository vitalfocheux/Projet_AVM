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
    public Object visit(ASTroot node, Object data) {
        return visitChildren(node, data);
    } 
   

     @Override
     public Object visit(ASTclasse node, Object data) {
        ASTident classNameNode = (ASTident) node.jjtGetChild(0);
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
    public Object visit(ASTident node, Object data) {
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
    public Object visit(ASTvnil node, Object data) {
        return ObjectType.EPSILON;
    }

    @Override
    public Object visit(ASTdecls node, Object data) {
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTcst node, Object data) {
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
    public Object visit(ASTvars node, Object data) {
        return visitChildren(node, data);
    }


    @Override
    public Object visit(ASTvar node, Object data) {

        ASTident identNode = (ASTident) node.jjtGetChild(1);
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
    public Object visit(ASTtableau node, Object data) {
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
    public Object visit(ASTomega node, Object data) {
        return ObjectType.VOID;
    }
    @Override
    public Object visit(ASTmethode node, Object data) {
            Object returnTypeObject = node.jjtGetChild(0).jjtAccept(this, data);
        if (!(returnTypeObject instanceof ObjectType)) {
            throw new TypeCheckException("Expected ObjectType but found " + returnTypeObject.getClass().getSimpleName());
        }
        ObjectType returnType = (ObjectType) returnTypeObject;
        System.out.println("Return type: " + returnType);

        String methodName = (String) node.jjtGetChild(1).jjtAccept(this, data);
        List<ObjectType> paramTypes = new ArrayList<>();

        // Récupérer les types des paramètres 
        ASTentetes entetesNode = (ASTentetes) node.jjtGetChild(2);
        collectParamTypes(entetesNode, paramTypes, data);
        System.out.println("Param types: " + paramTypes);

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

    private void collectParamTypes(ASTentetes entetesNode, List<ObjectType> paramTypes, Object data) {
        for (int i = 0; i < entetesNode.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) entetesNode.jjtGetChild(i);
            if (child instanceof ASTentete) {
                ObjectType paramType = (ObjectType) child.jjtGetChild(0).jjtAccept(this, data);
                if (paramType == null) {
                    throw new TypeCheckException("Parameter type cannot be null.");
                }
                paramTypes.add(paramType);
            } else if (child instanceof ASTentetes) {
                collectParamTypes((ASTentetes) child, paramTypes, data);
            }
        }
    }

    @Override
    public Object visit(ASTmain node, Object data) {

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
    public Object visit(ASTenil node, Object data) {
        return ObjectType.EPSILON;
    }

    @Override
    public Object visit(ASTentetes node, Object data) {
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTentete node, Object data) {
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTinil node, Object data) {
        return ObjectType.EPSILON;
    }

    @Override
    public Object visit(ASTinstrs node, Object data) {
        return visitChildren(node, data);
    }
    
    @Override
    public Object visit(ASTretour node, Object data) {
        ObjectType returnType = (ObjectType) node.jjtGetChild(0).jjtAccept(this, data);
        MemoryObject mo = lookupSymbol(currentMethod); // Récupérer la méthode courante
        if (mo != null && returnType != mo.getType()) {
            throw new TypeCheckException("Return type mismatch: Expected " + mo.getType() + " but got " + returnType);
        }
        currentMethod = null;
        return null;
    }

    @Override
    public Object visit(ASTecrire node, Object data) {
        return node.jjtGetChild(0).jjtAccept(this, data);
    }

    @Override
    public Object visit(ASTecrireln node, Object data) {
        return node.jjtGetChild(0).jjtAccept(this, data);
    }

    @Override
    public Object visit(ASTsi node, Object data) {
        ObjectType conditionType = (ObjectType) node.jjtGetChild(0).jjtAccept(this, data);
        if (conditionType != ObjectType.BOOLEAN) {
            throw new TypeCheckException("Condition in if statement must be boolean.");
        }
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTtantque node, Object data) {
        ObjectType conditionType = (ObjectType) node.jjtGetChild(0).jjtAccept(this, data);
        if (conditionType != ObjectType.BOOLEAN) {
            throw new TypeCheckException("Condition in while statement must be boolean.");
        }
        return visitChildren(node, data);
    }
    
    @Override
    public Object visit(ASTaffectation node, Object data) {
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
    public Object visit(ASTsomme node, Object data) {
        return visitBinaryOperation(node, data, ObjectType.INT);
    }

    @Override
    public Object visit(ASTincrement node, Object data) {
        String varName = (String) node.jjtGetChild(0).jjtAccept(this, data); //  nom de la variable
        System.out.println("Incrementing variable " + varName);
        MemoryObject mo = lookupSymbol(varName); 
        System.out.println("MemoryObject: " + mo);
        
        if (mo == null) {
            throw new TypeCheckException("Variable " + varName + " is not defined.");
        }
        if ( mo.getType() != ObjectType.INT) {
            throw new TypeCheckException("Type mismatch: Cannot increment non-integer variable " + varName);
        }
        return ObjectType.INT;
    }
    @Override
    public Object visit(ASTappelI node, Object data) {
        String methodName = (String) node.jjtGetChild(0).jjtAccept(this, data);
        //Object p = (Object) node.jjtGetChild(1).jjtAccept(this, data);
        //System.out.println("Looking up method: " + p);
    
        // Construire la signature de la méthode à partir des paramètres
        ASTlistexp paramsNode = (ASTlistexp) node.jjtGetChild(1);
        System.out.println("Params node: " + paramsNode);
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
        System.out.println("Looking up method: " + methodSignature);
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
    
    private void collectParamTypesFromListexp(ASTlistexp listexpNode, List<ObjectType> paramTypes, Object data) {
        if (listexpNode.jjtGetNumChildren() == 2) {
            SimpleNode firstChild = (SimpleNode) listexpNode.jjtGetChild(0);
            SimpleNode secondChild = (SimpleNode) listexpNode.jjtGetChild(1);
            // PROBLEME dans listexp son children est root :( ICI !!!
            /*System.out.println("First child: " + firstChild);
            System.out.println("Second child: " + secondChild);*/
    
            if (firstChild instanceof ASTexp) {
                ObjectType paramType = (ObjectType) firstChild.jjtAccept(this, data);
                System.out.println("Param type: " + paramType);
                if (paramType == null) {
                    throw new TypeCheckException("Parameter type cannot be null.");
                }
                paramTypes.add(paramType);
            }
    
            if (secondChild instanceof ASTlistexp) {
                collectParamTypesFromListexp((ASTlistexp) secondChild, paramTypes, data);
            } else if (!(secondChild instanceof ASTexnil)) {
                throw new TypeCheckException("Unexpected node type in ASTlistexp: " + secondChild.getClass().getSimpleName());
            }

        } 
    }

    // liste des expressions : exp, liste des expressions |exp , exnil |epsilon 
    @Override
    public Object visit(ASTlistexp node, Object data) {
        return visitChildren(node, data);
     
    }

    @Override
    public Object visit(ASTexnil node, Object data) {
        return ObjectType.EPSILON;
    }

    @Override
    public Object visit(ASTnot node, Object data) {
        return visitUnaryOperation(node, data, ObjectType.BOOLEAN);
    }

    @Override
    public Object visit(ASTneg node, Object data) {
        return visitUnaryOperation(node, data, ObjectType.INT);
    }

    @Override
    public Object visit(ASTet node, Object data) {
        return visitBinaryOperation(node, data, ObjectType.BOOLEAN);
    }

    @Override
    public Object visit(ASTou node, Object data) {
        return visitBinaryOperation(node, data, ObjectType.BOOLEAN);
    }

    @Override
    public Object visit(ASTeq node, Object data) {
        return visitBinaryOperation(node, data, ObjectType.BOOLEAN);
    }

    @Override
    public Object visit(ASTsup node, Object data) {
        return visitBinaryOperation(node, data, ObjectType.BOOLEAN);
    }

    @Override
    public Object visit(ASTadd node, Object data) {
        return visitBinaryOperation(node, data, ObjectType.INT);
    }

    @Override
    public Object visit(ASTsub node, Object data) {
        return visitBinaryOperation(node, data, ObjectType.INT);
    }

    @Override
    public Object visit(ASTmul node, Object data) {
        return visitBinaryOperation(node, data, ObjectType.INT);
    }

    @Override
    public Object visit(ASTdiv node, Object data) {
        return visitBinaryOperation(node, data, ObjectType.INT);
    }

    @Override
    public Object visit(ASTlongeur node, Object data) {
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTvrai node, Object data) {
        return ObjectType.BOOLEAN;
    }

    @Override
    public Object visit(ASTfaux node, Object data) {
        return ObjectType.BOOLEAN;
    }

    @Override
    public Object visit(ASTexp node, Object data) {
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTappelE node, Object data) {
        return node.jjtGetChild(0).jjtAccept(this, data);
    }

    @Override
    public Object visit(ASTtab node, Object data) {
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTrien node, Object data) {
        return ObjectType.VOID;
    }

    @Override
    public Object visit(ASTentier node, Object data) {
        return ObjectType.INT;
    }

    @Override
    public Object visit(ASTbooleen node, Object data) {
        return ObjectType.BOOLEAN;
    }

    @Override
    public Object visit(ASTnbre node, Object data) {
        if (node.jjtGetValue() instanceof Integer) {
            return ObjectType.INT;
        }
        
        return ObjectType.VOID;
    }

    @Override
    public Object visit(ASTchaine node, Object data) {
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