package RENFADFA;
import java.util.*;
import java.util.stream.Collectors;

public class DFA {
    private static int nextStateId = 0;

    private static class State {
        int id;
        Map<Character, State> transitions;
        boolean isAcceptState;

        State(Set<NFA.State> nfaStates, boolean isAcceptState) {
            this.id = nextStateId++;
            this.transitions = new HashMap<>();
            this.isAcceptState = isAcceptState;
        }

        void addTransition(char symbol, State nextState) {
            transitions.put(symbol, nextState);
        }

        State getTransition(char symbol) {
            return transitions.get(symbol);
        }
    }

    private State startState;
    private Set<State> acceptStates;

    public DFA(NFA nfa) {
        this.startState = new State(Collections.singleton(nfa.getStartState()), false);
        this.acceptStates = new HashSet<>();
        constructDFA(nfa);
    }

    private void constructDFA(NFA nfa) {
        Queue<Set<NFA.State>> queue = new LinkedList<>();
        Map<Set<NFA.State>, State> dfaStates = new HashMap<>();

        // Start with the epsilon closure of the NFA start state
        Set<NFA.State> startClosure = epsilonClosure(nfa.getStartState());
        State dfaStartState = new State(startClosure, isAcceptState(startClosure, nfa.getAcceptStates()));
        dfaStates.put(startClosure, dfaStartState);
        queue.add(startClosure);

        while (!queue.isEmpty()) {
            Set<NFA.State> currentSet = queue.poll();
            State currentState = dfaStates.get(currentSet);

            for (char symbol : getAlphabet(currentSet)) {
                Set<NFA.State> nextSet = move(currentSet, symbol);
                nextSet = epsilonClosure(nextSet);

                if (!dfaStates.containsKey(nextSet)) {
                    State nextState = new State(nextSet, isAcceptState(nextSet, nfa.getAcceptStates()));
                    dfaStates.put(nextSet, nextState);
                    queue.add(nextSet);
                }

                currentState.addTransition(symbol, dfaStates.get(nextSet));
            }
        }

        this.startState = dfaStartState;
        this.acceptStates.addAll(dfaStates.values().stream().filter(s -> s.isAcceptState).collect(Collectors.toSet()));
    }

    private Set<NFA.State> epsilonClosure(NFA.State state) {
        Set<NFA.State> closure = new HashSet<>();
        Stack<NFA.State> stack = new Stack<>();
        stack.push(state);

        while (!stack.isEmpty()) {
            NFA.State current = stack.pop();
            if (!closure.contains(current)) {
                closure.add(current);
                for (NFA.State nextState : current.getTransitions('\0')) {
                    stack.push(nextState);
                }
            }
        }

        return closure;
    }

    private Set<NFA.State> epsilonClosure(Set<NFA.State> states) {
        Set<NFA.State> closure = new HashSet<>();
        for (NFA.State state : states) {
            closure.addAll(epsilonClosure(state));
        }
        return closure;
    }

    private Set<NFA.State> move(Set<NFA.State> states, char symbol) {
        Set<NFA.State> result = new HashSet<>();
        for (NFA.State state : states) {
            result.addAll(state.getTransitions(symbol));
        }
        return result;
    }

    private boolean isAcceptState(Set<NFA.State> states, Set<NFA.State> acceptStates) {
        for (NFA.State state : states) {
            if (acceptStates.contains(state)) {
                return true;
            }
        }
        return false;
    }

    private Set<Character> getAlphabet(Set<NFA.State> states) {
        Set<Character> alphabet = new HashSet<>();
        for (NFA.State state : states) {
            for (char symbol : state.transitions.keySet()) {
                if (symbol != '\0') {
                    alphabet.add(symbol);
                }
            }
        }
        return alphabet;
    }

    public void display() {
        Queue<State> queue = new LinkedList<>();
        Set<State> visited = new HashSet<>();
        queue.add(startState);
        visited.add(startState);

        while (!queue.isEmpty()) {
            State current = queue.poll();
            System.out.println("State " + current.id + (current.isAcceptState ? " (Accept)" : ""));
            for (Map.Entry<Character, State> entry : current.transitions.entrySet()) {
                char symbol = entry.getKey();
                State nextState = entry.getValue();
                if (!visited.contains(nextState)) {
                    queue.add(nextState);
                    visited.add(nextState);
                }
                System.out.println("  On '" + symbol + "' -> State " + nextState.id);
            }
        }
    }
}