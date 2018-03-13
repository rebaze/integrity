/*
 * Copyright (c) 2015 rebaze GmbH
 * All rights reserved.
 *
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package org.rebaze.integrity.tree.internal;

import org.rebaze.integrity.tree.api.*;

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

    protected void guardIlegalWrappedTreeIndex( Tree tree )
    {
        if ( tree instanceof AbstractDelegateTree )
        {
            throw new IllegalArgumentException( "Should not already be wrapped.. waaaayyy too expensive.." );
        }
    }

    public boolean selectable()
    {
        return m_tree.selector() != null;
    }

    @Override
    public String fingerprint()
    {
        return m_tree.value().hash();
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
        return m_tree.toString();
    }

    @Override
    public Tag tags()
    {
        return m_tree.tags();
    }

    @Override
    public TreeValue value()
    {
        return m_tree.value();
    }
}
