package fr.m1comp5.Typechecker;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import fr.m1comp5.Analyzer.mjj.generated.*;

public class TypeChecker implements MiniJajaVisitor {

    private Stack<Map<String, String>> symbolTableStack = new Stack<>(); // Table des symboles
    private String currentMethod = null; // 

    public TypeChecker() {
        symbolTableStack.push(new HashMap<>()); // Initialiser la table des symboles
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
        return lookupSymbol(varName); // Retourner le type de la variable
    }

    @Override
    public Object visit(ASTvnil node, Object data) {
        return visitChildren(node, data);
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
        String returnType = (String) node.jjtGetChild(0).jjtAccept(this, data); // Obtenir le type de retour

        if (lookupSymbol(methodName) != null) {
            System.err.println("Method " + methodName + " is already defined.");
        } else {
            symbolTableStack.peek().put(methodName, returnType);
            currentMethod = methodName; // Enregistrer la méthode courante
        }

        // Nouvelle portée pour les paramètres et les variables locales
        symbolTableStack.push(new HashMap<>());

        // Vérifier les paramètres et le corps de la méthode
        for (int i = 2; i < node.jjtGetNumChildren(); i++) {
            node.jjtGetChild(i).jjtAccept(this, data);
        }

        // Réinitialiser après la méthode
        symbolTableStack.pop();
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
        String varType = (String) node.jjtGetChild(0).jjtAccept(this, data); // Obtenir le type de la variable

        if (lookupSymbol(varName) != null) {
            System.err.println("Variable " + varName + " is already defined.");
        } else {
            symbolTableStack.peek().put(varName, varType);
        }
        return null;
    }

    @Override
    public Object visit(ASTomega node, Object data) {
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTmain node, Object data) {
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTenil node, Object data) {
        return visitChildren(node, data);
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
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTinstrs node, Object data) {
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTretour node, Object data) {
        String returnType = (String) node.jjtGetChild(0).jjtAccept(this, data);
        String expectedReturnType = lookupSymbol(currentMethod); // Obtenir le type de retour attendu

        if (!returnType.equals(expectedReturnType)) {
            System.err.println("Return type mismatch: Expected " + expectedReturnType + " but got " + returnType);
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
        String conditionType = (String) node.jjtGetChild(0).jjtAccept(this, data);
        if (!conditionType.equals("boolean")) {
            System.err.println("Condition in if statement must be boolean.");
        }
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTtantque node, Object data) {
        String conditionType = (String) node.jjtGetChild(0).jjtAccept(this, data);
        if (!conditionType.equals("boolean")) {
            System.err.println("Condition in while statement must be boolean.");
        }
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTaffectation node, Object data) {
        String varName = (String) node.jjtGetChild(0).jjtAccept(this, data); // Obtenir le nom de la variable
        String assignedType = (String) node.jjtGetChild(1).jjtAccept(this, data); // Type de l'expression à affecter

        // Vérifier le type de la variable
        if (!assignedType.equals(lookupSymbol(varName))) {
            System.err.println("Type mismatch: Cannot assign " + assignedType + " to " + varName);
        }
        return null;
    }

    @Override
    public Object visit(ASTsomme node, Object data) {
        return visitBinaryOperation(node, data, "int");
    }

    @Override
    public Object visit(ASTincrement node, Object data) {
        String varName = (String) node.jjtGetChild(0).jjtAccept(this, data); // Obtenir le nom de la variable
        String varType = lookupSymbol(varName); // Obtenir le type de la variable

        if (!varType.equals("int")) {
            System.err.println("Type mismatch: Cannot increment non-integer variable " + varName);
        }
        return null;
    }

    @Override
    public Object visit(ASTappelI node, Object data) {
        String methodName = (String) node.jjtGetValue(); // Obtenir le nom de la méthode
        String returnType = lookupSymbol(methodName); // Obtenir le type de retour de la méthode
        return returnType;
    }

    @Override
    public Object visit(ASTlistexp node, Object data) {
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTexnil node, Object data) {
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTnot node, Object data) {
        return visitUnaryOperation(node, data, "boolean");
    }

    @Override
    public Object visit(ASTneg node, Object data) {
        return visitUnaryOperation(node, data, "int");
    }

    @Override
    public Object visit(ASTet node, Object data) {
        return visitBinaryOperation(node, data, "boolean");
    }

    @Override
    public Object visit(ASTou node, Object data) {
        return visitBinaryOperation(node, data, "boolean");
    }

    @Override
    public Object visit(ASTeq node, Object data) {
        return visitBinaryOperation(node, data, "boolean");
    }

    @Override
    public Object visit(ASTsup node, Object data) {
        return visitBinaryOperation(node, data, "boolean");
    }

    @Override
    public Object visit(ASTadd node, Object data) {
        return visitBinaryOperation(node, data, "int");
    }

    @Override
    public Object visit(ASTsub node, Object data) {
        return visitBinaryOperation(node, data, "int");
    }

    @Override
    public Object visit(ASTmul node, Object data) {
        return visitBinaryOperation(node, data, "int");
    }

    @Override
    public Object visit(ASTdiv node, Object data) {
        return visitBinaryOperation(node, data, "int");
    }

    @Override
    public Object visit(ASTlongeur node, Object data) {
        return visitChildren(node, data);
    }

    @Override
    public Object visit(ASTvrai node, Object data) {
        return "boolean";
    }

    @Override
    public Object visit(ASTfaux node, Object data) {
        return "boolean";
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
        return "void";
    }

    @Override
    public Object visit(ASTentier node, Object data) {
        return "int";
    }

    @Override
    public Object visit(ASTbooleen node, Object data) {
        return "boolean";
    }

    @Override
    public Object visit(ASTnbre node, Object data) {
        return "int";
    }

    @Override
    public Object visit(ASTchaine node, Object data) {
        return "String";
    }

    private Object visitBinaryOperation(SimpleNode node, Object data, String expectedType) {
        String leftType = (String) node.jjtGetChild(0).jjtAccept(this, data);
        String rightType = (String) node.jjtGetChild(1).jjtAccept(this, data);
        
        if (!leftType.equals(expectedType) || !rightType.equals(expectedType)) {
            System.err.println("Type mismatch: Expected " + expectedType + " for both operands.");
        }
        return expectedType;
    }

    private Object visitUnaryOperation(SimpleNode node, Object data, String expectedType) {
        String operandType = (String) node.jjtGetChild(0).jjtAccept(this, data);
        
        if (!operandType.equals(expectedType)) {
            System.err.println("Type mismatch: Expected " + expectedType + " for operand.");
        }
        return expectedType;
    }

    private Object visitChildren(SimpleNode node, Object data) {
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            node.jjtGetChild(i).jjtAccept(this, data);
        }
        return null;
    }
   // chercher le type d'une variable dans la table des symboles
    private String lookupSymbol(String name) {
        for (int i = symbolTableStack.size() - 1; i >= 0; i--) {
            if (symbolTableStack.get(i).containsKey(name)) {
                return symbolTableStack.get(i).get(name);
            }
        }
        return null;
    }

    @Override
    public Object visit(SimpleNode node, Object data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }
}