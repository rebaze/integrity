package com.rebaze.trees.core.internal;

import static com.rebaze.trees.core.Selector.*;
import static com.rebaze.trees.core.util.TreeSession.nodes;
import static org.junit.Assert.*;

import com.rebaze.trees.core.Tree;
import org.junit.Test;

import com.rebaze.trees.core.TreeBuilder;
import com.rebaze.trees.core.util.DefaultTreeSessionFactory;
import com.rebaze.trees.core.util.TreeSession;

import java.io.IOException;

public class InMemoryTreeImplTest
{
    final private TreeSession session = new DefaultTreeSessionFactory().create();

    @Test
    public void testEmptyTreeSize()
    {
        assertEquals( "Trunk only", 1, nodes( session.createTreeBuilder().selector( selector( "foo" ) ).seal() ) );
    }

    @Test
    public void testAddsOnlyOnSingle()
    {
        assertEquals( "Trunk only", 1, nodes( session.createTreeBuilder().selector( selector( "foo" ) ).add( "data".getBytes() ).seal() ) );
    }

    @Test
    public void testSingleBranch()
    {
        assertEquals( 1, nodes( session.createTreeBuilder().selector( selector( "foo" ) ).branch( selector( "branch" ) ).seal() ) );
    }

    @Test
    public void testMore()
    {
        TreeBuilder tb = session.createTreeBuilder().selector( selector( "trunk" ) );
        tb.branch( selector( "branch2" ) ).add( "data1".getBytes() );
        tb.branch( selector( "branch3" ) ).add( "data1".getBytes() );
        assertEquals( 3, nodes( tb.seal() ) );
    }

    @Test
    public void testSameDataSameSelector() throws IOException
    {
        TreeBuilder c1 = session.createTreeBuilder();
        c1.branch( selector( "db1" ) ).add( "data".getBytes() );
        c1.branch( selector( "db1" ) ).add( "data".getBytes() );
        c1.branch( selector( "db3" ) ).add( "data".getBytes() );
        assertEquals( 2, c1.seal().branches().length );
    }

    @Test
    public void testCountLeadsAndBranches() throws IOException
    {
        TreeBuilder c1 = session.createTreeBuilder();
        c1.branch( selector( "db1" ) ).branch( selector( "a" ) ).add( "data".getBytes() );
        c1.branch( selector( "db2" ) ).add( "data".getBytes() );
        c1.branch( selector( "db3" ) ).add( "data".getBytes() );
        Tree result = c1.seal();
        assertEquals( 3, TreeSession.leafs( result ) );
        assertEquals( 5, TreeSession.nodes( result ) );

    }
}
