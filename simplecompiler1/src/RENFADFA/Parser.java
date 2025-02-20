package RENFADFA;

import java.util.*;

public class Parser {
    private List<LexicalAnalyzer.Token> tokens;
    private int position;
    private ErrorHandler errorHandler;

    public Parser(List<LexicalAnalyzer.Token> tokens, ErrorHandler errorHandler) {
        this.tokens = tokens;
        this.position = 0;
        this.errorHandler = errorHandler;
    }

    private LexicalAnalyzer.Token currentToken() {
        if (position < tokens.size()) {
            return tokens.get(position);
        }
        return null;
    }

    private LexicalAnalyzer.Token nextToken() {
        if (position < tokens.size()) {
            return tokens.get(position++);
        }
        return null;
    }

    private void expect(String type) {
        LexicalAnalyzer.Token token = nextToken();
        if (token != null && token.getType().equals(type)) {
            return;
        }
        errorHandler.reportError("Expected token of type " + type + ", but found " + (token != null ? token.getType() : "EOF"), 1);
    }

    public void parseProgram() {
        while (currentToken() != null) {
            if (currentToken().getType().equals("KEYWORD")) {
                parseDeclaration();
            } else if (currentToken().getType().equals("IDENTIFIER")) {
                parseFunction();
            } else {
                errorHandler.reportError("Unexpected token: " + currentToken().getValue(), 1);
                nextToken(); // Skip the unexpected token
            }
        }
    }

    private void parseDeclaration() {
        expect("KEYWORD");
        expect("IDENTIFIER");
        if (currentToken() != null && currentToken().getType().equals("OPERATOR") && currentToken().getValue().equals("=")) {
            nextToken();
            parseExpression();
        }
        expect("OPERATOR");
    }

    private void parseFunction() {
        expect("IDENTIFIER");
        expect("IDENTIFIER");
        expect("OPERATOR");
        expect("OPERATOR");
        expect("OPERATOR");
        while (currentToken() != null && !currentToken().getValue().equals("}")) {
            parseStatement();
        }
        expect("OPERATOR");
    }

    private void parseExpression() {
        parseTerm();
        while (currentToken() != null && (currentToken().getValue().equals("+") || currentToken().getValue().equals("-"))) {
            nextToken();
            parseTerm();
        }
    }

    private void parseTerm() {
        parseFactor();
        while (currentToken() != null && (currentToken().getValue().equals("*") || currentToken().getValue().equals("/"))) {
            nextToken();
            parseFactor();
        }
    }

    private void parseFactor() {
        if (currentToken().getType().equals("IDENTIFIER") || currentToken().getType().equals("INTEGER") || currentToken().getType().equals("STRING") || currentToken().getType().equals("BOOLEAN")) {
            nextToken();
        } else if (currentToken().getValue().equals("(")) {
            nextToken();
            parseExpression();
            expect("OPERATOR");
        } else {
            errorHandler.reportError("Unexpected token: " + currentToken().getValue(), 1);
            nextToken(); // Skip the unexpected token
        }
    }

    private void parseIfStatement() {
        expect("KEYWORD");
        expect("OPERATOR");
        parseExpression();
        expect("OPERATOR");
        while (currentToken() != null && !currentToken().getValue().equals("}")) {
            parseStatement();
        }
        expect("OPERATOR");
    }

    private void parseWhileStatement() {
        expect("KEYWORD");
        expect("OPERATOR");
        parseExpression();
        expect("OPERATOR");
        while (currentToken() != null && !currentToken().getValue().equals("}")) {
            parseStatement();
        }
        expect("OPERATOR");
    }

    private void parseReturnStatement() {
        expect("KEYWORD");
        parseExpression();
        expect("OPERATOR");
    }

    private void parseStatement() {
        if (currentToken().getType().equals("KEYWORD")) {
            if (currentToken().getValue().equals("_if")) {
                parseIfStatement();
            } else if (currentToken().getValue().equals("_while")) {
                parseWhileStatement();
            } else if (currentToken().getValue().equals("_return")) {
                parseReturnStatement();
            } else {
                parseDeclaration();
            }
        } else {
            parseExpression();
            expect("OPERATOR");
        }
    }
}