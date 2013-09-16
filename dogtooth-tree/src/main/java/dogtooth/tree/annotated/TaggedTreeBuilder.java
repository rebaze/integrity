package dogtooth.tree.annotated;

import dogtooth.tree.TreeBuilder;

public interface TaggedTreeBuilder extends TreeBuilder {
    TreeBuilder tag( Tag tag );
}
