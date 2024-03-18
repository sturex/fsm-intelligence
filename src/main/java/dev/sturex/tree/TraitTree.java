package dev.sturex.tree;

import dev.sturex.feature.CategoricalFeature;
import dev.sturex.feature.Descriptor;
import dev.sturex.feature.Feature;
import dev.sturex.fsm.Stateful;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class TraitTree<E extends Enum<E>> {
    private final Map<E, Node<E>> nodeMap;

    TraitTree() {
        nodeMap = new HashMap<>();
    }

    public TraitTree(Map<E, Node<E>> nodeMap) {
        this.nodeMap = nodeMap;
    }

    public Trait[] computeTraits() {
        return new Trait[]{};
    }

    Node<E> getOrCreate(E e) {
        return nodeMap.computeIfAbsent(e, e1 -> new Node<E>(e));
    }

    public TraitTree<E> copyFiltering(TraitTreeFilter traitTreeFilter) {
        Map<E, Node<E>> nodeMapCopy = new HashMap<>();
        nodeMap.forEach((e, srcNode) -> {
            List<Predicate<? super Map<? extends Enum<?>, ? extends Descriptor<?>>>> filters = traitTreeFilter.getFilter(e);
            srcNode.forEachTransition((eTarget, edge) -> {
                List<Map<? extends Enum<?>, ? extends Descriptor<?>>> descriptorsList = edge.filtering(filters).toList();
                if (!descriptorsList.isEmpty()) {
                    Node<E> nodeCopy = nodeMapCopy.computeIfAbsent(e, e1 -> nodeMap.get(e1).copy());
                    Node<E> nodeTarget = nodeMapCopy.computeIfAbsent(eTarget, e1 -> nodeMap.get(e1).copy());
                    Edge edgeCopy = nodeCopy.getOrConnect(nodeTarget);
                    edgeCopy.addAllDescriptors(descriptorsList);
                }
            });
        });
        return new TraitTree<>(nodeMapCopy);
    }

    @Override
    public String toString() {
        return nodeMap.entrySet().stream().map(e -> e.getKey() + ": " + e.getValue().size()).collect(Collectors.joining(", "));
    }

    public <N extends Enum<N>> Map<N, Double> getProbabilityMap(E e, CategoricalFeature<N, ? extends Stateful<?>> feature) {
        Node<E> eNode = nodeMap.get(e);
        if (eNode == null) {
            return Collections.emptyMap();
        } else {
            long size = eNode.size();
            return eNode.transitions().map(Map.Entry::getValue).map(edge -> edge.descriptorStream().map(map -> map.get(feature.name())))

                    .flatMap(stream -> stream.map(descriptor -> (N) descriptor.value()))
                    .collect(Collectors.groupingBy(d -> d, Collectors.collectingAndThen(Collectors.counting(), cnt -> (double) cnt / size)));
        }
    }

    public <N extends Enum<N>> Map<N, Double> getConditionalProbabilityMap(E e,
                                                                           CategoricalFeature<N, ? extends Stateful<E>> feature,
                                                                           Feature<N, ? extends Stateful<E>> condFeature,
                                                                           Predicate<N> predicate) {
        Node<E> eNode = nodeMap.get(e);
        if (eNode == null) {
            return Collections.emptyMap();
        } else {
            long size = eNode.size();
            return eNode.transitions().map(Map.Entry::getValue).map(edge -> edge.descriptorStream().map(map -> map.get(feature.name())))
                    .flatMap(stream -> stream.map(descriptor -> (N) descriptor.value()))
                    .collect(Collectors.groupingBy(d -> d, Collectors.collectingAndThen(Collectors.counting(), cnt -> (double) cnt / size)));
        }
    }

    public <N extends Enum<N>> Map<N, Double> getProbabilityMap(Stateful<E> stateful, CategoricalFeature<N, ? extends Stateful<?>> feature) {
        E state = stateful.getState();



        return getProbabilityMap(state, feature);
    }
}
