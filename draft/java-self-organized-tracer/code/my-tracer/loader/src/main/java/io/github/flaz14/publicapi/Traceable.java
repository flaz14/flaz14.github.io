package io.github.flaz14.publicapi;

public interface Traceable<T> {
    void processSomething(T t);

    T getSomething();
}