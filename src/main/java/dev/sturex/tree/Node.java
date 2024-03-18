package dev.sturex.tree;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class Node<S extends Enum<S>> {

    private final Map<S, Edge> edgeMap = new HashMap<>();
    private final S s;

    Node(S s) {
        this.s = s;
    }

    public void forEachTransition(BiConsumer<? super S, ? super Edge> action) {
        edgeMap.forEach(action);
    }

    public Stream<Map.Entry<S, Edge>> transitions() {
        return edgeMap.entrySet().stream();
    }

    Edge getOrConnect(Node<S> node) {
        return edgeMap.computeIfAbsent(node.s, s1 -> new Edge());
    }

    Node<S> copy() {
        return new Node<>(s);
    }

    public long size() {
        //TODO add field
        return edgeMap.values().stream().mapToInt(Edge::size).sum();
    }

    @Override
    public String toString() {
        return s + ": " + size();
    }
}
