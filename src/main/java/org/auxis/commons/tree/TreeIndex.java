/*
 * Copyright (c) 2012-2013 rebaze GmbH
 * All rights reserved. 
 * 
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package org.auxis.commons.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.auxis.commons.tree.annotated.Tag;

/**
 * Index implementation that augments a given tree with some extra index accessors like find by
 * selector and tag.
 *
 * @author Toni Menzel <toni.menzel@rebaze.com>
 */
public class TreeIndex extends AbstractDelegateTree
{
    final private Map<Selector, TreeIndex> m_selectors = new HashMap<Selector, TreeIndex>();
    final private TreeIndex[] m_sub;

    public TreeIndex( Tree tree )
    {
        super( tree );
        guardIlegalWrappedTreeIndex( tree );
        m_sub = createDeepIndex( tree );
    }

    private TreeIndex[] createDeepIndex( Tree tree )
    {
        List<TreeIndex> sub = new ArrayList<TreeIndex>();

        for ( Tree h : tree.branches() )
        {
            TreeIndex idx = new TreeIndex( h );
            TreeIndex previous = m_selectors.get( h.selector() );
            if ( previous != null )
            {
                if ( !h.fingerprint().equals( previous.fingerprint() ) )
                {
                    throw new IllegalArgumentException( "Tree contains selector on same level with identical selector but different hashes: " + idx + " and " + previous );
                }

            }
            else
            {
                sub.add( idx );
                m_selectors.put( h.selector(), idx );

            }
        }
        return sub.toArray( new TreeIndex[sub.size()] );
    }

    private void guardIlegalWrappedTreeIndex( Tree tree )
    {
        if ( tree instanceof TreeIndex )
        {
            throw new RuntimeException( "You should not wrap a TreeIndex.. waaaayyy too expensive.." );
        }
    }

    public TreeIndex select( Selector selector )
    {
        return m_selectors.get( selector );
    }

    public TreeIndex select( Selector... selectors )
    {
        TreeIndex r = this;
        for ( Selector s : selectors )
        {
            r = r.select( s );
        }
        return r;
    }

    @Override
    public TreeIndex[] branches()
    {
        return m_sub;
    }

    public String toString()
    {
        return "TreeIndex of [" + super.toString() + "] with selectors: " + m_selectors.size();
    }

}
