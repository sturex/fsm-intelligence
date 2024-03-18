package dev.sturex.feature;

import dev.sturex.fsm.FeaturedState;
import dev.sturex.fsm.Stateful;

import java.util.function.Function;

public class StringFeature<S extends Stateful<? extends FeaturedState<S>>> extends Feature<String, S> {
    public StringFeature(Enum<?> name, Function<S, String> func) {
        super(name, s -> new Descriptor<>(name, func.apply(s)));
    }
}
