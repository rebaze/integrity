/*
 * Copyright (c) 2015 rebaze GmbH
 * All rights reserved.
 *
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package org.rebaze.integrity.tree.util;

import java.io.PrintStream;

import org.rebaze.integrity.tree.api.Tree;

public class TreeConsoleFormatter
{

    public TreeConsoleFormatter()
    {
        this( System.out );
    }

    final private PrintStream m_out;

    public TreeConsoleFormatter( PrintStream ps )
    {
        m_out = ps;
    }

    public void displayTree( int depth, Tree tree )
    {
        if ( depth == 0 )
            m_out.println( " ---- TREE ----------" );
        m_out.print( "+" );
        for ( int i = 0; i < depth; i++ )
        {
            m_out.print( "--" );
        }
        m_out.println( " " + tree.toString() );
        depth++;
        Tree[] elements = tree.branches();
        int count = ( elements.length < 10 ) ? elements.length : 10;
        for ( int i = 0; i < count; i++ )
        {
            displayTree( depth, elements[i] );
        }
    }

    public void prettyPrint( int depth, Tree tree )
    {
        if ( depth == 0 )
            m_out.println( " ---- TREE: " );
        m_out.print( "+" );
        for ( int i = 0; i < depth; i++ )
        {
            m_out.print( "--" );
        }
        m_out.println( " " + tree);
        depth++;
        for ( Tree sub : tree.branches() )
        {
            prettyPrint( depth, sub );
        }
    }

    public void prettyPrint(  Tree... trees )
    {
        for (Tree tree : trees) {
            prettyPrint(0, tree);
        }
    }
}
