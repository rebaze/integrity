package dogtooth.tree.annotated;

import dogtooth.tree.Tree;

public interface TaggedTree extends Tree {
    TaggedTree[] branches();
    Tag tags();
}
