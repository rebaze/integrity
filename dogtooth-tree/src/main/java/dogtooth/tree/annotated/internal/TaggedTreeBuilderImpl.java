package dogtooth.tree.annotated.internal;

import dogtooth.tree.Tree;
import dogtooth.tree.TreeBuilder;
import dogtooth.tree.annotated.Tag;
import dogtooth.tree.annotated.TaggedTree;
import dogtooth.tree.annotated.TaggedTreeBuilder;

public class TaggedTreeBuilderImpl implements TaggedTreeBuilder {

    private final TreeBuilder m_delegate;
    private Tag m_tag;
    
    public TaggedTreeBuilderImpl(TreeBuilder delegate) {
        m_delegate = delegate;
    }
    
    @Override
    public TaggedTreeBuilder add( byte[] bytes ) {
         m_delegate.add( bytes );
         return this;
    }

    @Override
    public TaggedTreeBuilder selector( String selector ) {
         m_delegate.selector( selector );
         return this;
    }

    @Override
    public TaggedTreeBuilder branch( String selector ) {
         return new TaggedTreeBuilderImpl(m_delegate.branch( selector) );
         
    }

    @Override
    public TaggedTree seal() {
        Tree tree = m_delegate.seal();
        return new TaggedTreeImpl(tree,m_tag);
    }

    @Override
    public TaggedTreeBuilder tag( Tag tag ) {
       m_tag = tag;
       return this;
    }
    
}
