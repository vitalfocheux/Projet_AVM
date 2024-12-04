package fr.m1comp5.Debug;

import fr.m1comp5.Analyzer.mjj.generated.Token;

public class TokenPositionDebug implements PositionError {
    private final Token token;

    public TokenPositionDebug(Token token) {
        this.token = token;
    }

    @Override
    public int getLine() {
        return token.beginLine;
    }

    @Override
    public int getColumn() {
        return token.beginColumn;
    }
}