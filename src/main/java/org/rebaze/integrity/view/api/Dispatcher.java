package org.rebaze.integrity.view.api;

public interface Dispatcher<T> {
    void register(View v);
    void dispatch(T[] g);
}

