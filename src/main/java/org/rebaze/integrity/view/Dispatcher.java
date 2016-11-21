package org.rebaze.integrity.view;

public interface Dispatcher<T> {
    void register(View v);
    void dispatch(T[] g);
}

