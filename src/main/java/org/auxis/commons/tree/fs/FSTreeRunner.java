/*
 * Copyright (c) 2015 rebaze GmbH
 * All rights reserved.
 *
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package org.auxis.commons.tree.fs;

import org.auxis.commons.tree.Tree;
import org.auxis.commons.tree.TreeBuilder;
import org.auxis.commons.tree.util.DefaultTreeSessionFactory;
import org.auxis.commons.tree.util.StreamTreeBuilder;
import org.auxis.commons.tree.util.TreeConsoleFormatter;
import org.auxis.commons.tree.util.TreeSession;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.auxis.commons.tree.Selector.selector;

/**
 *
 */
public class FSTreeRunner
{
    private final static TreeConsoleFormatter FORMAT = new TreeConsoleFormatter();

    public static void main( String[] args )
    {
        args = new String[] { "/Users/tonit/devel/auxis" };
        FSTreeRunner runner = new FSTreeRunner();

        TreeSession session = new DefaultTreeSessionFactory().create();

        if ( args.length >= 1 )
        {
            System.out.println("Building tree..");

            Tree base = new FSTreeRunner().collect( session.createTreeBuilder(), new File( args[0] ) ).seal();
            System.out.println("Indexed with " + TreeSession.nodes( base ) + " nodes.");

            // build deep index:
            //FORMAT.prettyPrint( base );
            Map<Tree, Integer> map = new HashMap<Tree, Integer>();
            walk( map, base );
            for ( Tree s : map.keySet() )
            {
                int count = map.get( s );
                if ( count > 1 && TreeSession.nodes(s) > 2)
                {
                    // find that tree within base.
                    Tree result = session.find( base, s);

                    System.out.println( "# Found" + s + " : " + map.get( s ) );
                    FORMAT.prettyPrint( result );
                }
            }

        }
        else
        {
            System.err.println( "Specify <Folder1>" );
        }
    }

    private static void walk( Map<Tree, Integer> map, Tree input )
    {
        if (TreeSession.isWrapper( input )) {
            // go deeper without counting:
            walk(map,input.branches()[0]);
            return;
        }

        if ( map.containsKey( input ) )
        {
            map.put( input, map.get( input) + 1 );
        }
        else
        {
            map.put( input, 1 );
        }
        for ( Tree tree : input.branches() )
        {
            walk( map, tree );
        }
    }

    public TreeBuilder collect( TreeBuilder builder, File base )
    {
        builder.selector( selector( base.getName() ) );
        for ( File f : base.listFiles() )
        {
            if ( f.isHidden() || !f.canRead() )
                continue;
            TreeBuilder sub = builder.branch( selector( f.getName() ) );
            if ( f.isDirectory() )
                collect( sub, f );
            else
            {
                new StreamTreeBuilder( sub ).add( f );
            }
        }
        return builder;
    }

}

