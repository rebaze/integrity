/*
 * Copyright 2014 rebaze GmbH. All Rights Reserved.
 */
package org.auxis.commons.tree;

import org.auxis.commons.tree.annotated.Tag;
import org.auxis.commons.tree.util.TreeTools;

/**
 * Augments a given tree as {@link TreeBuilder}. Used to incorporate existing {@link Tree}s in Tree
 * construction.
 *
 * @author Toni Menzel <toni.menzel@rebaze.com>
 */
public class StaticTreeBuilder implements TreeBuilder
{
    private Tree m_tree;
    private final TreeTools m_session;

    public StaticTreeBuilder(Tree encapsulatedTree, TreeTools treeTools)
    {
        m_tree = encapsulatedTree;
        m_session = treeTools;
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
        // TODO: Use compound tag!
        m_tree = m_session.createTree(m_tree.selector(),m_tree.fingerprint(),m_tree.branches(), tag);
        return this;
    }

    @Override
    public Tree seal()
    {
        return m_tree;
    }
}
