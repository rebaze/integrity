package dogtooth.tree.annotated.internal;

import dogtooth.tree.Tree;
import dogtooth.tree.annotated.Tag;
import dogtooth.tree.annotated.TaggedTree;

public class TaggedTreeImpl implements TaggedTree {

    final private Tree m_delegate;
    final private Tag m_tags;

    public TaggedTreeImpl(Tree delegate, Tag tag) {
        m_delegate = delegate;
        m_tags = tag;
    }
    
    @Override
    public String fingerprint() {
        return m_delegate.fingerprint();
    }

    @Override
    public String selector() {
        return m_delegate.selector();
    }

    @Override
    public TaggedTree[] branches() {
        return (TaggedTree[]) m_delegate.branches();
    }

    @Override
    public long effectiveSize() {
        return m_delegate.effectiveSize();
    }

    @Override
    public Tag tags() {
        return m_tags;
    }

}
