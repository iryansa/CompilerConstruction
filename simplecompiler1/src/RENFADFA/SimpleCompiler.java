//package RENFADFA;
//
////import java.util.*;
////import java.util.regex.*;
////
////// Token Types
////enum TokenType {
////    KEYWORD, IDENTIFIER, NUMBER, SYMBOL, UNKNOWN
////}
////
////// Token Representation
////class Token {
////    TokenType type;
////    String value;
////
////    public Token(TokenType type, String value) {
////        this.type = type;
////        this.value = value;
////    }
////
////    @Override
////    public String toString() {
////        return "(" + type + ", '" + value + "')";
////    }
////}
////
////// Lexer for Tokenization
////class Lexer {
////    private static final Pattern TOKEN_PATTERN = Pattern.compile(
////        "(_[a-zA-Z]+)|([a-z][a-zA-Z0-9]*)|(\\d+)|([+\\-*/=(){};,])"
////    );
////
////    public List<Token> tokenize(String input) {
////        List<Token> tokens = new ArrayList<>();
////        Matcher matcher = TOKEN_PATTERN.matcher(input);
////
////        while (matcher.find()) {
////            if (matcher.group(1) != null) {
////                tokens.add(new Token(TokenType.KEYWORD, matcher.group(1)));
////            } else if (matcher.group(2) != null) {
////                tokens.add(new Token(TokenType.IDENTIFIER, matcher.group(2)));
////            } else if (matcher.group(3) != null) {
////                tokens.add(new Token(TokenType.NUMBER, matcher.group(3)));
////            } else if (matcher.group(4) != null) {
////                tokens.add(new Token(TokenType.SYMBOL, matcher.group(4)));
////            } else {
////                tokens.add(new Token(TokenType.UNKNOWN, matcher.group()));
////            }
////        }
////        return tokens;
////    }
////}
////
////// State Representation
////class State {
////    int id;
////    boolean isFinal;
////    Map<Character, List<State>> transitions = new HashMap<>();
////
////    public State(int id, boolean isFinal) {
////        this.id = id;
////        this.isFinal = isFinal;
////    }
////
////    public void addTransition(char symbol, State nextState) {
////        transitions.putIfAbsent(symbol, new ArrayList<>());
////        transitions.get(symbol).add(nextState);
////    }
////}
////
////// NFA Representation
////class NFA {
////    State startState;
////    Set<State> states = new HashSet<>();
////
////    public NFA(State startState) {
////        this.startState = startState;
////        collectStates(startState);
////    }
////
////    private void collectStates(State state) {
////        if (!states.contains(state)) {
////            states.add(state);
////            for (List<State> nextStates : state.transitions.values()) {
////                for (State nextState : nextStates) {
////                    collectStates(nextState);
////                }
////            }
////        }
////    }
////
////    public int getTotalStates() {
////        return states.size();
////    }
////}
////
////// DFA Representation
////class DFA {
////    Map<Integer, Map<Character, Integer>> transitionTable = new HashMap<>();
////    int totalStates;
////
////    public DFA(NFA nfa) {
////        totalStates = nfa.getTotalStates();
////        constructDFA(nfa);
////    }
////
////    private void constructDFA(NFA nfa) {
////        for (State state : nfa.states) {
////            Map<Character, Integer> transitions = new HashMap<>();
////            for (Map.Entry<Character, List<State>> entry : state.transitions.entrySet()) {
////                transitions.put(entry.getKey(), entry.getValue().get(0).id);
////            }
////            transitionTable.put(state.id, transitions);
////        }
////    }
////
////    public void displayTransitionTable() {
////        System.out.println("State Transition Table:");
////        for (var entry : transitionTable.entrySet()) {
////            System.out.print("State " + entry.getKey() + " -> ");
////            System.out.println(entry.getValue());
////        }
////    }
////}
////
////// Main Compiler
////public class SimpleCompiler {
////    public static void main(String[] args) {
////        String input = "_start; _int x = 10; _int y = x + 20; _print(y); _end;";
////        Lexer lexer = new Lexer();
////        List<Token> tokens = lexer.tokenize(input);
////        System.out.println("Tokens: " + tokens);
////        
////        State q0 = new State(0, false);
////        State q1 = new State(1, true);
////        q0.addTransition('x', q1);
////        
////        NFA nfa = new NFA(q0);
////        System.out.println("Total NFA States: " + nfa.getTotalStates());
////        
////        DFA dfa = new DFA(nfa);
////        dfa.displayTransitionTable();
////    }
////}
//
//import java.util.*;
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
//        // Hardcoded NFA for simple expressions (keywords, identifiers, numbers)
//        State q0 = new State(0, false);
//        State q1 = new State(1, true);
//        State q2 = new State(2, true);
//        
//        q0.addTransition('_', 1); // Keywords start with '_'
//        q0.addTransition('a', 2); // Identifiers start with lowercase
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
//            State newState = new State(currentStateId, false);
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
//public class SimpleCompiler {
//    public static void main(String[] args) {
//        NFA nfa = new NFA();
//        nfa.displayTransitions();
//
//        DFA dfa = new DFA(nfa);
//        dfa.displayTransitions();
//    }
//}
//
