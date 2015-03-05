/*
 * Copyright (c) 2012-2013 rebaze GmbH
 * All rights reserved. 
 * 
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package org.auxis.commons.tree;

import static org.auxis.commons.tree.Selector.selector;
import static org.auxis.commons.tree.util.TreeTools.wrapAsIndex;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.auxis.commons.tree.operators.DiffTreeCombiner;
import org.auxis.commons.tree.util.TreeConsoleFormatter;
import org.auxis.commons.tree.util.TreeTools;
import org.junit.Test;

public class DiffToolTest
{

    private static TreeConsoleFormatter FORMAT = new TreeConsoleFormatter();
    private static TreeTools TOOLS =  TreeTools.treeTools();

    @Test
    public void diffIdenticalEmpty()
    {
        Tree sn1 = TOOLS.createTreeBuilder().selector( selector( "c1" ) ).seal();
        Tree sn2 = TOOLS.createTreeBuilder().selector( selector( "c2" ) ).seal();
        Tree result = TOOLS.diff( sn1, sn2 );
        assertEquals( "Should no elements", 0, result.branches().length );
    }

    @Test
    public void diffIdenticalMedium()
    {
        TreeBuilder b1 = TOOLS.createTreeBuilder().selector( selector( "c1" ) );
        b1.add( "Some".getBytes() );
        Tree sn1 = b1.seal();
        TreeBuilder b2 = TOOLS.createTreeBuilder().selector( selector( "c2" ) );
        b2.add( "Some".getBytes() );
        Tree sn2 = b2.seal();
        Tree result = TOOLS.diff( sn1, sn2 );
        assertEquals( "Should no elements", 0, result.branches().length );
    }

    @Test
    public void diffDifferentSimple()
    {
        TreeBuilder b1 = TOOLS.createTreeBuilder().selector( selector( "c1" ) );
        b1.add( "Some".getBytes() );
        Tree sn1 = b1.seal();
        TreeBuilder b2 = TOOLS.createTreeBuilder().selector( selector( "c2" ) );
        Tree sn2 = b2.seal();
        Tree result = TOOLS.diff( sn1, sn2 );
        assertEquals( "Should no elements", 1, result.branches().length );
        assertEquals( "Select what is different", selector( "c2" ), result.branches()[0].selector() );
        assertEquals( "Select what is different", DiffTreeCombiner.MODIFIED, result.branches()[0].tags() );

    }

    @Test
    public void diff() throws IOException
    {
        // Setup State 1
        TreeBuilder c1 = TOOLS.createTreeBuilder().selector( selector( "db1" ) );
        c1.branch( selector( "table1" ) ).add( "Data1".getBytes() );
        c1.branch( selector( "table2" ) ).add( "Data2".getBytes() );
        c1.branch( selector( "table3" ) ).add( "Data2".getBytes() );

        Tree sn1 = c1.seal();

        // Setup State 2
        TreeBuilder c2 = TOOLS.createTreeBuilder().selector( selector( "db1" ) );
        c2.branch( selector( "table1" ) ).add( "Data1".getBytes() );
        c2.branch( selector( "table2" ) ).add( "Data2Mod".getBytes() );
        c2.branch( selector( "table4" ) ).add( "Data1".getBytes() );
        Tree sn2 = c2.seal();

        // Actually diff
        TreeIndex result = wrapAsIndex( TOOLS.diff( sn1, sn2 ) );

        // Display both for visual reference..
        FORMAT.prettyPrint( sn1 );
        FORMAT.prettyPrint( sn2 );
        FORMAT.prettyPrint( result );

        assertEquals( "Detect 3 modifications", 3, result.select( selector( "db1" ) ).branches().length );
        assertEquals( "Modification in db2.table2", DiffTreeCombiner.MODIFIED, result.select( selector( "db1" ) ).select( selector( "table2" ) ).tags() );
        assertEquals( "Modification in db2.table2", DiffTreeCombiner.REMOVED, result.select( selector( "db1" ) ).select( selector( "table3" ) ).tags() );
        assertEquals( "Modification in db2.table2", DiffTreeCombiner.ADDED, result.select( selector( "db1" ) ).select( selector( "table4" ) ).tags() );
    }
}
