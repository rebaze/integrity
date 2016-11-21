package org.rebaze.integrity.view.api;

import org.rebaze.integrity.tree.api.Tree;

public interface ViewDefinition {

    boolean match(Tree thing);

    int size();
}
