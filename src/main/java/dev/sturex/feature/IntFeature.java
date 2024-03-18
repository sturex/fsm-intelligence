package dev.sturex.feature;

import dev.sturex.fsm.FeaturedState;
import dev.sturex.fsm.Stateful;

import java.util.function.Function;

public class IntFeature<S extends Stateful<? extends FeaturedState<S>>> extends Feature<Integer, S> {
    public IntFeature(Enum<?> name, Function<S, Integer> func) {
        super(name, s -> new Descriptor<>(name, func.apply(s)));
    }
}