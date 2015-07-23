/*
 * Copyright (c) 2012-2013 rebaze GmbH
 * All rights reserved. 
 * 
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package com.rebaze.commons.tree.internal;

import com.rebaze.commons.tree.Selector;
import com.rebaze.commons.tree.Tree;
import com.rebaze.commons.tree.annotated.Tag;

/**
 * Default implementation not really suitable for very large trees but fast and simple.
 *
 * @author Toni Menzel <toni.menzel@rebaze.com>
 *
 */
public class InMemoryTreeImpl implements Tree
{
    final private String m_hashValue;
    final private Tree[] m_subs;
    final private Selector m_selector;
    final private Tag m_tag;

    public InMemoryTreeImpl( Selector selector, String hashValue, Tree[] subs, Tag tag )
    {
        m_selector = selector;
        m_hashValue = hashValue;
        m_subs = subs;
        m_tag = tag;
    }

    @Override
    public String fingerprint()
    {
        return m_hashValue;
    }

    @Override
    public Selector selector()
    {
        return m_selector;
    }

    @Override
    public Tree[] branches()
    {
        return m_subs;
    }

    public String toString()
    {
        return pretty(m_hashValue.substring( 0, 6 )) +  pretty(m_selector) + "  #" + m_subs.length + pretty( m_tag);
    }

    private String pretty(Object thing) {
        return (thing != null ? " [" + thing.toString() + "] " : " [] ");
    }

    public int hashCode()
    {
        return m_hashValue.hashCode();
    }

    public boolean equals( Object other )
    {
        if ( other instanceof Tree )
        {
            return m_hashValue.equals( ( ( Tree ) other ).fingerprint() );
        }
        else
        {
            throw new RuntimeException( "Should not come here.." );
        }
    }

    @Override
    public Tag tags()
    {
        return m_tag;
    }
}
