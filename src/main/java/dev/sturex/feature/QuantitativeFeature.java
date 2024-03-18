package dev.sturex.feature;

import dev.sturex.fsm.FeaturedState;
import dev.sturex.fsm.Stateful;

import java.util.function.Function;

public class QuantitativeFeature<S extends Stateful<? extends FeaturedState<S>>> extends Feature<Number, S> {
    public QuantitativeFeature(Enum<?> name, Function<S, Number> func) {
        super(name, s -> new Descriptor<>(name, func.apply(s)));
    }
}
