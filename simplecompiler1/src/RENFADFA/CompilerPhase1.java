//package RENFADFA;
//import java.util.*;
//import java.util.List;
//import java.util.regex.Pattern;
//class Parser {
//    private final ErrorHandler errorHandler;
//    private final SymbolTable symbolTable;
//    private final IntermediateCodeGenerator tacGenerator;
//
//    public Parser(ErrorHandler errorHandler, SymbolTable symbolTable, IntermediateCodeGenerator tacGenerator) {
//        this.errorHandler = errorHandler;
//        this.symbolTable = symbolTable;
//        this.tacGenerator = tacGenerator;
//    }
//
//    public void parse(String code, LexicalAnalyzer lexer) {
//        lexer.analyze(code, symbolTable, errorHandler);
//
//        String[] lines = code.split("\n");
//        for (int lineNumber = 0; lineNumber < lines.length; lineNumber++) {
//            String line = lines[lineNumber].trim();
//            if (line.isEmpty()) continue;
//
//            if (!isValidStatement(line)) {
//                errorHandler.addError(lineNumber + 1, "Invalid syntax: '" + line + "'");
//            } else {
//                tacGenerator.generate(line);  // Generate TAC if valid
//            }
//        }
//    }
//
//    private boolean isValidStatement(String line) {
//        String assignmentPattern = "^[a-z][a-zA-Z0-9_]*\\s*=\\s*\\d+;$";
//        String ifPattern = "^_if\\s+[a-z][a-zA-Z0-9_]*\\s*\\{?$";
//        String elsePattern = "^_else\\s*\\{?$";
//
//        return line.matches(assignmentPattern) || line.matches(ifPattern) || line.matches(elsePattern);
//    }
//}
//
//
//class State {
//    int id;
//    Map<Character, List<Integer>> transitions = new HashMap<>();
//    boolean isFinal;
//
//    State(int id, boolean isFinal) {
//        this.id = id;
//        this.isFinal = isFinal;
//    }
//
//    void addTransition(char symbol, int nextState) {
//        transitions.computeIfAbsent(symbol, k -> new ArrayList<>()).add(nextState);
//    }
//}
//
//class NFA {
//    List<State> states = new ArrayList<>();
//
//    NFA() {
//        createNFA();
//    }
//
//    void createNFA() {
//        State q0 = new State(0, false);
//        State q1 = new State(1, true);
//        State q2 = new State(2, true);
//        
//        q0.addTransition('_', 1);
//        q0.addTransition('a', 2);
//        
//        states.add(q0);
//        states.add(q1);
//        states.add(q2);
//    }
//
//    void displayTransitions() {
//        System.out.println("NFA Transition Table:");
//        for (State state : states) {
//            for (Map.Entry<Character, List<Integer>> entry : state.transitions.entrySet()) {
//                System.out.println("State " + state.id + " --" + entry.getKey() + "--> " + entry.getValue());
//            }
//        }
//    }
//}
//
//class DFA {
//    Map<Set<Integer>, Integer> stateMapping = new HashMap<>();
//    List<State> states = new ArrayList<>();
//    int stateCounter = 0;
//
//    DFA(NFA nfa) {
//        convertToDFA(nfa);
//    }
//
//    void convertToDFA(NFA nfa) {
//        Queue<Set<Integer>> queue = new LinkedList<>();
//        Set<Integer> startState = new HashSet<>();
//        startState.add(0);
//        queue.add(startState);
//        stateMapping.put(startState, stateCounter++);
//
//        while (!queue.isEmpty()) {
//            Set<Integer> currentSet = queue.poll();
//            int currentStateId = stateMapping.get(currentSet);
//            
//            // Determine if this DFA state should be final
//            boolean isFinalState = false;
//            for (int stateId : currentSet) {
//                if (nfa.states.get(stateId).isFinal) {
//                    isFinalState = true;
//                    break;
//                }
//            }
//            
//            State newState = new State(currentStateId, isFinalState);
//            states.add(newState);
//
//            Map<Character, Set<Integer>> transitions = new HashMap<>();
//            for (int stateId : currentSet) {
//                State nfaState = nfa.states.get(stateId);
//                for (Map.Entry<Character, List<Integer>> entry : nfaState.transitions.entrySet()) {
//                    transitions.computeIfAbsent(entry.getKey(), k -> new HashSet<>()).addAll(entry.getValue());
//                }
//            }
//
//            for (Map.Entry<Character, Set<Integer>> entry : transitions.entrySet()) {
//                char symbol = entry.getKey();
//                Set<Integer> targetSet = entry.getValue();
//
//                if (!stateMapping.containsKey(targetSet)) {
//                    stateMapping.put(targetSet, stateCounter++);
//                    queue.add(targetSet);
//                }
//                newState.addTransition(symbol, stateMapping.get(targetSet));
//            }
//        }
//    }
//
//
//    void displayTransitions() {
//        System.out.println("DFA Transition Table:");
//        for (State state : states) {
//            for (Map.Entry<Character, List<Integer>> entry : state.transitions.entrySet()) {
//                System.out.println("State " + state.id + " --" + entry.getKey() + "--> " + entry.getValue());
//            }
//        }
//    }
//}
//
//class SymbolTable {
//    private static class SymbolEntry {
//        String name;
//        String type;
//        String scope;
//
//        SymbolEntry(String name, String type, String scope) {
//            this.name = name;
//            this.type = type;
//            this.scope = scope;
//        }
//
//        @Override
//        public String toString() {
//            return "Name: " + name + " | Type: " + type + " | Scope: " + scope;
//        }
//    }
//
//    private final List<SymbolEntry> table = new ArrayList<>();
//
//    void addEntry(String name, String type, String scope) {
//        table.add(new SymbolEntry(name, type, scope));
//    }
//
//    void displayTable() {
//        System.out.println("Symbol Table:");
//        for (SymbolEntry entry : table) {
//            System.out.println(entry);
//        }
//    }
//}
//
//
//
//class ErrorHandler {
//    private final List<String> errors = new ArrayList<>();
//
//    void addError(int line, String message) {
//        errors.add("Error at line " + line + ": " + message);
//    }
//
//    void displayErrors() {
//        if (errors.isEmpty()) {
//            System.out.println("No syntax errors found.");
//        } else {
//            System.out.println("Syntax Errors:");
//            for (String error : errors) {
//                System.out.println(error);
//            }
//        }
//    }
//}
//
//class LexicalAnalyzer {
//    private static final Set<String> DATA_TYPES = Set.of("int", "string", "bool");
//    private static final String IDENTIFIER_PATTERN = "^[a-zA-Z_][a-zA-Z0-9_]*$";
//    private static final String NUMBER_PATTERN = "^[0-9]+$";
//    private static final Set<String> KEYWORDS = Set.of("_if", "_else", "_while", "_return");
//    private static final Set<String> SYMBOLS = Set.of("{", "}", "=", ";", "(", ")");
//    private final List<String> tokens = new ArrayList<>();
//
//    void analyze(String input, SymbolTable symbolTable, ErrorHandler errorHandler) {
//        tokens.clear();
//        input = preprocess(input);
//        String[] lines = input.split("\n");
//
//        String lastKeyword = null;
//        for (int lineNumber = 0; lineNumber < lines.length; lineNumber++) {
//            String line = lines[lineNumber].trim();
//            if (line.isEmpty()) continue;
//
//            String[] parts = line.split("\\s+");
//            for (String part : parts) {
//                if (KEYWORDS.contains(part)) {
//                    tokens.add("KEYWORD: " + part);
//                    lastKeyword = null;
//                } else if (DATA_TYPES.contains(part)) {
//                    tokens.add("DATA TYPE: " + part);
//                    lastKeyword = part;
//                } else if (SYMBOLS.contains(part)) {
//                    tokens.add("SYMBOL: " + part);
//                } else {
//                    String cleanedPart = part.replaceAll(";$", "");
//
//                    if (cleanedPart.matches(IDENTIFIER_PATTERN)) {
//                        tokens.add("IDENTIFIER: " + cleanedPart);
//                        String type = (lastKeyword != null) ? lastKeyword : "Variable";
//                        symbolTable.addEntry(cleanedPart, type, "global");
//                        lastKeyword = null;
//                    } else if (cleanedPart.matches(NUMBER_PATTERN)) {
//                        tokens.add("NUMBER: " + cleanedPart);
//                    } else {
//                        tokens.add("UNKNOWN: " + part);
//                        errorHandler.addError(lineNumber + 1, "Unrecognized token: '" + part + "'");
//                    }
//                }
//            }
//        }
//    }
//
//
//    private String preprocess(String input) {
//        input = input.toLowerCase();
//        input = input.replaceAll("//.*", "").replaceAll("/\\*.*?\\*/", "");
//        return input.trim();
//    }
//
//    void displayTokens() {
//        System.out.println("Tokens:");
//        for (String token : tokens) {
//            System.out.println(token);
//        }
//    }
//}
//
//class IntermediateCodeGenerator {
//    private final List<String> tacInstructions = new ArrayList<>();
//    private int tempVarCounter = 0;
//
//    public void generate(String statement) {
//        String[] tokens = statement.split("\\s+");
//
//        if (statement.contains("=")) {  // Assignment Statement
//            String var = tokens[0];
//            String value = tokens[2].replace(";", "");
//            String tempVar = getTempVar();
//            tacInstructions.add(tempVar + " = " + value);
//            tacInstructions.add(var + " = " + tempVar);
//        } 
//        else if (statement.startsWith("_if")) {  // If Condition
//            String conditionVar = tokens[1];
//            String label = "L" + tacInstructions.size();
//            tacInstructions.add("if " + conditionVar + " goto " + label);
//            tacInstructions.add("goto L" + (tacInstructions.size() + 1));
//            tacInstructions.add(label + ":");
//        } 
//        else if (statement.startsWith("_else")) {  // Else Condition
//            String label = "L" + tacInstructions.size();
//            tacInstructions.add("goto " + label);
//            tacInstructions.add(label + ":");
//        }
//    }
//
//    private String getTempVar() {
//        return "t" + tempVarCounter++;
//    }
//
//    public void displayTAC() {
//        System.out.println("\nIntermediate Three-Address Code (TAC):");
//        for (String instruction : tacInstructions) {
//            System.out.println(instruction);
//        }
//    }
//}
//public class CompilerPhase1 {
////    public static void main(String[] args) {
////        NFA nfa = new NFA();
////        nfa.displayTransitions();
////
////        DFA dfa = new DFA(nfa);
////        dfa.displayTransitions();
////
////        SymbolTable symbolTable = new SymbolTable();
////        LexicalAnalyzer lexer = new LexicalAnalyzer();
////        ErrorHandler errorHandler = new ErrorHandler();
////        IntermediateCodeGenerator tacGenerator = new IntermediateCodeGenerator();
////        Parser parser = new Parser(errorHandler, symbolTable, tacGenerator);
////
////        String code = """
////            int x;
////            _if y {
////            x = 10;
////            _else {
////            x = 20;
////            """;
////
////        parser.parse(code, lexer);
////        errorHandler.displayErrors();
////        tacGenerator.displayTAC();  // Display the generated TAC
////    }
//}
//
//
//
//
