package dev.sturex.tree;

import dev.sturex.feature.Descriptor;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Edge {

    Edge() {
    }

    private final List<Map<? extends Enum<?>, ? extends Descriptor<?>>> descriptors = new ArrayList<>();

    public void addDescriptors(List<Descriptor<?>> descriptors) {
        Map<? extends Enum<?>, ? extends Descriptor<?>> collect = descriptors.stream()
                .collect(Collectors.toMap(Descriptor::name, descriptor -> descriptor));
        this.descriptors.add(collect);
    }

    public void addAllDescriptors(List<Map<? extends Enum<?>, ? extends Descriptor<?>>> descriptors) {
        this.descriptors.addAll(descriptors);
    }

    public Stream<Map<? extends Enum<?>, ? extends Descriptor<?>>> descriptorStream() {
        return descriptors.stream();
    }

    public void forEachFiltering(Predicate<? super Map<? extends Enum<?>, ? extends Descriptor<?>>> filter,
                                 Consumer<? super Map<? extends Enum<?>, ? extends Descriptor<?>>> consumer) {
        descriptors.stream().filter(filter).forEach(consumer);
    }

    public Stream<Map<? extends Enum<?>, ? extends Descriptor<?>>> filtering(List<Predicate<? super Map<? extends Enum<?>, ? extends Descriptor<?>>>> filters) {
        return descriptors.stream().filter(map -> filters.stream().allMatch(predicate -> predicate.test(map)));
    }

    public int size() {
        return descriptors.size();
    }

    @Override
    public String toString() {
        return "size=" + size();
    }

}
