package RENFADFA;

import java.util.*;

public class SymbolTable {
    private static class Symbol {
        String name;
        String type;
        String scope;
        int memoryLocation;

        Symbol(String name, String type, String scope, int memoryLocation) {
            this.name = name;
            this.type = type;
            this.scope = scope;
            this.memoryLocation = memoryLocation;
        }

        @Override
        public String toString() {
            return "Symbol{" +
                    "name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", scope='" + scope + '\'' +
                    ", memoryLocation=" + memoryLocation +
                    '}';
        }
    }

    private Map<String, Symbol> symbols;
    private int memoryLocationCounter;
    private String currentScope;

    public SymbolTable() {
        this.symbols = new HashMap<>();
        this.memoryLocationCounter = 0;
        this.currentScope = "global";
    }

    public void enterScope(String scopeName) {
        this.currentScope = scopeName;
    }

    public void exitScope() {
        this.currentScope = "global";
    }

    public void addSymbol(String name, String type) {
        if (symbols.containsKey(name)) {
            throw new RuntimeException("Symbol '" + name + "' already defined in this scope.");
        }
        symbols.put(name, new Symbol(name, type, currentScope, memoryLocationCounter++));
    }

    public Symbol getSymbol(String name) {
        return symbols.get(name);
    }

    public void display() {
        System.out.println("Symbol Table:");
        for (Symbol symbol : symbols.values()) {
            System.out.println(symbol);
        }
    }
}