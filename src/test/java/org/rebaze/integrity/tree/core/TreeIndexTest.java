package org.rebaze.integrity.tree.core;

import org.rebaze.integrity.tree.Selector;
import org.rebaze.integrity.tree.TreeBuilder;
import org.rebaze.integrity.tree.TreeIndex;
import org.rebaze.integrity.tree.TreeSession;
import org.rebaze.integrity.tree.util.DefaultTreeSessionFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.rebaze.integrity.tree.Selector.selector;
import static org.rebaze.integrity.tree.Tag.tag;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        Assert.assertEquals( 2, TreeSession.wrapAsIndex( c1.seal() ).branches().length );
    }

    @Test
    public void testSameDataDifferentSelector() throws IOException
    {
        TreeBuilder c1 = session.createTreeBuilder();
        c1.branch( Selector.selector( "db1" ) ).add( "data".getBytes() );
        c1.branch( Selector.selector( "db2" ) ).add( "dat2".getBytes() );
        c1.branch( Selector.selector( "db3" ) ).add( "data3".getBytes() );
        Assert.assertEquals( 3, TreeSession.wrapAsIndex( c1.seal() ).branches().length );
    }


    @Test
    public void testSameDataSameSelector() throws IOException
    {
        TreeBuilder c1 = session.createTreeBuilder();
        c1.branch( Selector.selector( "db1" ) ).add( "data".getBytes() );
        c1.branch( Selector.selector( "db1" ) ).add( "data".getBytes() );
        c1.branch( Selector.selector( "db3" ) ).add( "data".getBytes() );
        Assert.assertEquals( 2, TreeSession.wrapAsIndex( c1.seal() ).branches().length );
    }

    private TreeIndex sampleTree()
    {
        TreeBuilder c1 = session.createTreeBuilder().selector( Selector.selector( "db1" ) );
        c1.branch( Selector.selector( "table1" ) ).branch( Selector.selector( "other" ) ).add( "Data1".getBytes() ).tag( tag( "findme" ) );
        TreeIndex sn1 = new TreeIndex( c1.seal() );
        return sn1;
    }
}
