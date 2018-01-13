package io.github.flaz14.publicapi;

public interface Traceable<T> {
    void doBusinessLogic(T t);

    T calculateBusinessValue();
}