package dev.sturex.rec;

import dev.sturex.fsm.StateMachine;
import dev.sturex.fsm.Stateful;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ScenarioReplayer<Event extends Enum<Event>, State extends Enum<State>, S extends Stateful<State>, C> {
    private final StateMachine<Event, State, S, C> stateMachine;

    public ScenarioReplayer(StateMachine<Event, State, S, C> stateMachine) {
        this.stateMachine = stateMachine;
    }

    public void play(S s,
                     Scenario<Event, C> scenario,
                     Consumer<S> beginConsumer,
                     Consumer<S> stepConsumer,
                     Consumer<S> endConsumer,
                     Predicate<S> filter) {
        scenario.onBegin((scenarioStep, c) -> {
            stateMachine.apply(s, scenarioStep.event(), scenarioStep.eventIdx(), c);
            if (filter.test(s)) {
                beginConsumer.accept(s);
            }
        });
        scenario.onEachStep((scenarioStep, c) -> {
            stateMachine.apply(s, scenarioStep.event(), scenarioStep.eventIdx(), c);
            if (filter.test(s)) {
                stepConsumer.accept(s);
            }
        });
        scenario.onEnd((scenarioStep, c) -> endConsumer.accept(s));
    }

    public void play(S s,
                     Scenario<Event, C> scenario,
                     Consumer<S> beginConsumer,
                     Consumer<S> stepConsumer,
                     Consumer<S> endConsumer) {
        play(s, scenario, beginConsumer, stepConsumer, endConsumer, s1 -> true);
    }

}
