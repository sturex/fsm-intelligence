package dev.sturex.fsm;

import java.util.*;

public class HashMapStateMachine<Event extends Enum<Event>, State extends Enum<State>, S extends Stateful<State>, C> implements StateMachine<Event, State, S, C> {

    private class TransitionData {
        Guard<S, C> guard;
        TransitionFunction<State, S, C> transitionFunction;
        Action<S, C> errorAction;

        public TransitionData(Guard<S, C> guard, TransitionFunction<State, S, C> transitionFunction, Action<S, C> errorAction) {
            this.guard = guard;
            this.transitionFunction = transitionFunction;
            this.errorAction = errorAction;
        }
    }

    private final Map<State, Map<Event, TransitionData>> transitions = new HashMap<>();

    @Override
    public HashMapStateMachine<Event, State, S, C> addTransition(State src,
                                                                 Event event,
                                                                 Guard<S, C> guard,
                                                                 TransitionFunction<State, S, C> transitionFunction,
                                                                 Action<S, C> errorAction) {
        transitions.computeIfAbsent(src, s -> new HashMap<>())
                .put(event, new TransitionData(guard, transitionFunction, errorAction));
        return this;
    }


    @Override
    public void apply(S stateful, Event event, int idx, C context) {
        State state = stateful.getState();
        Map<Event, TransitionData> destinationsByEventMap = Optional.ofNullable(transitions.get(state))
                .orElseThrow(() -> new RuntimeException("The state " + state + " is not registered as source for event=" + event + " with idx=" + idx + "\nstateful=" + stateful + "\ncontext=" + context));
        TransitionData transitionData = Optional.ofNullable(destinationsByEventMap.get(event))
                .orElseThrow(() -> new RuntimeException("The state " + state + " does not accept event " + event + ", stateful=" + stateful + ", context=" + context));
        if (transitionData.guard.check(stateful, idx, context)) {
            State dest = transitionData.transitionFunction.apply(stateful, idx, context);
            stateful.setState(dest);
        } else {
            transitionData.errorAction.apply(stateful, context);
        }
    }

}
