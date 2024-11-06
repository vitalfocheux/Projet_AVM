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

    public Stack stack = new Stack(); // Pile pour gérer les portées
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
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTident node, Object data) {
        String varName = (String) node.jjtGetValue(); // Obtenir le nom de la variable
        MemoryObject mo = lookupSymbol(varName);
        return mo != null ? mo.getType() : null; // Retourner le type de la variable
    }

    @Override
    public Object visit(ASTvnil node, Object data) {
        return ObjectType.VOID;
    }

    @Override
    public Object visit(ASTdecls node, Object data) {
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            node.jjtGetChild(i).jjtAccept(this, data);
        }
        return null;
    }

    @Override
    public Object visit(ASTcst node, Object data) {
        return node.jjtGetValue(); // Retourner le type de la constante
    }

    @Override
    public Object visit(ASTmethode node, Object data) {
        String methodName = (String) node.jjtGetChild(1).jjtAccept(this, data); // Obtenir le nom de la méthode
        ObjectType returnType = (ObjectType) node.jjtGetChild(0).jjtAccept(this, data); // Obtenir le type de retour

        List<ObjectType> paramTypes = new ArrayList<>();
        ASTentetes entetesNode = (ASTentetes) node.jjtGetChild(2);
        for (int i = 0; i < entetesNode.jjtGetNumChildren(); i++) {
            paramTypes.add((ObjectType) entetesNode.jjtGetChild(i).jjtAccept(this, data));
        }

        if (lookupSymbol(methodName) != null) {
            throw new TypeCheckException("Method " + methodName + " is already defined.");
        } else {
            MemoryObject mo = new MemoryObject(methodName, null, ObjectNature.METH, returnType, paramTypes);
            try {
                ((SymbolTable) stack.getTop().getValue()).put(mo);
            } catch (StackException e) {
                e.printStackTrace();
            }
            currentMethod = methodName; // Enregistrer la méthode courante
        }

        // Nouvelle portée pour les paramètres et les variables locales
        try {
            stack.push(new MemoryObject("scope", new SymbolTable(), ObjectNature.VAR, ObjectType.VOID));
        } catch (StackException e) {
            e.printStackTrace();
        }

        // Vérifier les paramètres et le corps de la méthode
        for (int i = 3; i < node.jjtGetNumChildren(); i++) {
            node.jjtGetChild(i).jjtAccept(this, data);
        }

        // Réinitialiser après la méthode
        try {
            stack.pop();
        } catch (StackException e) {
            e.printStackTrace();
        }
        currentMethod = null;
        return null;
    }

    @Override
    public Object visit(ASTvars node, Object data) {
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTtableau node, Object data) {
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTvar node, Object data) {
        String varName = (String) node.jjtGetChild(1).jjtAccept(this, data); // Obtenir le nom de la variable
        ObjectType varType = (ObjectType) node.jjtGetChild(0).jjtAccept(this, data); // Obtenir le type de la variable

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
        return null;
    }

    @Override
    public Object visit(ASTomega node, Object data) {
        return ObjectType.VOID;
    }

    @Override
    public Object visit(ASTmain node, Object data) {
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTenil node, Object data) {
        return ObjectType.VOID;
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
        return ObjectType.VOID;
    }

    @Override
    public Object visit(ASTinstrs node, Object data) {
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTretour node, Object data) {
        ObjectType returnType = (ObjectType) node.jjtGetChild(0).jjtAccept(this, data);
        MemoryObject mo = lookupSymbol(currentMethod); // Obtenir le type de retour attendu

        if (mo != null && returnType != mo.getType()) {
            throw new TypeCheckException("Return type mismatch: Expected " + mo.getType() + " but got " + returnType);
        }
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
        String varName = (String) node.jjtGetChild(0).jjtAccept(this, data); // Obtenir le nom de la variable
        ObjectType assignedType = (ObjectType) node.jjtGetChild(1).jjtAccept(this, data); // Type de l'expression à affecter

        // Vérifier le type de la variable
        MemoryObject mo = lookupSymbol(varName);
        if (mo != null && assignedType != mo.getType()) {
            throw new TypeCheckException("Type mismatch: Cannot assign " + assignedType + " to " + varName);
        }
        return null;
    }

    @Override
    public Object visit(ASTsomme node, Object data) {
        return visitBinaryOperation(node, data, ObjectType.INT);
    }

    @Override
    public Object visit(ASTincrement node, Object data) {
        String varName = (String) node.jjtGetChild(0).jjtAccept(this, data); // Obtenir le nom de la variable
        MemoryObject mo = lookupSymbol(varName); // Obtenir le type de la variable

        if (mo != null && mo.getType() != ObjectType.INT) {
            throw new TypeCheckException("Type mismatch: Cannot increment non-integer variable " + varName);
        }
        return null;
    }

        @Override
    public Object visit(ASTappelI node, Object data) {
        String methodName = (String) node.jjtGetValue(); // Obtenir le nom de la méthode
        MemoryObject mo = lookupSymbol(methodName); // Obtenir le type de retour de la méthode
        if (mo == null) {
            throw new TypeCheckException("Method " + methodName + " is not defined.");
        }

        // Vérifier les types des paramètres
        ASTlistexp paramsNode = (ASTlistexp) node.jjtGetChild(0);
        List<ObjectType> expectedParamTypes = mo.getParamTypes();
        List<ObjectType> actualParamTypes = new ArrayList<>();

        for (int i = 0; i < paramsNode.jjtGetNumChildren(); i++) {
            actualParamTypes.add((ObjectType) paramsNode.jjtGetChild(i).jjtAccept(this, data));
        }

        if (expectedParamTypes.size() != actualParamTypes.size()) {
            throw new TypeCheckException("Parameter count mismatch in method " + methodName);
        }

        for (int i = 0; i < expectedParamTypes.size(); i++) {
            if (expectedParamTypes.get(i) != actualParamTypes.get(i)) {
                throw new TypeCheckException("Parameter type mismatch in method " + methodName + ": expected " + expectedParamTypes.get(i) + " but got " + actualParamTypes.get(i));
            }
        }

        return mo.getType();
    }

    @Override
    public Object visit(ASTlistexp node, Object data) {
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTexnil node, Object data) {
        return ObjectType.VOID;
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
        return ObjectType.INT;
    }

    @Override
    public Object visit(ASTchaine node, Object data) {
        return ObjectType.STRING;
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
            for (int i = stack.size() - 1; i >= 0; i--) {
                SymbolTable symbolTable = (SymbolTable) stack.get(i).getValue();
                MemoryObject mo = symbolTable.get(name);
                if (mo != null) {
                    return mo;
                }
            }
        } catch (StackException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object visit(SimpleNode node, Object data) {
        return node.jjtAccept(this, data);
    }
}