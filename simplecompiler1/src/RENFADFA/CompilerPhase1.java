package RENFADFA;
import java.util.*;

class State {
    int id;
    Map<Character, List<Integer>> transitions = new HashMap<>();
    boolean isFinal;

    State(int id, boolean isFinal) {
        this.id = id;
        this.isFinal = isFinal;
    }

    void addTransition(char symbol, int nextState) {
        transitions.computeIfAbsent(symbol, k -> new ArrayList<>()).add(nextState);
    }
}

class NFA {
    List<State> states = new ArrayList<>();

    NFA() {
        createNFA();
    }

    void createNFA() {
        State q0 = new State(0, false);
        State q1 = new State(1, true);
        State q2 = new State(2, true);
        
        q0.addTransition('_', 1);
        q0.addTransition('a', 2);
        
        states.add(q0);
        states.add(q1);
        states.add(q2);
    }

    void displayTransitions() {
        System.out.println("NFA Transition Table:");
        for (State state : states) {
            for (Map.Entry<Character, List<Integer>> entry : state.transitions.entrySet()) {
                System.out.println("State " + state.id + " --" + entry.getKey() + "--> " + entry.getValue());
            }
        }
    }
}

class DFA {
    Map<Set<Integer>, Integer> stateMapping = new HashMap<>();
    List<State> states = new ArrayList<>();
    int stateCounter = 0;

    DFA(NFA nfa) {
        convertToDFA(nfa);
    }

    void convertToDFA(NFA nfa) {
        Queue<Set<Integer>> queue = new LinkedList<>();
        Set<Integer> startState = new HashSet<>();
        startState.add(0);
        queue.add(startState);
        stateMapping.put(startState, stateCounter++);

        while (!queue.isEmpty()) {
            Set<Integer> currentSet = queue.poll();
            int currentStateId = stateMapping.get(currentSet);
            
            // Determine if this DFA state should be final
            boolean isFinalState = false;
            for (int stateId : currentSet) {
                if (nfa.states.get(stateId).isFinal) {
                    isFinalState = true;
                    break;
                }
            }
            
            State newState = new State(currentStateId, isFinalState);
            states.add(newState);

            Map<Character, Set<Integer>> transitions = new HashMap<>();
            for (int stateId : currentSet) {
                State nfaState = nfa.states.get(stateId);
                for (Map.Entry<Character, List<Integer>> entry : nfaState.transitions.entrySet()) {
                    transitions.computeIfAbsent(entry.getKey(), k -> new HashSet<>()).addAll(entry.getValue());
                }
            }

            for (Map.Entry<Character, Set<Integer>> entry : transitions.entrySet()) {
                char symbol = entry.getKey();
                Set<Integer> targetSet = entry.getValue();

                if (!stateMapping.containsKey(targetSet)) {
                    stateMapping.put(targetSet, stateCounter++);
                    queue.add(targetSet);
                }
                newState.addTransition(symbol, stateMapping.get(targetSet));
            }
        }
    }


    void displayTransitions() {
        System.out.println("DFA Transition Table:");
        for (State state : states) {
            for (Map.Entry<Character, List<Integer>> entry : state.transitions.entrySet()) {
                System.out.println("State " + state.id + " --" + entry.getKey() + "--> " + entry.getValue());
            }
        }
    }
}

class SymbolTable {
    private static class SymbolEntry {
        String name;
        String type;
        String scope;

        SymbolEntry(String name, String type, String scope) {
            this.name = name;
            this.type = type;
            this.scope = scope;
        }

        @Override
        public String toString() {
            return "Name: " + name + " | Type: " + type + " | Scope: " + scope;
        }
    }

    private final List<SymbolEntry> table = new ArrayList<>();

    void addEntry(String name, String type, String scope) {
        table.add(new SymbolEntry(name, type, scope));
    }

    void displayTable() {
        System.out.println("Symbol Table:");
        for (SymbolEntry entry : table) {
            System.out.println(entry);
        }
    }
}



class ErrorHandler {
    private final List<String> errors = new ArrayList<>();

    void addError(int line, String message) {
        errors.add("Error at line " + line + ": " + message);
    }

    void displayErrors() {
        if (errors.isEmpty()) {
            System.out.println("No syntax errors found.");
        } else {
            System.out.println("Syntax Errors:");
            for (String error : errors) {
                System.out.println(error);
            }
        }
    }
}

class LexicalAnalyzer {
    private static final Set<String> KEYWORDS = Set.of("_if", "_else", "_while", "_return");
    private static final Set<String> DATA_TYPES = Set.of("int", "string", "bool");
    private static final String IDENTIFIER_PATTERN = "^[a-z][a-zA-Z0-9_]*$";
    private static final String NUMBER_PATTERN = "^[0-9]+$";
    private final List<String> tokens = new ArrayList<>();

    void analyze(String input, SymbolTable symbolTable, ErrorHandler errorHandler) {
        tokens.clear();
        input = preprocess(input);
        String[] lines = input.split("\n");

        String lastKeyword = null;
        for (int lineNumber = 0; lineNumber < lines.length; lineNumber++) {
            String line = lines[lineNumber].trim();
            if (line.isEmpty()) continue; // Skip empty lines

            String[] parts = line.split("\\s+");
            for (String part : parts) {
                if (KEYWORDS.contains(part)) {
                    tokens.add("KEYWORD: " + part);
                    lastKeyword = null;
                } else if (DATA_TYPES.contains(part)) {
                    tokens.add("DATA TYPE: " + part);
                    lastKeyword = part;
                } else if (part.matches(IDENTIFIER_PATTERN)) {
                    tokens.add("IDENTIFIER: " + part);
                    String type = (lastKeyword != null) ? lastKeyword : "Variable";
                    symbolTable.addEntry(part, type, "global");
                    lastKeyword = null;
                } else if (part.matches(NUMBER_PATTERN)) {
                    tokens.add("NUMBER: " + part);
                } else {
                    tokens.add("UNKNOWN: " + part);
                    errorHandler.addError(lineNumber + 1, "Unrecognized token: '" + part + "'");
                }
            }
        }
    }

    private String preprocess(String input) {
        input = input.toLowerCase();
        input = input.replaceAll("//.*", "").replaceAll("/\\*.*?\\*/", "");
        return input.trim();
    }

    void displayTokens() {
        System.out.println("Tokens:");
        for (String token : tokens) {
            System.out.println(token);
        }
    }
}


public class CompilerPhase1 {
    public static void main(String[] args) {
        NFA nfa = new NFA();
        nfa.displayTransitions();

        DFA dfa = new DFA(nfa);
        dfa.displayTransitions();

        SymbolTable symbolTable = new SymbolTable();
        LexicalAnalyzer lexer = new LexicalAnalyzer();
        ErrorHandler errorHandler = new ErrorHandler();

        String code = """
            int x
            _if y _return 42 else_var
            x = 10
            """;

        lexer.analyze(code, symbolTable, errorHandler);
        lexer.displayTokens();
        symbolTable.displayTable();
        errorHandler.displayErrors();
    }
}

