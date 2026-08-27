package org.rebaze.integrity.tree.internal;

import static org.junit.jupiter.api.Assertions.*;

import org.rebaze.integrity.tree.api.Selector;
import org.rebaze.integrity.tree.api.Tree;
import org.rebaze.integrity.tree.api.TreeBuilder;
import org.rebaze.integrity.tree.api.TreeSession;
import org.rebaze.integrity.tree.util.DefaultTreeSessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class InMemoryTreeImplTest
{
    final private TreeSession session = new DefaultTreeSessionFactory().create();

    @Test
    public void testEmptyTreeSize()
    {
        Assertions.assertEquals( 1, TreeSession.nodes( session.createTreeBuilder().selector( Selector.selector( "foo" ) ).seal() ), "Trunk only" );
    }

    @Test
    public void testAddsOnlyOnSingle()
    {
        Assertions.assertEquals( 1, TreeSession.nodes( session.createTreeBuilder().selector( Selector.selector( "foo" ) ).add( "data".getBytes() ).seal() ), "Trunk only" );
    }

    @Test
    public void testSingleBranch()
    {
        Assertions.assertEquals( 1, TreeSession.nodes( session.createTreeBuilder().selector( Selector.selector( "foo" ) ).branch( Selector.selector( "branch" ) ).seal() ) );
    }

    @Test
    public void testMore()
    {
        TreeBuilder tb = session.createTreeBuilder().selector( Selector.selector( "trunk" ) );
        tb.branch( Selector.selector( "branch2" ) ).add( "data1".getBytes() );
        tb.branch( Selector.selector( "branch3" ) ).add( "data1".getBytes() );
        Assertions.assertEquals( 3, TreeSession.nodes( tb.seal() ) );
    }

    @Test
    public void testSameDataSameSelector() throws IOException
    {
        TreeBuilder c1 = session.createTreeBuilder();
        c1.branch( Selector.selector( "db1" ) ).add( "data".getBytes() );
        c1.branch( Selector.selector( "db1" ) ).add( "data".getBytes() );
        c1.branch( Selector.selector( "db3" ) ).add( "data".getBytes() );
        assertEquals( 2, c1.seal().branches().length );
    }

    @Test
    public void testCountLeadsAndBranches() throws IOException
    {
        TreeBuilder c1 = session.createTreeBuilder();
        c1.branch( Selector.selector( "db1" ) ).branch( Selector.selector( "a" ) ).add( "data".getBytes() );
        c1.branch( Selector.selector( "db2" ) ).add( "data".getBytes() );
        c1.branch( Selector.selector( "db3" ) ).add( "data".getBytes() );
        Tree result = c1.seal();
        assertEquals( 3, TreeSession.leafs( result ) );
        assertEquals( 5, TreeSession.nodes( result ) );

    }
}
