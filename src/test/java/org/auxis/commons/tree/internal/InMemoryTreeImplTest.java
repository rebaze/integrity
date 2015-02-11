package org.auxis.commons.tree.internal;

import static org.auxis.commons.tree.Selector.*;
import static org.junit.Assert.*;

import org.auxis.commons.tree.TreeBuilder;
import org.auxis.commons.tree.util.TreeTools;
import org.junit.Test;

public class InMemoryTreeImplTest
{
    private TreeTools tools = new TreeTools();

    @Test
    public void testEmptyTreeSize()
    {

        assertEquals( "Trunk only", 1, tools.nodes( tools.createTreeBuilder().selector( selector( "foo" ) ).seal() ) );
    }

    @Test
    public void testAddsOnlyOnSingle()
    {
        assertEquals( "Trunk only", 1, tools.nodes( tools.createTreeBuilder().selector( selector( "foo" ) ).add( "data".getBytes() ).seal() ) );
    }

    @Test
    public void testSingleBranch()
    {
        assertEquals( 1, tools.nodes( tools.createTreeBuilder().selector( selector( "foo" ) ).branch( selector( "branch" ) ).seal() ) );
    }

    @Test
    public void testMore()
    {
        TreeBuilder tb = tools.createTreeBuilder().selector( selector( "trunk" ) );
        tb.branch( selector( "branch2" ) ).add( "data1".getBytes() );
        tb.branch( selector( "branch3" ) ).add( "data1".getBytes() );
        assertEquals( 3, tools.nodes( tb.seal() ) );
    }
}
