package org.rebaze.integrity.view;

/**
 *
 */
public interface View<T>
{
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
     * Creates a view update template.
     * Used to feed update into view  using {@link #applyUpdate(ViewUpdate)}.
     *
     * @return a fesh update builder.
     */
    ViewUpdate createUpdate();

    /**
     * The amount of updates done with this used (metrics only).
     * @return
     */
    long getUpdates();

    ViewDefinition getDefinition();
}
