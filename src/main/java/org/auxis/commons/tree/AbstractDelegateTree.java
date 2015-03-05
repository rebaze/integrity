package org.auxis.commons.tree;

import org.auxis.commons.tree.annotated.Tag;

/**
 * Created by tonit on 05/03/15.
 */
public abstract class AbstractDelegateTree implements Tree
{
    final private Tree m_tree;

    public AbstractDelegateTree( Tree tree )
    {
        guardIlegalWrappedTreeIndex( tree );
        m_tree = tree;
    }

    private void guardIlegalWrappedTreeIndex( Tree tree )
    {
        if ( tree instanceof AbstractDelegateTree )
        {
            throw new RuntimeException( "Should not already be wrapped.. waaaayyy too expensive.." );
        }
    }

    public boolean selectable()
    {
        return m_tree.selector() != null;
    }

    @Override
    public String fingerprint()
    {
        return m_tree.fingerprint();
    }

    @Override
    public Selector selector()
    {
        return m_tree.selector();
    }

    @Override
    public Tree[] branches()
    {
        return m_tree.branches();
    }

    public String toString()
    {
        return "TreeIndex of [" + m_tree.toString() + "] ";
    }

    @Override
    public Tag tags()
    {
        return m_tree.tags();
    }

}
