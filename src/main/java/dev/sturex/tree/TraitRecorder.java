package dev.sturex.tree;

import dev.sturex.feature.Descriptor;
import dev.sturex.fsm.FeaturedState;
import dev.sturex.fsm.Stateful;

import java.util.List;
import java.util.stream.Collectors;

public class TraitRecorder<E extends Enum<E> & FeaturedState<S>, S extends Stateful<E>> {
    private final Node<E> root;
    private Node<E> parentNode;

    private final TraitTree<E> traitTree;

    public TraitRecorder(E e) {
        traitTree = new TraitTree<>();
        root = traitTree.getOrCreate(e);
    }

    public void reset() {
        parentNode = root;
    }

    public void accept(S stateful) {
        E state = stateful.getState();
        Node<E> childNode = traitTree.getOrCreate(state);
        Edge edge = parentNode.getOrConnect(childNode);
        List<Descriptor<?>> descriptors = state.getFeatures().stream()
                .map(feature -> feature.compute(stateful))
                .collect(Collectors.toList());
        if (!descriptors.isEmpty()) {
            edge.addDescriptors(descriptors);
        }
        parentNode = childNode;
    }

    public TraitTree<E> getTraitTree() {
        return traitTree;
    }

}
