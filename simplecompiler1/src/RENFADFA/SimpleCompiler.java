package RENFADFA;

import java.util.*;
import java.util.regex.*;

// Token Types
enum TokenType {
    KEYWORD, IDENTIFIER, NUMBER, SYMBOL, UNKNOWN
}

// Token Representation
class Token {
    TokenType type;
    String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return "(" + type + ", '" + value + "')";
    }
}

// Lexer for Tokenization
class Lexer {
    private static final Pattern TOKEN_PATTERN = Pattern.compile(
        "(_[a-zA-Z]+)|([a-z][a-zA-Z0-9]*)|(\\d+)|([+\\-*/=(){};,])"
    );

    public List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        Matcher matcher = TOKEN_PATTERN.matcher(input);

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                tokens.add(new Token(TokenType.KEYWORD, matcher.group(1)));
            } else if (matcher.group(2) != null) {
                tokens.add(new Token(TokenType.IDENTIFIER, matcher.group(2)));
            } else if (matcher.group(3) != null) {
                tokens.add(new Token(TokenType.NUMBER, matcher.group(3)));
            } else if (matcher.group(4) != null) {
                tokens.add(new Token(TokenType.SYMBOL, matcher.group(4)));
            } else {
                tokens.add(new Token(TokenType.UNKNOWN, matcher.group()));
            }
        }
        return tokens;
    }
}

// State Representation
class State {
    int id;
    boolean isFinal;
    Map<Character, List<State>> transitions = new HashMap<>();

    public State(int id, boolean isFinal) {
        this.id = id;
        this.isFinal = isFinal;
    }

    public void addTransition(char symbol, State nextState) {
        transitions.putIfAbsent(symbol, new ArrayList<>());
        transitions.get(symbol).add(nextState);
    }
}

// NFA Representation
class NFA {
    State startState;
    Set<State> states = new HashSet<>();

    public NFA(State startState) {
        this.startState = startState;
        collectStates(startState);
    }

    private void collectStates(State state) {
        if (!states.contains(state)) {
            states.add(state);
            for (List<State> nextStates : state.transitions.values()) {
                for (State nextState : nextStates) {
                    collectStates(nextState);
                }
            }
        }
    }

    public int getTotalStates() {
        return states.size();
    }
}

// DFA Representation
class DFA {
    Map<Integer, Map<Character, Integer>> transitionTable = new HashMap<>();
    int totalStates;

    public DFA(NFA nfa) {
        totalStates = nfa.getTotalStates();
        constructDFA(nfa);
    }

    private void constructDFA(NFA nfa) {
        for (State state : nfa.states) {
            Map<Character, Integer> transitions = new HashMap<>();
            for (Map.Entry<Character, List<State>> entry : state.transitions.entrySet()) {
                transitions.put(entry.getKey(), entry.getValue().get(0).id);
            }
            transitionTable.put(state.id, transitions);
        }
    }

    public void displayTransitionTable() {
        System.out.println("State Transition Table:");
        for (var entry : transitionTable.entrySet()) {
            System.out.print("State " + entry.getKey() + " -> ");
            System.out.println(entry.getValue());
        }
    }
}

// Main Compiler
public class SimpleCompiler {
    public static void main(String[] args) {
        String input = "_start x = 10; _print(x);";
        Lexer lexer = new Lexer();
        List<Token> tokens = lexer.tokenize(input);
        System.out.println("Tokens: " + tokens);
        
        State q0 = new State(0, false);
        State q1 = new State(1, true);
        q0.addTransition('x', q1);
        
        NFA nfa = new NFA(q0);
        System.out.println("Total NFA States: " + nfa.getTotalStates());
        
        DFA dfa = new DFA(nfa);
        dfa.displayTransitionTable();
    }
}
