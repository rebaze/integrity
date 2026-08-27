/*
 * Copyright (c) 2012-2013 rebaze GmbH
 * All rights reserved. 
 * 
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package org.rebaze.integrity.tree.internal.operators;

import static org.rebaze.integrity.tree.api.Selector.selector;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.rebaze.integrity.tree.api.Selector;
import org.rebaze.integrity.tree.api.Tree;
import org.rebaze.integrity.tree.api.TreeIndex;
import org.rebaze.integrity.tree.api.TreeSession;
import org.rebaze.integrity.tree.util.DefaultTreeSessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.rebaze.integrity.tree.api.TreeBuilder;
import org.rebaze.integrity.tree.util.TreeConsoleFormatter;

public class DiffTreeCombinatorTest
{

    private static TreeConsoleFormatter FORMAT = TreeConsoleFormatter.formatter();
    private TreeSession session =  new DefaultTreeSessionFactory().create();

    @Test
    public void diffIdenticalEmpty()
    {
        Tree sn1 = session.createTreeBuilder().selector( Selector.selector( "c1" ) ).seal();
        Tree sn2 = session.createTreeBuilder().selector( Selector.selector( "c2" ) ).seal();
        Tree result = new DiffTreeCombiner(session).combine( sn1, sn2 );
        assertEquals( 0, result.branches().length, "Should no elements" );
    }

    @Test
    public void diffIdenticalMedium()
    {
        TreeBuilder b1 = session.createTreeBuilder().selector( Selector.selector( "c1" ) );
        b1.add( "Some".getBytes() );
        Tree sn1 = b1.seal();
        TreeBuilder b2 = session.createTreeBuilder().selector( Selector.selector( "c2" ) );
        b2.add( "Some".getBytes() );
        Tree sn2 = b2.seal();
        Tree result = new DiffTreeCombiner(session).combine( sn1, sn2 );
        assertEquals( 0, result.branches().length, "Should no elements" );
    }

    @Test
    public void diffDifferentSimple()
    {
        TreeBuilder b1 = session.createTreeBuilder().selector( Selector.selector( "c1" ) );
        b1.add( "Some".getBytes() );
        Tree sn1 = b1.seal();
        TreeBuilder b2 = session.createTreeBuilder().selector( Selector.selector( "c2" ) );
        Tree sn2 = b2.seal();
        Tree result = new DiffTreeCombiner(session).combine( sn1, sn2 );
        assertEquals( 1, result.branches().length, "Should no elements" );
        Assertions.assertEquals( Selector.selector( "c2" ), result.branches()[0].selector(), "Select what is different" );
        assertEquals( DiffTreeCombiner.MODIFIED, result.branches()[0].tags(), "Select what is different" );

    }

    @Test
    public void diff() throws IOException
    {
        // Setup State 1
        TreeBuilder c1 = session.createTreeBuilder().selector( Selector.selector( "db1" ) );
        c1.branch( Selector.selector( "table1" ) ).add( "Data1".getBytes() );
        c1.branch( Selector.selector( "table2" ) ).add( "Data2".getBytes() );
        c1.branch( Selector.selector( "table3" ) ).add( "Data2".getBytes() );

        Tree sn1 = c1.seal();

        // Setup State 2
        TreeBuilder c2 = session.createTreeBuilder().selector( Selector.selector( "db1" ) );
        c2.branch( Selector.selector( "table1" ) ).add( "Data1".getBytes() );
        c2.branch( Selector.selector( "table2" ) ).add( "Data2Mod".getBytes() );
        c2.branch( Selector.selector( "table4" ) ).add( "Data1".getBytes() );
        Tree sn2 = c2.seal();

        // Actually diff
        TreeIndex result = TreeSession.wrapAsIndex( new DiffTreeCombiner(session).combine( sn1, sn2 ) );

        // Display both for visual reference..
        FORMAT.prettyPrint( sn1,sn2,result );

        assertEquals( 3, result.select( Selector.selector( "db1" ) ).branches().length, "Detect 3 modifications" );
        assertEquals( DiffTreeCombiner.MODIFIED, result.select( Selector.selector( "db1" ) ).select( Selector.selector( "table2" ) ).tags(), "Modification in db2.table2" );
        assertEquals( DiffTreeCombiner.REMOVED, result.select( Selector.selector( "db1" ) ).select( Selector.selector( "table3" ) ).tags(), "Modification in db2.table2" );
        assertEquals( DiffTreeCombiner.ADDED, result.select( Selector.selector( "db1" ) ).select( Selector.selector( "table4" ) ).tags(), "Modification in db2.table2" );
    }
}
