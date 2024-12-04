package fr.m1comp5.Debug;

import fr.m1comp5.Analyzer.mjj.generated.SimpleNode;
import fr.m1comp5.Analyzer.mjj.generated.Token;

import java.util.HashMap;
import java.util.Map;

public class TokenerrorPosition {
    private static final Map<SimpleNode, Token> nodeTokenMap = new HashMap<>();

    public static void setToken(SimpleNode node, Token token) {
        nodeTokenMap.put(node, token);
    }

    public static Token getToken(SimpleNode node) {
        return nodeTokenMap.get(node);
    }

    public static PositionDebug getPosition(SimpleNode node) {
        Token token = getToken(node);
        return token != null ? new TokenPositionDebug(token) : null;
    }
}