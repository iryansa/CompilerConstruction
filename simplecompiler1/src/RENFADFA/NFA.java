package RENFADFA;
import java.util.*;

public class NFA {
    private static int nextStateId = 0;

    static class State {
        int id;
        Map<Character, Set<State>> transitions;

        State() {
            this.id = nextStateId++;
            this.transitions = new HashMap<>();
        }

        void addTransition(char symbol, State nextState) {
            transitions.computeIfAbsent(symbol, k -> new HashSet<>()).add(nextState);
        }

        Set<State> getTransitions(char symbol) {
            return transitions.getOrDefault(symbol, Collections.emptySet());
        }
    }

    private State startState;
    private Set<State> acceptStates;

    public NFA() {
        this.startState = new State();
        this.acceptStates = new HashSet<>();
    }

    public static NFA createNFAForChar(char ch) {
        NFA nfa = new NFA();
        State start = nfa.startState;
        State accept = new State();
        start.addTransition(ch, accept);
        nfa.acceptStates.add(accept);
        return nfa;
    }

    public static NFA concatenate(NFA nfa1, NFA nfa2) {
        NFA result = new NFA();
        result.startState = nfa1.startState;

        for (State acceptState : nfa1.acceptStates) {
            acceptState.addTransition('\0', nfa2.startState);
        }

        result.acceptStates = nfa2.acceptStates;
        return result;
    }

    public static NFA union(NFA nfa1, NFA nfa2) {
        NFA result = new NFA();
        State start = result.startState;
        State accept = new State();

        start.addTransition('\0', nfa1.startState);
        start.addTransition('\0', nfa2.startState);

        for (State state : nfa1.acceptStates) {
            state.addTransition('\0', accept);
        }

        for (State state : nfa2.acceptStates) {
            state.addTransition('\0', accept);
        }

        result.acceptStates.add(accept);
        return result;
    }

    public static NFA kleeneStar(NFA nfa) {
        NFA result = new NFA();
        State start = result.startState;
        State accept = new State();

        start.addTransition('\0', nfa.startState);
        start.addTransition('\0', accept);

        for (State state : nfa.acceptStates) {
            state.addTransition('\0', nfa.startState);
            state.addTransition('\0', accept);
        }

        result.acceptStates.add(accept);
        return result;
    }

    // Public method to get the start state
    public State getStartState() {
        return startState;
    }

    // Public method to get the accept states
    public Set<State> getAcceptStates() {
        return acceptStates;
    }

    // Public method to get transitions from a state
    public Set<State> getTransitions(State state, char symbol) {
        return state.getTransitions(symbol);
    }
}