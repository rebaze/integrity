package dogtooth.tree.annotated.internal;

import dogtooth.tree.Tree;
import dogtooth.tree.TreeBuilder;
import dogtooth.tree.annotated.Tag;
import dogtooth.tree.annotated.TaggedTreeBuilder;

public class TaggedTreeBuilderImpl implements TaggedTreeBuilder {

    private final TreeBuilder m_delegate;
    private Tag m_tag;
    
    public TaggedTreeBuilderImpl(TreeBuilder delegate) {
        m_delegate = delegate;
    }
    
    @Override
    public TreeBuilder add( byte[] bytes ) {
        return m_delegate.add( bytes );
    }

    @Override
    public TreeBuilder selector( String selector ) {
        return m_delegate.selector( selector );
    }

    @Override
    public TreeBuilder branch( String selector ) {
        return m_delegate.branch( selector );
    }

    @Override
    public Tree seal() {
        return m_delegate.seal();
    }

    @Override
    public TreeBuilder tag( Tag tag ) {
       m_tag = tag;
       return this;
    }
    
}
