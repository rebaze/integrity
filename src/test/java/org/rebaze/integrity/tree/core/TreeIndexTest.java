package org.rebaze.integrity.tree.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.rebaze.integrity.tree.api.Selector;
import org.rebaze.integrity.tree.api.TreeBuilder;
import org.rebaze.integrity.tree.api.TreeIndex;
import org.rebaze.integrity.tree.api.TreeSession;
import org.rebaze.integrity.tree.util.DefaultTreeSessionFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.rebaze.integrity.tree.api.Tag.tag;

public class TreeIndexTest
{
    private TreeSession session =  new DefaultTreeSessionFactory().create();

    @Test
    public void compoundSelectArrayTest() throws IOException
    {
        TreeIndex sn1 = sampleTree();
        assertEquals( tag( "findme" ), sn1.select( Selector.selector( "table1", "other" ) ).tags() );
        assertNull( sn1.select( Selector.selector( "table1", "wrong" ) ) );
        assertNull( sn1.select( Selector.selector( "table2" ) ) );
    }

    @Test
    public void testSameSelectorDifferentData() throws IOException
    {
        TreeBuilder c1 = session.createTreeBuilder();
        c1.branch( Selector.selector( "db1" ) ).add( "data".getBytes() );
        c1.branch( Selector.selector( "db2" ) ).add( "dat2".getBytes() );
        c1.branch( Selector.selector( "db1" ) ).add( "data3".getBytes() );
        Assertions.assertEquals( 2, TreeSession.wrapAsIndex( c1.seal() ).branches().length );
    }

    @Test
    public void testSameDataDifferentSelector() throws IOException
    {
        TreeBuilder c1 = session.createTreeBuilder();
        c1.branch( Selector.selector( "db1" ) ).add( "data".getBytes() );
        c1.branch( Selector.selector( "db2" ) ).add( "dat2".getBytes() );
        c1.branch( Selector.selector( "db3" ) ).add( "data3".getBytes() );
        Assertions.assertEquals( 3, TreeSession.wrapAsIndex( c1.seal() ).branches().length );
    }


    @Test
    public void testSameDataSameSelector() throws IOException
    {
        TreeBuilder c1 = session.createTreeBuilder();
        c1.branch( Selector.selector( "db1" ) ).add( "data".getBytes() );
        c1.branch( Selector.selector( "db1" ) ).add( "data".getBytes() );
        c1.branch( Selector.selector( "db3" ) ).add( "data".getBytes() );
        Assertions.assertEquals( 2, TreeSession.wrapAsIndex( c1.seal() ).branches().length );
    }

    @Test
    public void testContains() throws IOException
    {
        TreeBuilder c1 = session.createTreeBuilder();
        c1.branch( "db1" ).add( "data" );
        c1.branch( "db2" ).add( "data1" );
        c1.branch( "db3" ).add( "data2" );
        c1.branch( "db4" ).branch("deep").add( "foo" );

        TreeIndex idx = TreeSession.wrapAsIndex( c1.seal());
        assertTrue(idx.contains(idx.branches()[0]), "tree contains branch part");
        assertTrue(idx.contains(idx.branches()[1]), "tree contains branch part");
        assertTrue(idx.contains(idx.branches()[2]), "tree contains branch part");
        assertTrue(idx.contains(idx), "tree contains itself");
        assertFalse(idx.contains(null), "tree does not contain null");
        assertFalse(idx.contains(session.createTreeBuilder().add("data4").seal()), "tree does not contain other tree");

        assertTrue(idx.contains(session.createTreeBuilder().add("foo").seal()), "tree contained");

    }

    private TreeIndex sampleTree()
    {
        TreeBuilder c1 = session.createTreeBuilder().selector( Selector.selector( "db1" ) );
        c1.branch( Selector.selector( "table1" ) ).branch( Selector.selector( "other" ) ).add( "Data1".getBytes() ).tag( tag( "findme" ) );
        TreeIndex sn1 = new TreeIndex( c1.seal() );
        return sn1;
    }
}
