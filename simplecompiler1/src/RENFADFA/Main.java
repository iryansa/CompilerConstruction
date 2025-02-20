package RENFADFA;
import java.io.*;
import java.nio.file.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Create an error handler
            ErrorHandler errorHandler = new ErrorHandler();

            // Read the source code from a file
            String sourceCode = new String(Files.readAllBytes(Paths.get("example.irons")));

            // Tokenize the source code
            LexicalAnalyzer lexer = new LexicalAnalyzer(errorHandler);
            List<LexicalAnalyzer.Token> tokens = lexer.tokenize(sourceCode);

            // Print the tokens
            for (LexicalAnalyzer.Token token : tokens) {
                System.out.println(token);
            }

            // Create a symbol table
            SymbolTable symbolTable = new SymbolTable(errorHandler);

            // Example of adding symbols to the symbol table
            symbolTable.addSymbol("x", "_int");
            symbolTable.addSymbol("y", "_float");
            symbolTable.addSymbol("z", "_string");
            symbolTable.addSymbol("isTrue", "_bool");

            // Enter a new scope (e.g., a function)
            symbolTable.enterScope("main");
            symbolTable.addSymbol("localVar", "_int");

            // Exit the scope
            symbolTable.exitScope();

            // Display the symbol table
            symbolTable.display();

            // Display all errors
            errorHandler.displayErrors();
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }
}