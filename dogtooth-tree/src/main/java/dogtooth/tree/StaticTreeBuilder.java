/*
 * Copyright 2014 rebaze GmbH. All Rights Reserved.
 */
package dogtooth.tree;

import dogtooth.tree.annotated.Tag;

/**
 * Augments a given tree as {@link TreeBuilder}. Used to incorporate existing {@link Tree}s in Tree construction.
 *
 * @author Toni Menzel <toni.menzel@rebaze.com>
 */
public class StaticTreeBuilder implements TreeBuilder {

    private final Tree m_tree;

    public StaticTreeBuilder( Tree encapsulatedTree )
    {
        m_tree = encapsulatedTree;
    }

    @Override
    public TreeBuilder add( byte[] bytes )
    {
        throw new UnsupportedOperationException( "Static TreeBuilder does not allow modification." );
    }

    @Override
    public TreeBuilder selector( Selector selector )
    {
        throw new UnsupportedOperationException( "Static TreeBuilder does not allow modification." );
    }

    @Override
    public TreeBuilder branch( Selector selector )
    {
        throw new UnsupportedOperationException( "Static TreeBuilder does not allow modification." );
    }

    @Override
    public TreeBuilder branch( Tree subtree )
    {
        throw new UnsupportedOperationException( "Static TreeBuilder does not allow modification." );
    }

    @Override
    public TreeBuilder tag( Tag tag )
    {
        throw new UnsupportedOperationException( "Static TreeBuilder does not allow modification." );
    }

    @Override
    public Tree seal()
    {
        return m_tree;
    }
}
