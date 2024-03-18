package dev.sturex.feature;

import dev.sturex.fsm.FeaturedState;
import dev.sturex.fsm.Stateful;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class Feature<E, S extends Stateful<? extends FeaturedState<S>>> {

    private final Enum<?> name;

    private final Function<S, Descriptor<E>> func;

    public Feature(Enum<?> name, Function<S, Descriptor<E>> func) {
        this.name = name;
        this.func = func;
    }

    public Enum<?> name() {
        return name;
    }

    public Descriptor<E> compute(S stateful) {
        return func.apply(stateful);
    }

    public boolean test(Map<? extends Enum<?>, ? extends Descriptor<?>> map, Predicate<E> predicate) {
        return Optional.ofNullable((Descriptor<E>) map.get(name)).map(d -> predicate.test(d.value())).orElse(false);
    }

    public boolean test(Descriptor<?> descriptor, Predicate<E> predicate) {
        return predicate.test((E) descriptor.value());
    }
}
