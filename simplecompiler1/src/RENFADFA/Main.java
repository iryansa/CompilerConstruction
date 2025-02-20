package RENFADFA;
import java.io.*;
import java.nio.file.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Read the source code from a file
            String sourceCode = new String(Files.readAllBytes(Paths.get("example.irons")));

            // Tokenize the source code
            List<LexicalAnalyzer.Token> tokens = LexicalAnalyzer.tokenize(sourceCode);

            // Print the tokens
            for (LexicalAnalyzer.Token token : tokens) {
                System.out.println(token);
            }

            // Create a symbol table
            SymbolTable symbolTable = new SymbolTable();

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

            // Create an error handler
            ErrorHandler errorHandler = new ErrorHandler();

            // Example of reporting an error
            errorHandler.reportError("Variable 'x' is not defined.", 5);

            // Display all errors
            errorHandler.displayErrors();
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }
}