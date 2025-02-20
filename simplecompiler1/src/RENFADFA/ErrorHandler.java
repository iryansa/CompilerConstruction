package RENFADFA;

import java.util.ArrayList;
import java.util.List;

public class ErrorHandler {
    private List<String> errors;

    public ErrorHandler() {
        this.errors = new ArrayList<>();
    }

    public void reportError(String message, int lineNumber) {
        String error = "Error at line " + lineNumber + ": " + message;
        errors.add(error);
        System.err.println(error);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public void displayErrors() {
        if (hasErrors()) {
            System.err.println("Compilation failed with the following errors:");
            for (String error : errors) {
                System.err.println(error);
            }
        } else {
            System.out.println("No errors found.");
        }
    }
}