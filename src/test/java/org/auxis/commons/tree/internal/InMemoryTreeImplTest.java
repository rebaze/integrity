package org.auxis.commons.tree.internal;

import static org.auxis.commons.tree.Selector.*;
import static org.junit.Assert.*;

import org.auxis.commons.tree.TreeBuilder;
import org.auxis.commons.tree.util.TreeSession;
import org.junit.Test;

import java.io.IOException;

public class InMemoryTreeImplTest
{
    private TreeSession session = TreeSession.getSession();

    @Test
    public void testEmptyTreeSize()
    {
        assertEquals( "Trunk only", 1, session.nodes( session.createTreeBuilder().selector( selector( "foo" ) ).seal() ) );
    }

    @Test
    public void testAddsOnlyOnSingle()
    {
        assertEquals( "Trunk only", 1, session.nodes( session.createTreeBuilder().selector( selector( "foo" ) ).add( "data".getBytes() ).seal() ) );
    }

    @Test
    public void testSingleBranch()
    {
        assertEquals( 1, session.nodes( session.createTreeBuilder().selector( selector( "foo" ) ).branch( selector( "branch" ) ).seal() ) );
    }

    @Test
    public void testMore()
    {
        TreeBuilder tb = session.createTreeBuilder().selector( selector( "trunk" ) );
        tb.branch( selector( "branch2" ) ).add( "data1".getBytes() );
        tb.branch( selector( "branch3" ) ).add( "data1".getBytes() );
        assertEquals( 3, session.nodes( tb.seal() ) );
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
}
