/*
 * Copyright (c) 2012-2013 rebaze GmbH
 * All rights reserved. 
 * 
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package com.rebaze.trees.core;

import java.util.HashSet;
import java.util.Set;

public class Tag
{
    final private Set<String> m_tag;

    public static Tag tag( String... tags )
    {
        return new Tag( tags );
    }

    public Tag( String... tag )
    {
        m_tag = new HashSet<String>();
        for ( String t : tag )
        {
            m_tag.add( t );
        }
    }

    public boolean contains( Tag other )
    {
        return m_tag.contains( other );
    }

    public String toString()
    {
        return m_tag.toString();
    }

    public int hashCode()
    {
        return m_tag.hashCode();
    }

    public boolean equals( Object other )
    {
        if ( other instanceof Tag )
        {
            Tag ot = ( Tag ) other;
            return ot.toString().equals( toString() );
        }
        return false;
    }
}
