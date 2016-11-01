/*
 * Copyright (c) 2012-2013 rebaze GmbH
 * All rights reserved. 
 * 
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package org.rebaze.integrity.tree;

public class Selector
{
    public static final Selector DEFAULT = selector( "<DEFAULT>" );
    final private String m_name;

    public static Selector selector( String s )
    {
        return new Selector( s );
    }

    public static Selector[] selector( String... s )
    {
        Selector[] res = new Selector[s.length];
        for ( int i = 0; i < s.length; i++ )
        {
            res[i] = selector( s[i] );
        }
        return res;
    }

    public Selector( String s )
    {
        assert ( s != null );
        m_name = s;
    }

    public String name()
    {
        return m_name;
    }

    public int hashCode()
    {
        return m_name.hashCode();
    }

    public boolean equals( Object other )
    {
        if ( other instanceof Selector )
        {
            Selector ot = ( Selector ) other;
            return m_name.equals( ot.name() );
        }
        return false;
    }

    public String toString()
    {
        return ":" + m_name;
    }
}
