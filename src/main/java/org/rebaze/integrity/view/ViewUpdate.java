package org.rebaze.integrity.view;

import org.rebaze.integrity.tree.api.Tree;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ViewUpdate<T> {
    private final ViewDefinition def;

    public ViewUpdate(ViewDefinition viewDefinition) {
        this.def = viewDefinition;
    }

    private final Map<Tree,T> elements = new HashMap<>();

    public void add(Tree mask, T item) {
        if (def.match( mask )) {
            elements.put(mask,item);
        }
    }

    public Set<Map.Entry<Tree, T>> get() {
        return elements.entrySet();
    }
}
