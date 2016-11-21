package org.rebaze.integrity.view.api;

import org.rebaze.integrity.view.internal.ViewUpdate;

/**
 *
 */
public interface View<T>
{
    ViewDefinition getDefinition();

    /**
     * Creates a view update template.
     * Used to feed update into view  using {@link #applyUpdate(ViewUpdate)}.
     *
     * @return a fesh update builder.
     */
    ViewUpdate createUpdate();

    /**
     * Actually update this view.
     *
     * @param viewUpdates
     */
    void applyUpdate( ViewUpdate<T> viewUpdates );

    /**
     * Callback method overwriting methods can use to get notified when this view gets updated.
     */
    void updated();

    /**
     * Threadsafe way to get the latest state.
     *
     * @return The current view (copy!)
     */
    T[] getState( T[] feed );

    /**
     * The amount of updates done with this used (metrics only).
     * @return
     */
    long getUpdates();
}
