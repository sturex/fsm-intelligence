package dev.sturex.feature;

import dev.sturex.fsm.FeaturedState;
import dev.sturex.fsm.Stateful;

import java.util.function.Function;

public class DoubleFeature<S extends Stateful<? extends FeaturedState<S>>> extends Feature<Double, S> {
    public DoubleFeature(Enum<?> name, Function<S, Double> func) {
        super(name, s -> new Descriptor<>(name, func.apply(s)));
    }
}