package fr.m1comp5;

public class SymbolTableException extends Exception {
    public SymbolTableException(String message) {
      super("An error occurred while trying to access the symbol table: " + message);
    }
}
