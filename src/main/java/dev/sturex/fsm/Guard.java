package dev.sturex.fsm;

public interface Guard<S extends Stateful<?>, C> {
    boolean check(S s, int idx, C c);
}
