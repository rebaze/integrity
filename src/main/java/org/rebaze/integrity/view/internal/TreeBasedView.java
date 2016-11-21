package org.rebaze.integrity.view.internal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.rebaze.integrity.tree.api.Tree;
import org.rebaze.integrity.view.api.View;
import org.rebaze.integrity.view.api.ViewDefinition;

import java.util.Map;
import java.util.WeakHashMap;

@EqualsAndHashCode
public class TreeBasedView<T> implements View<T>
{
    @Getter
    private final String name;

    @Getter
    private final ViewDefinition definition;

    private Map<Tree,T> state = new WeakHashMap<>();

    @Getter
    private long updates = 0;

    public TreeBasedView(String name, ViewDefinition viewDefinition) {
        this.name = name;
        this.definition = viewDefinition;
        // seed state:
    }

    @Override
    final public synchronized void applyUpdate(ViewUpdate<T> viewUpdates) {
        updates++;
        for (Map.Entry<Tree, T> update : viewUpdates.get()) {
            state.put( update.getKey(),update.getValue() );
        }
        updated();
    }

    @Override
    public void updated() {

    }


    @Override
    public T[] getState( T[] feed ) {
        return state.values().toArray(feed);
    }

    @Override
    public ViewUpdate createUpdate()
    {
        return new ViewUpdate<T>(definition);
    }

}
