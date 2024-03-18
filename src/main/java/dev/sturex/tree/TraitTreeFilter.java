package dev.sturex.tree;

import dev.sturex.feature.Descriptor;
import dev.sturex.feature.Feature;
import dev.sturex.fsm.FeaturedState;
import dev.sturex.fsm.Stateful;

import java.util.*;
import java.util.function.Predicate;

public class TraitTreeFilter {
    private final Map<Object, List<Predicate<? super Map<? extends Enum<?>, ? extends Descriptor<?>>>>> map = new HashMap<>();

    public <E extends Enum<E>> List<Predicate<? super Map<? extends Enum<?>, ? extends Descriptor<?>>>> getFilter(E e) {
        return map.getOrDefault(e, Collections.emptyList());
    }

    public <D, E extends Enum<E>> TraitTreeFilter and(E e, Feature<D, ? extends Stateful<? extends FeaturedState<?>>> feature, Predicate<D> predicate) {
        map.computeIfAbsent(e, o -> new ArrayList<>()).add(m -> feature.test(m, predicate));
        return this;
    }

    public <D, E extends Enum<E>> TraitTreeFilter and(Collection<E> collection, Feature<D, ? extends Stateful<? extends FeaturedState<?>>> feature, Predicate<D> predicate) {
        collection.forEach(e -> and(e, feature, predicate));
        return this;
    }
}
