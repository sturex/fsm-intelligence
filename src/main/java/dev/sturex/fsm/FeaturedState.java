package dev.sturex.fsm;

import dev.sturex.feature.Feature;

import java.util.Set;

public interface FeaturedState<S extends Stateful<? extends FeaturedState<S>>> {

    Set<Feature<? , S>> getFeatures();
}
